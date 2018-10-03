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

    //private static final String[] outgoing_goods_one_page_arr = {"warehouse_name","date","order_num","store_name"};
    private static final String[] ORDER_GOODS_ONE_PAGE_ARR = {"order_num","buy_time","receiver_name","phone","address","pay_status","transport_type","pay_type"};

    public void printSingleOrder()throws UnsupportedEncodingException{
        JsonHashMap jhm = new JsonHashMap();
        String orderId = getPara("id");
        if (StringUtils.isEmpty(orderId)){
            jhm.putCode(0).putMessage("订单id为空");
            renderJson(jhm);
            return;
        }
        Record dataRecord = Db.findFirst("SELECT o.onum AS order_num,o.ocreate_time AS buy_time,o.oname AS receiver_name,o.ophone AS phone,o.oaddress AS address,d.name AS pay_status,dd.name AS transport_type ,ddd.name AS pay_type,o.ooriginal_sum AS product_sum,o.ocurrent_sum AS should_pay FROM w_orderform o LEFT JOIN w_dictionary d ON d.value=o.ostatus LEFT JOIN w_dictionary dd ON dd.value=o.otransport_type LEFT JOIN w_dictionary ddd ON ddd.value=o.opay_type WHERE o.oid=?", orderId);
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
//        String title = "<table class=\"order-list\"><thead style=\"display:table-header-group\"><tr><td colspan=\"5\"><table class=\"order-top\"><tr><th colspan=\"2\" align=\"center\">出库单</th></tr><tr><td width=\"60%\">出库单号：${order_num}</td><td>仓库：${warehouse_name}</td></tr><tr><td width=\"60%\">出库日期：${date}</td><td>门店：${store_name}</td></tr></table></td></tr></thead></table>";
//        for(String s : outgoing_goods_one_page_arr){
//            title = title.replace("${" + s + "}", onePageData.get(s));
//        }
        String order_detail_sql="SELECT odid AS product_id,odname AS product_name,odquantity AS product_quantity,odcurrent_price AS product_price FROM w_orderform_detail WHERE oid =?";
        List<Record> dataList = Db.find(order_detail_sql, orderId);
        if(dataList != null && dataList.size() > 0){
            int i = 1;
            for(int j = 0; j < dataList.size(); j++,i++){
                Record r = dataList.get(j);
                tableStr += "<tr><td>" + r.get("product_id") + "</td><td>" + r.get("product_name") + "</td><td>" + r.get("product_quantity") + "</td><td>" + r.get("product_price") + "</td></tr>";
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
        //data.put("creater_name", new UserSessionUtil(getRequest()).getRealName());
        data.put("print_time", DateTool.GetDateTime());
        data.put("product_sum", dataRecord.getStr("product_sum"));
        data.put("freight", "100");
        data.put("should_pay", dataRecord.getStr("should_pay"));
        data.put("creater_name", "admin");
        String content = pdfUtil.loadDataByTemplate(data, "orderTemplate.html");
        try {
            String name = UUIDTool.getUUID();
            pdfUtil.createPdf(content, this.getRequest().getSession().getServletContext().getRealPath("") + "/pdf/" + name + ".pdf");
//            createPrintDetails(orderId, dataRecord, "outgoing_goods");
//            this.getResponse().sendRedirect(getRequest().getContextPath()  + "/pdf/" + name + ".pdf");
//            this.getRequest().getRequestDispatcher(getRequest().getContextPath()  + "/pdf/a.pdf");
            Record result = new Record();
            result.set("pageUrl", getRequest().getScheme()+"://"+getRequest().getServerName()+":"+getRequest() .getServerPort()  + "/pdf/" + name + ".pdf");
            result.set("title", "客户订单");
            jhm.put("data", result);
        } catch (Exception e) {
            e.printStackTrace();
            jhm.putMessage(e.getMessage()).putCode(-1);
        }
        renderJson(jhm);
    }

//    private void createPrintDetails(String orderId, Record dataRecord, String type){
//        List<Record> printDetailsList = Db.find("select * from print_details where order_id=? order by sort", orderId);
//        int sort = 1;
//        if(printDetailsList != null && printDetailsList.size() > 0){
//            sort += printDetailsList.get(printDetailsList.size() - 1).getInt("sort");
//        }
//        UserSessionUtil usu = new UserSessionUtil(getRequest());
//        Record printDetails = new Record();
//        printDetails.set("id", UUIDTool.getUUID());
//        printDetails.set("order_id", orderId);
//        printDetails.set("print_date", DateTool.GetDateTime());
//        printDetails.set("sort", sort);
//        printDetails.set("creater_id", usu.getUserId());
//        printDetails.set("creater_name", usu.getRealName());
//        printDetails.set("type", type);
//        printDetails.set("status", dataRecord.getStr("sostatus"));
//        printDetails.set("order_number", dataRecord.getStr("order_number"));
//        Db.save("print_details", printDetails);
//    }
//
//    public void printOutgoingGoodsOrder()  {
//        JsonHashMap jhm = new JsonHashMap();
//        String orderId = getPara("id");
//        Record dataRecord = Db.findFirst("SELECT so.out_time date, w.name warehouse_name, s.name store_name, so.order_number order_num, so.status sostatus FROM warehouse_out_order so, store s, warehouse w WHERE so.store_id = s.id and so.warehouse_id=w.id and so.id=?", orderId);
//        if(dataRecord == null){
//            jhm.putCode(-1).putMessage("订单号有错误，请确认订单！");
//            renderJson(jhm);
//            return;
//        }
//        Map<String, String> onePageData = new HashMap<>();
//        for(String s : outgoing_goods_one_page_arr){
//            onePageData.put(s, dataRecord.getStr(s));
//        }
//        File onePageTemp = new File(this.getRequest().getSession().getServletContext().getRealPath("") + "/template/outgoingGoodsOnePage.template");
//        BufferedReader br = null;
//        String onePageTempStr = "";
//        try {
//            br = new BufferedReader(new InputStreamReader(new FileInputStream(onePageTemp), "utf-8"));
//            String str = br.readLine();
//            while(str != null){
//                onePageTempStr += str;
//                str = br.readLine();
//            }
//            for(String s : outgoing_goods_one_page_arr){
//                onePageTempStr = onePageTempStr.replace("${" + s + "}", onePageData.get(s));
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if(br != null){
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        PDFUtil pdfUtil = new PDFUtil(this.getRequest(), "template");
//        Map<String,Object> data = new HashMap();
//        String table = "";
//        String tableStr = "";
////        String title = "<table class=\"order-list\"><thead style=\"display:table-header-group\"><tr><td colspan=\"5\"><table class=\"order-top\"><tr><th colspan=\"2\" align=\"center\">出库单</th></tr><tr><td width=\"60%\">出库单号：${order_num}</td><td>仓库：${warehouse_name}</td></tr><tr><td width=\"60%\">出库日期：${date}</td><td>门店：${store_name}</td></tr></table></td></tr></thead></table>";
////        for(String s : outgoing_goods_one_page_arr){
////            title = title.replace("${" + s + "}", onePageData.get(s));
////        }
//        String warehouse_out_order_sql = "SELECT " +
//                " so.order_number order_number, " +
//                " som.*, " +
//                " ( " +
//                "  SELECT " +
//                "   NAME " +
//                "  FROM " +
//                "   goods_attribute " +
//                "  WHERE " +
//                "   id = som.attribute_1 " +
//                " ) ganame " +
//                "FROM " +
//                " warehouse_out_order_material_detail som, " +
//                " warehouse_out_order so " +
//                "WHERE " +
//                " so.id = som.warehouse_out_order_id " +
//                "AND warehouse_out_order_id =? order by som.material_id";
//        List<Record> dataList = Db.find(warehouse_out_order_sql, orderId);
//        if(dataList != null && dataList.size() > 0){
//            dataRecord.set("order_number", dataList.get(0).get("order_number"));
//            int i = 1;
//            for(int j = 0; j < dataList.size(); j++,i++){
//                Record r = dataList.get(j);
//                String attr = UnitConversion.getAttrByOutUnit(r);
//                tableStr += "<tr><td>" + r.get("code") + "</td><td>" + r.get("name") + "</td><td>" + r.get("batch_code") + "</td><td>" + attr + "</td><td>" + r.get("out_unit") + "</td><td>" + r.get("send_num") + "</td></tr>";
//                if(i % 31 == 0){
//                    table += onePageTempStr.replace("${table}", tableStr);
//                    table += "<div class='pageNext'></div>";
//                    tableStr = "";
//                }
//            }
//            if(tableStr != null && tableStr.length() > 0){
//                table += onePageTempStr.replace("${table}", tableStr);
//            }
//        }
//        data.put("table", table);
//        data.put("creater_name", new UserSessionUtil(getRequest()).getRealName());
//        String content = pdfUtil.loadDataByTemplate(data, "outgoingGoodsTemplate.html");
//        try {
//            String name = UUIDTool.getUUID();
//            pdfUtil.createPdf(content, this.getRequest().getSession().getServletContext().getRealPath("") + "/pdf/" + name + ".pdf");
//            createPrintDetails(orderId, dataRecord, "outgoing_goods");
////            this.getResponse().sendRedirect(getRequest().getContextPath()  + "/pdf/" + name + ".pdf");
////            this.getRequest().getRequestDispatcher(getRequest().getContextPath()  + "/pdf/a.pdf");
//            Record result = new Record();
//            result.set("pageUrl", getRequest().getScheme()+"://"+getRequest().getServerName()+":"+getRequest() .getServerPort()  + "/pdf/" + name + ".pdf");
//            result.set("title", "出库单");
//            jhm.put("data", result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            jhm.putMessage(e.getMessage()).putCode(-1);
//        }
//        renderJson(jhm);
//    }
//
//    public void getPrintDetail(){
//        String pageNumStr=getPara("pageNum");
//        String pageSizeStr=getPara("pageSize");
//
//        int pageNum= NumberUtils.parseInt(pageNumStr,1);
//        int pageSize=NumberUtils.parseInt(pageSizeStr,10);
//        String orderId = getPara("orderNumber");
//        String type = getPara("type");
////        String date = getPara("date");
//        String sql = " from print_details where 1=1 ";
//        List<Object> params = new ArrayList<>();
//        if(orderId != null && orderId.length() > 0){
//            sql += " and order_number=? ";
//            params.add(orderId);
//        }
//        if(type != null && type.length() > 0){
//            sql += " and type=? ";
//            params.add(type);
//        }
////        if(date != null && date.length() > 0){
////            sql += " and print_date=? ";
////            params.add(date);
////        }
//        sql += " order by sort desc";
//        Page<Record> orderList = Db.paginate(pageNum, pageSize, "select * ", sql, params.toArray());
//        int printTime = 0;
//        if(orderList != null && orderList.getList().size() > 0){
//            printTime = orderList.getList().get(0).getInt("sort");
//        }
//        JsonHashMap jhm = new JsonHashMap();
//        jhm.put("data", orderList).put("printTime", printTime);
//        renderJson(jhm);
//    }



}
