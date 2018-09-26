package com.wechatmall.pc.order;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import easy.util.NumberUtils;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class OrderCtrl extends BaseCtrl{

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询订单列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/listOrder
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  number  string                  不允许   查询添加，按照订单编号完全匹配查询
     * 	type	string		            不允许	 查询添加，按照客户类型完全匹配查询
     *  customerName    string          不允许   查询添加，按照客户姓名完全匹配查询
     *  status  string                  不允许   查询添加，按照订单状态完全匹配查询
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code": 1,
        "list":[{
        "orderId":"11da",
        "time":"2018/9/9",
        "customerName":"客户姓名",
        "receiverName":"收货人姓名",
        "phone":1313005589,
        "type":0,
        "address":"收货地址",
        "originalPrice":20.0,
        "presentPrice":18.0,
        "status":0
        },{
        "orderId":"11da",
        "time":"2018/9/9",
        "customerName":"客户姓名",
        "receiverName":"收货人姓名",
        "phone":1313005589,
        "type":0,
        "address":"收货地址",
        "originalPrice":20.0,
        "presentPrice":18.0,
        "status":0
        }]
    }
     * 失败：
     * {
    "code":"0",
    "message":"查询失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void listOrder(){
        JsonHashMap jhm = new JsonHashMap();

        String number = getPara("number");
        String type = getPara("type");
        String status = getPara("status");
        String customerName = getPara("customerName");
        String pageNumStr = getPara("pageNum");
        String pageSizeStr = getPara("pageSize");

        //为空时赋予默认值
        int pageNum = NumberUtils.parseInt(pageNumStr, 1);
        int pageSize = NumberUtils.parseInt(pageSizeStr, 10);

        /**
         * 根据订单表w_orderform和客户表w_customer关联查询：客户id：orderId, 客户姓名:customerName, 创建时间：time, 客户类型:type
         * 收货人姓名:receiverName, 收货人电话:phone, 收货地址:address, 应付价格:originalPrice, 实付价格:presentPrice, 订单状态:status
         */
        String select = "SELECT oid as orderId, ocreate_time as time, oname as receiverName, ophone as phone, wc.cname as customerName, wc.ctype as type, wo.oaddress as address, wo.ooriginal_sum as originalPrice, wo.ocurrent_sum as presentPrice, wo.ostatus as status";

        StringBuilder sql = new StringBuilder(" from w_orderform wo, w_customer wc WHERE wo.cid = wc.cid");

        List<Object> params = new ArrayList<>();

        //对检索条件进行判断
        if(!StringUtils.isEmpty(number)){
            sql.append(" and wo.oid = ? ");
            params.add(number);
        }

        if(!StringUtils.isEmpty(type)){
            sql.append(" and wc.type = ? ");
            params.add(type);
        }

        if(!StringUtils.isEmpty(status)){
            sql.append(" and wo.status = ? ");
            params.add(status);
        }

        if(!StringUtils.isEmpty(customerName)){
            sql.append(" and wc.oanme = ? ");
            params.add(customerName);
        }

        try {
            Page<Record> page = Db.paginate(pageNum, pageSize, select, sql.toString(), params.toArray());
            jhm.put("data", page);
            jhm.putCode(1);
        } catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常");
        }
        renderJson(jhm);
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查看订单详情
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/listOrderDetail
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  orderId  string                  不允许   订单id/编号
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code": 1,
        "customerType":0,
        "orderId":"A5454845648",
        "transportType":"物流类型",
        "payType":"支付方式",
        "products":[{
        "name":"大米",
        "originalPrice":20.0,
        "presentPrice":18.0,
        "number":1,
        "singleSum":20.0
        },{
        "name":"大米",
        "originalPrice":20.0,
        "presentPrice":18.0,
        "number":1,
        "singleSum":20.0
        }],
        "orderOriginalSum":400,
        "orderPresentSum":380
    }
     * 失败：
     * {
    "code":"0",
    "message":"查询失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void listOrderDetail(){
        JsonHashMap jhm = new JsonHashMap();

        String orderId = getPara("orderId");

        if(StringUtils.isEmpty(orderId)){
            jhm.putCode(0).putMessage("订单号不能为空！");
            renderJson(jhm);
            return;
        }

        try {
            /**
             * 根据w_orderform表查询是否存在该订单
             */
            String sql  = "SELECT count(*) as c from w_orderform where oid = ? ";
            Record r = Db.findFirst(sql, orderId);
            if(!StringUtils.equals("0", r.getStr("c"))){
                /**
                 * 根据订单表w_orderform和客户信息表w_customer双表关联查询: 订单id : orderId , 客户类型 : customerType , 物流类型 : transportType
                 * 支付类型 : payType , 订单原总价 : orderOriginalSum , 订单应付金额 : orderPresentSum
                 */
                String orderSearch  = "select wo.oid as orderId, wc.ctype as customerType, wo.otransport_type transportType, wo.opay_type as payType, wo.ooriginal_sum as orderOriginalSum, wo.ocurrent_sum as orderPresentSum from w_orderform wo, w_customer wc where wo.cid = wc.cid and wo.oid = ? ";
                Record record = Db.findFirst(orderSearch, orderId);

                /**
                 * 根据订单表w_orderform和订单详情表w_orderform_detail双表关联查询: 订单商品名称 : name , 订单商品原价 : originalPrice
                 * 订单商品现价 : presentPrice , 订单商品数量 : number , 订单商品小结 : singleSum
                 */
                String orderDetailSearch = "select wod.odname as name, wod.odoriginal_price as originalPrice, wod.odcurrent_price as presentPrice, wod.odquantity as number, (wod.odoriginal_price * wod.odquantity)as singleSum from w_orderform wo, w_orderform_detail wod, w_customer wc where wo.oid = ? and wod.oid = wo.oid ";
                List<Record> recordList = Db.find(orderDetailSearch, orderId);
                jhm.put("data", record);
                jhm.put("products", recordList);
                jhm.putCode(1);
            } else {
                jhm.putCode(0).putMessage("该订单不存在！");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"customerType\":0,\"orderId\":\"A5454845648\",\"transportType\":\"物流类型\",\"payType\":\"支付方式\",\"products\":[{\"name\":\"大米\",\"originalPrice\":20,\"presentPrice\":18,\"number\":1,\"singleSum\":20},{\"name\":\"大米\",\"originalPrice\":20,\"presentPrice\":18,\"number\":1,\"singleSum\":20}],\"orderOriginalSum\":400,\"orderPresentSum\":380}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	打印订单详情
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/printOrders
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  orderIdList  array                  不允许   订单id/编号
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code": 1,
    "list": [{
    "orderId": "A6464546548945",
    "customerName": "小明",
    "customerPhone": 13130005589,
    "address": "用户地址",
    "createTime": "2018-09-16 23:00:00",
    "transportType":"物流类型",
    "payType":"支付方式",
    "orderOriginalSum": 20.00,
    "orderPresentSum": 19.00,
    "productsList": [{
    "productId": "51564157854",
    "productName": "大米",
    "productNum": 1,
    "productPrice": 18.00
    }]
    },{
    "orderId": "A6464546548945",
    "customerName": "小明",
    "customerPhone": 13130005589,
    "address": "用户地址",
    "createTime": "2018-09-16 23:00:00",
    "transportType":"物流类型",
    "payType":"支付方式",
    "orderOriginalSum": 20.00,
    "orderPresentSum": 19.00,
    "productsList": [{
    "productId": "15615648748794",
    "productName": "大米",
    "productNum": 1,
    "productPrice": 18.00
    }]
    }]
    }
     * 失败：
     * {
    "code":"0",
    "message":"查询失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void printOrders(){
        JsonHashMap jhm = new JsonHashMap();

        String [] orderIdList = getParaValues("orderIdList");

        //进行非空验证
        if(orderIdList.length == 0 && orderIdList == null){
            jhm.putCode(0).putMessage("尚未选择订单！");
            renderJson(jhm);
            return;
        }

        StringBuilder order = new StringBuilder(orderIdList[0]);
        for(int t = 1; t < orderIdList.length; t++){
            order.append(","+ orderIdList[t]);
        }

        //检测数据库中是否存在的sql语句
        String sql = "SELECT count(*) as c from w_orderform where oid IN("+ order.toString() +") ";

        //订单id
        String orderId = "";

        List<Record> result = new ArrayList<>();

        /**
         *根据订单表w_orderform和客户信息表w_customer查询 : 订单id : orderId , 客户姓名 : customerName , 客户电话 : customerPhone
         * 收货地址 : address , 订单创建时间 : crateTime , 物流类型 : transportType , 支付方式 : payType
         * 订单原总价 : orderOriginalSum , 订单总现价 : orderPresentSum
         */
        String orderSearch = "SELECT wo.oid as orderId, wc.cname as customerName, wc.cphone as customerPhone, wo.oaddress as address, wo.ocreate_time as crateTime, wo.otransport_type as transportType, wo.opay_type as payType, wo.ooriginal_sum as orderOriginalSum, wo.ocurrent_sum as orderPresentSum FROM w_orderform wo, w_customer wc where wo.oid IN("+ order.toString() +") and wc.cid = wo.cid";

        /**
         *根据订单表w_orderform和订单详情表w_orderfrom_detail查询 : 商品名称 : name , 商品所属订单号 ： oid
         * 订单商品原价 : originalPrice , 订单商品现价 : presentPrice , 订单商品数量 : number
         */
        String goodsSearch = "select wod.odname as name,wo.oid as orderId, wod.pid as productId, wod.odcurrent_price as presentPrice, wod.odquantity as number from w_orderform wo, w_orderform_detail wod, w_customer wc where wo.oid IN ("+ order.toString() +") and wod.oid = wo.oid ";

        try {
            Record r = Db.findFirst(sql);
            if(StringUtils.equals(r.getStr("c"),String.valueOf(orderIdList.length))){
                List<Record> recordList = Db.find(orderSearch);
                List<Record> records = Db.find(goodsSearch);

                //查出来之后对数据进行分类
                for(int i = 0; i < orderIdList.length; i++){
                    orderId = orderIdList[i];
                    for(int j = 0; j < records.size() ; j++){
                        if(StringUtils.equals(records.get(j).getStr("orderId"), orderId)){
                            result.add(records.get(j));
                        }
                    }
                    recordList.get(i).set("productsList", result);
                    result = new ArrayList<>();
                }
                jhm.putCode(1).put("list", recordList);
            } else {
                jhm.putCode(0).putMessage("订单记录缺失！");
            }
        } catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器出错！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"list\":[{\"orderId\":\"A6464546548945\",\"customerName\":\"小明\",\"customerPhone\":13130005589,\"address\":\"用户地址\",\"createTime\":\"2018-09-16 23:00:00\",\"transportType\":\"物流类型\",\"payType\":\"支付方式\",\"orderOriginalSum\":20,\"orderPresentSum\":19,\"productsList\":[{\"productId\":\"51564157854\",\"productName\":\"大米\",\"productNum\":1,\"productPrice\":18}]},{\"orderId\":\"A6464546548945\",\"customerName\":\"小明\",\"customerPhone\":13130005589,\"address\":\"用户地址\",\"createTime\":\"2018-09-16 23:00:00\",\"transportType\":\"物流类型\",\"payType\":\"支付方式\",\"orderOriginalSum\":20,\"orderPresentSum\":19,\"productsList\":[{\"productId\":\"15615648748794\",\"productName\":\"大米\",\"productNum\":1,\"productPrice\":18}]}]}");
    }

}
