package com.wechatmall.pc.finance;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import easy.util.NumberUtils;
import org.apache.commons.lang.StringUtils;
import utils.NumberFormat;
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
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/finance/listBill
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
        try{
            /*
             * 查询
             * customer表     customerName客户姓名，customerType客户类型
             * customer_address表    receiverName收货人姓名，phone收货人联系电话，address收货人联系地址
             * orderform表      id订单编号，createTime订单创建时间，shouldPay应付金额，havePaid已付金额，payStatus订单状态，payType付类型， transportModel订单物流类型
             * dictionary表     payStatus_text订单状态的中文名，payType_text支付状态的中文名，transportModel_text订单物流类型的中文名
             * 根据字段模糊查询或完全匹配查询
             * */
            String select = "select wor.oid as id,wor.onum as orderNum, wor.ocreate_time as createTime, wor.ocurrent_sum as shouldPay,  wor.ostatus as payStatus, wor.opay_type as payType, wor.otransport_type as transportModel, wcu.cname as customerName, wcu.ctype as customerType, wca.caname as receiverName, wca.caphone as phone, wca.caaddress address, ( select name from w_dictionary w where wor.opay_type = w.`value` and w.parent_id = 800 ) payType_text, ( select name from w_dictionary w where wor.ostatus = w. value and w.parent_id = 100 ) payStatus_text, ( select name from w_dictionary w where wor.otransport_type = w.`value` and w.parent_id = 700 ) transportModel_text   ";
            String sql = "  from w_customer wcu, w_customer_address wca, w_orderform wor where wor.cid = wcu.cid and wor.caid = wca.caid   ";
            String sql1 = " select SUM(wor.ocurrent_sum) as weChatHavePaid  from w_customer wcu, w_customer_address wca, w_orderform wor where wor.cid = wcu.cid and wor.caid = wca.caid and (wor.opay_type = 'wechat_pay') and wor.ostatus in ( 'finished', 'paid', 'shipped' )";
            String sql2 = " select SUM(wor.ocurrent_sum) as cashOnHavePaid from w_customer wcu, w_customer_address wca, w_orderform wor where wor.cid = wcu.cid and wor.caid = wca.caid and (wor.opay_type = 'cashOn_delivery') and wor.ostatus in ( 'finished', 'paid' )  ";
            String sql3 = " select SUM(wor.ocurrent_sum) as shouldPay from w_customer wcu, w_customer_address wca, w_orderform wor where wor.cid = wcu.cid and wor.caid = wca.caid ";
            if(customerName != null && customerName.length() > 0){
                customerName = "%" + customerName + "%";
                sql += "  and wcu.cname like ? ";
                sql1 += "  and wcu.cname like ? ";
                sql2 += "  and wcu.cname like ? ";
                sql3 += "  and wcu.cname like ? ";
                params.add(customerName);
            }
            if(customerType != null && customerType.length() > 0){
                sql += "  and wcu.ctype = ? ";
                sql1 +=  "  and wcu.ctype = ? ";
                sql2 += "  and wcu.ctype = ? ";
                sql3 += "  and wcu.ctype = ? ";
                params.add(customerType);
            }
            if(startDate != null && startDate.length() > 0 ){
                startDate = startDate + " 00:00:00";
                    sql += "   and wor.ocreate_time >= ? ";
                    sql1 += "   and wor.ocreate_time >= ? ";
                    sql2 += "   and wor.ocreate_time >= ? ";
                    sql3 += "   and wor.ocreate_time >= ? ";
                    params.add(startDate);
            }
            if(endDate != null && endDate.length() > 0 ){
                endDate = endDate + " 23:59:59";
                    sql += "  and wor.ocreate_time <=  ? ";
                    sql1 += "  and wor.ocreate_time <=  ? ";
                    sql2 += "  and wor.ocreate_time <=  ? ";
                    sql3 += "  and wor.ocreate_time <=  ? ";
                    params.add(endDate);

            }
            if(payType != null && payType.length() > 0){
                sql += "  and wor.opay_type = ?  ";
                sql1 += "  and wor.opay_type = ?  ";
                sql2 += "  and wor.opay_type = ?  ";
                sql3 += "  and wor.opay_type = ?  ";
                params.add(payType);
            }
            //按时间倒序排列
            sql += " ORDER BY wor.omodify_time  desc ";
            sql1 += " ORDER BY wor.omodify_time  desc ";
            sql2 += " ORDER BY wor.omodify_time  desc ";
            sql3 += " ORDER BY wor.omodify_time  desc";
            //分页查询
            Page<Record> page = Db.paginate(pageNum, pageSize,select, sql,params.toArray());
            //定义个人应付款、已付款、未付款和总体应付款、已付款和未付款
            float shouldPayInvid = 0.00f;
            float shouldPay = 0.00f;
            float notPayInvid = 0.00f;
            float havePaid = 0.00f;
            float noPay = 0.00f;
            //遍历page，notPay = shouldPay - havePaid;
            for(Record r : page.getList()){
                 shouldPayInvid = r.getFloat("shouldPay");
                 //保留两位小数，把未付款放入page集合中
                String shouldPayStr = NumberFormat.doubleFormatStr(shouldPayInvid);
                //把个人的应付款，已付款，未付款放在page中
                r.set("price",shouldPayStr);
                //r.set("havePaid",havePaidStr);
                //r.set("notPay",notPayStr);
                //求出所有记录的应付款
            }
            //根据支付方式为微信支付计算已付款的金额
            Record weChatHavePaid = Db.findFirst(sql1,params.toArray());
            //根据货到付款方式计算已付款的金额
            Record cashOnHavePaid = Db.findFirst(sql2,params.toArray());
            //查询所有应付款的金额
            Record shouldPayRecord = Db.findFirst(sql3,params.toArray());
            //把两种方式付款的应付金额相加
                havePaid = weChatHavePaid.getFloat("weChatHavePaid") + cashOnHavePaid.getFloat("cashOnHavePaid");

            //应付款
                shouldPay = shouldPayRecord.getFloat("shouldPay");
            //计算未付款金额
                noPay = shouldPay - havePaid;
                    jhm.put("shouldPay",shouldPay);
                    jhm.put("havePaid",havePaid);
                    jhm.put("notPay",noPay);
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
