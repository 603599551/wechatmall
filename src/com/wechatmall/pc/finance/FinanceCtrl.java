package com.wechatmall.pc.finance;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import easy.util.NumberUtils;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * FinanceCtrl class
 * @author liushiwen
 * @date   2018-9-27
 */
public class FinanceCtrl extends BaseCtrl{
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查看账单列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/finance/listBill
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	           类型	       最大长度	允许空	 描述
     *  customerName      string                不允许  查询添加，根据客户姓名模糊查询
     *  customerType      string                不允许  查询添加，根据客户类型完全匹配查询
     *  startDate         string                不允许  查询添加，根据开始日期完全匹配查询
     *  endDate           string                不允许  查询添加，根据结束日期完全匹配查询
     *  payType           string                不允许  查询添加，根据支付类型完全匹配查询
     *  pageSize          string                不允许  每页限制的行数
     *  pageNumber        string                不允许  页码
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code": 1,
    "data": {
    "totalRow": 1,
    "pageNumber": 1,
    "firstPage": true,
    "lastPage": true,
    "totalPage": 1,
    "pageSize": 10,
    "list": [{
    "id": "自提点id",
    "storeName": "自提点名称",
    "managerName": "管理员姓名",
    "cityName": "城市名称",
    "address": "地址",
    "workTime": "工作时间",
    "managerPhone": "联系电话",
    "status": "启用状态"
    }]
    }
    }
     * 失败：
     * {
    "code":"0",
    "message":"显示失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */

    public void listBill(){
        JsonHashMap jhm = new JsonHashMap();
        Map map = getParaMap();
        /**
         * 接收前台参数
         **/
        //客户姓名
        String customerName = getPara("customerName");
        //客户类型
        String customerType = getPara("customerType");
        //开始日期
        String startDate = getPara("startDate");
        //结束日期
        String endDate = getPara("endDate");
        //支付类型
        String payType = getPara("payType");
        //每页限制的行数
        String pageSizeStr = getPara("pageSize");
        //页码
        String pageNumStr = getPara("pageNumber");

        int pageNum,pageSize;
        //非空验证
        if(StringUtils.isEmpty(pageNumStr)){
            pageNum = NumberUtils.parseInt(pageNumStr,1);
        }else {
            pageNum = Integer.parseInt(pageNumStr);
        }
        if(StringUtils.isEmpty(pageSizeStr)){
            pageSize = NumberUtils.parseInt(pageSizeStr,10);
        }else {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        //新建集合，放入替换参数
        List<Object> params = new ArrayList<>();
        //查询订单id，订单创建时间，客户姓名，客户类型，收货人姓名，联系电话，联系地址，实付金额，支付方式，支付状态，应付金额，实付金额
        String select = "select wor.oid as id, wor.ocreate_time as createTime, wcu.cname  as customerName, wcu.ctype as customerType, " +
                " wca.caname as receiverName, wca.caphone as phone, wca.caaddress as address, wor.opay_type as payType, wor.otransport_type as payStatus, " +
                " ( select name from w_dictionary wd where wcu.ctype = wd.`value` and wd.parent_id = 500 ) as customerType_text, " +
                "  ( select name from w_dictionary wd where wor.opay_type = wd.`value` and wd.parent_id = 800 ) as payType_text, " +
                "  ( select name from w_dictionary wd where wor.otransport_type = wd.`value` and wd.parent_id = 700 ) as payStatus_text, "+
                " SUM(wor.ocurrent_sum) as shouldPay, SUM(wod.odcurrent_price) as havePaid  ";
        String sql = "  from w_customer wcu, w_orderform wor, w_customer_address wca, w_orderform_detail wod where wor.caid = wca.caid and wor.cid = wcu.cid and wor.oid = wod.oid   ";
        try{
            if(customerName != null && customerName.length() > 0){
                customerName = "%" + customerName + "%";
                sql += "  and wcu.cname like ? ";
                params.add(customerName);
            }
            if(customerType != null && customerType.length() > 0 || customerType !=""){
                sql += "  and wcu.ctype = ? ";
                params.add(customerType);
            }
            if(startDate != null && startDate.length() > 0 ){
                sql += "   and wor.ocreate_time >= ? ";
                params.add(startDate);
            }
            if(endDate != null && endDate.length() > 0 ){
                sql += "  and wor.ocreate_time <=  ? ";
                params.add(endDate);
            }
            if(payType != null && payType.length() > 0){
                sql += "  and wor.opay_type = ?  ";
                params.add(payType);
            }
            sql += " ORDER BY wor.omodify_time  desc ";
            /**
             * 查询自提点列表
             */
            Page<Record> page = Db.paginate(pageNum, pageSize,select, sql,params.toArray());
            double notPay = 0.0;
            float shouldPay = 0.0f;
            float havePaid = 0.0f;
                for(Record r : page.getList()){
                     shouldPay = r.getFloat("shouldPay");
                     havePaid = r.getFloat("havePaid");
                     notPay = shouldPay - havePaid;
                     r.set("notPay",notPay);
                }
                    jhm.put("list",page);
                    jhm.putMessage("查询成功！");

        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record发生异常!");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"data\":{\"totalRow\":\"1\",\"pageNumber\":\"1\",\"firstPage\":\"true\",\"lastPage\":\"true\",\"totalPage\":\"1\",\"shouldPay\":\"2000\",\"havePaid\":\"800\",\"notPay\":\"1200\",\"list\":[{\"id\":\"订单编号\",\"createTime\":\"创建时间\",\"customerName\":\"客户姓名\",\"customerType\":\"客户类型\",\"receiverName\":\"收货人姓名\",\"phone\":\"联系电话\",\"address\":\"收货地址\",\"realPay\":\"实付金额\",\"payType\":\"支付方式\",\"payStatus\":\"支付状态\"},{\"id\":\"订单编号\",\"createTime\":\"创建时间\",\"customerName\":\"客户姓名\",\"customerType\":\"客户类型\",\"receiverName\":\"收货人姓名\",\"phone\":\"联系电话\",\"address\":\"收货地址\",\"realPay\":\"实付金额\",\"payType\":\"支付方式\",\"payStatus\":\"支付状态\"}]}}");
    }
}
