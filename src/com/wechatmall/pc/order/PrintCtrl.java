package com.wechatmall.pc.order;

import com.common.controllers.BaseCtrl;
//生成订单编号的公共类
//import com.common.services.OrderNumberGenerator;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.utils.PDFUtil;
import com.utils.UnitConversion;
import com.utils.UserSessionUtil;
import easy.util.DateTool;
import easy.util.NumberUtils;
import easy.util.UUIDTool;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintCtrl extends BaseCtrl {

    private static final String[] ORDER_GOODS_ONE_PAGE_ARR = {"order_num","buy_time","receiver_name","phone","address","pay_status","transport_type","pay_type"};

    public void printSingleOrder()throws UnsupportedEncodingException{
        JsonHashMap jhm = new JsonHashMap();
        String orderId = getPara("id");
        if (StringUtils.isEmpty(orderId)){
            jhm.putCode(0).putMessage("订单id为空");
            renderJson(jhm);
            return;
        }
        Record dataRecord = Db.findFirst("SELECT o.odesc AS order_desc,o.onum AS order_num,o.ocreate_time AS buy_time,o.oname AS receiver_name,o.ophone AS phone,o.oaddress AS address,d.name AS pay_status,dd.name AS transport_type ,ddd.name AS pay_type,o.ooriginal_sum AS product_sum,o.ocurrent_sum AS should_pay FROM w_orderform o LEFT JOIN w_dictionary d ON d.value=o.ostatus LEFT JOIN w_dictionary dd ON dd.value=o.otransport_type LEFT JOIN w_dictionary ddd ON ddd.value=o.opay_type WHERE o.oid=?", orderId);
        if(dataRecord == null){
            jhm.putCode(-1).putMessage("订单号有错误，请确认订单！");
            renderJson(jhm);
            return;
        }
        Map<String, String> onePageData = new HashMap<>();
        for(String s : ORDER_GOODS_ONE_PAGE_ARR){
            onePageData.put(s, dataRecord.getStr(s));
        }
        File onePageTemp = new File(this.getRequest().getSession().getServletContext().getRealPath("") + "/template/order.template");
        BufferedReader br = null;
        String onePageTempStr = "";
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(onePageTemp), "utf-8"));
            String str = br.readLine();
            while(str != null){
                onePageTempStr += str;
                str = br.readLine();
            }
            for(String s : ORDER_GOODS_ONE_PAGE_ARR){
                onePageTempStr = onePageTempStr.replace("${" + s + "}", onePageData.get(s));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        PDFUtil pdfUtil = new PDFUtil(this.getRequest(), "template");
        Map<String,Object> data = new HashMap();
        String table = "";
        String tableStr = "";
        String order_detail_sql="SELECT pnum AS productNum,odid AS product_id,odname AS product_name,odquantity AS product_quantity,odcurrent_price AS product_price FROM w_orderform_detail WHERE oid =?";
        List<Record> dataList = Db.find(order_detail_sql, orderId);
        if(dataList != null && dataList.size() > 0){
            int i = 1;
            int k=0;
            for(int j = 0; j < dataList.size(); j++,i++){
                k=j+1;
                Record r = dataList.get(j);
                tableStr += "<tr><td>"+ k +"</td><td>"+ r.get("productNum") + "</td><td>" + r.get("product_name") + "</td><td>" + r.get("product_quantity") + "</td><td>" + r.get("product_price") + "</td></tr>";
                if(i % 29 == 0){
                    table += onePageTempStr.replace("${table}", tableStr);
                    table += "<div class='pageNext'></div>";
                    tableStr = "";
                }
            }
            if(tableStr != null && tableStr.length() > 0){
                table += onePageTempStr.replace("${table}", tableStr);
            }
        }
        data.put("table", table);
        data.put("print_time", DateTool.GetDateTime());
        data.put("product_sum", dataRecord.getStr("product_sum"));
        data.put("should_pay", dataRecord.getStr("should_pay"));
        data.put("creater_name", "admin");
        data.put("order_desc", dataRecord.getStr("order_desc"));
        String content = pdfUtil.loadDataByTemplate(data, "orderTemplate.html");
        try {
            String name = UUIDTool.getUUID();
            pdfUtil.createPdf(content, this.getRequest().getSession().getServletContext().getRealPath("") + "/pdf/" + name + ".pdf");
            Record result = new Record();
            result.set("pageUrl", getRequest().getScheme()+"://"+getRequest().getServerName()+":"+getRequest() .getServerPort() +getRequest().getContextPath() + "/pdf/" + name + ".pdf");
            result.set("title", "客户订单");
            jhm.put("data", result);
        } catch (Exception e) {
            e.printStackTrace();
            jhm.putMessage(e.getMessage()).putCode(-1);
        }
        renderJson(jhm);
    }



}
