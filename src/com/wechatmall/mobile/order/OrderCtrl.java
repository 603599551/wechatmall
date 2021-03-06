package com.wechatmall.mobile.order;

import com.common.controllers.BaseCtrl;
import com.jfinal.Config;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.wechatmall.pc.customer.CustomerGroupService;
import easy.util.DateTool;
import easy.util.UUIDTool;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * customerCtrl class
 * @author liushiwen
 * @date   2018-9-22
 */

public class OrderCtrl extends BaseCtrl {
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	提交订单-查询
     * 描述     显示订单的内容（自提点地址，联系人信息，订单详情）
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/mobile/order/querySubmitOrder
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	string		            不允许	 用户的id
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * 个人：
     * {
        "code": 1,
        "address":[{
        "address":"最近的地址",
        "miles":75
        },{
        "address":"地址二",
        "miles":75
        }],
        "contacts":[{
        "name":"小明",
        "phone":13130005589,
        "isDefult":1
        },{
        "name":"小明",
        "phone":13130005589,
        "isDefult":0
        }]
        }
        或者
    商家：
        {
        “code”: 1,
        “address”: [{
        “address”: “最近的地址”,
        “name”: “小明”,
        “phone”: 1313005558
        }, {
        “address”: “最近的地址”,
        “name”: “小明”,
        “phone”: 1313005558
        }]
        }
     * 失败：
     * {
    "code": 0,
    "message": "查询失败！"
    }

     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */

    public void querySubmitOrder(){
        JsonHashMap jhm=new JsonHashMap();
        //接收前端参数 userId
        String userId=getPara("userId");
        //非空验证
        if (StringUtils.isEmpty(userId)){
            jhm.putCode(0).putMessage("客户id为空");
            renderJson(jhm);
            return;
        }

        String transportType="transport_type";
        String payType="pay_type";
        //查询store表得到所有的自提点地址address， 还没有查距离
        String sql1="SELECT saddress AS address FROM w_store WHERE sstatus='start_using'";
        //根据userId查询customer_address表得到多个 收货人姓名name，联系电话phone，收货地址province+city+district+address,默认状态isDefault
        String sql2="SELECT caname AS name,caphone AS phone,CONCAT(caprovince,cacity,cadistrict,caaddress) AS address,castatus AS isDefault FROM w_customer_address WHERE cid=?";
        //根据value值查询dictionary表得到 物流类型和支付类型
        String sql3="SELECT value,name,`desc` FROM w_dictionary d WHERE parent_id = (SELECT id FROM w_dictionary WHERE value =?) AND d.`desc` LIKE CONCAT('%',?,'%')";

        String sql4="SELECT name FROM w_dictionary WHERE value=(SELECT ctype FROM w_customer WHERE cid=?)";

        try{
            //自提点列表
            List<Record> storeList=Db.find(sql1);
            if (storeList !=null){
                for (Record store:storeList){
                    store.set("miles","75");
                }
            }
            //收货地址列表
            List<Record> contactList=Db.find(sql2,userId);
            Record r=Db.findFirst(sql4,userId);
            if (r==null){
                jhm.putCode(0).putMessage("没有该客户信息");
                renderJson(jhm);
                return;
            }
            String customerType=r.getStr("name");
            //物流类型列表
            List<Record> transportList=Db.find(sql3,transportType,customerType);

            //支付类型列表
            List<Record> payList=Db.find(sql3,payType,customerType);

            jhm.putCode(1);
            jhm.put("selfAddressedAddress",storeList);
            jhm.put("contacts",contactList);
            jhm.put("receivingMethod",transportList);
            jhm.put("payMethod",payList);

        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常");
        }
        renderJson(jhm);
//        renderJson("{\"code\":1,\"selfAddressedAddress\":[{\"address\":\"最近的地址\",\"miles\":75},{\"address\":\"地址二\",\"miles\":75}],\"contacts\":[{\"name\":\"小明\",\"phone\":13130005589,\"address\":\"地址\",\"isDefult\":1},{\"name\":\"小明\",\"phone\":13130005589,\"address\":\"地址\",\"isDefult\":0}],\"receivingMethod\":[\"自提\",\"快递\"],\"payMethod\":[\"微信\",\"账期\"]}");
    }

    public static String getOrderIdByTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate=sdf.format(new Date());
        String result="";
        Random random=new Random();
        for(int i=0;i<3;i++){
            result+=random.nextInt(10);
        }
        return newDate+result;
    }


    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	提交订单
     * 描述     将订单的内容存入数据库
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/mobile/order/placeOrder
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	string		            不允许	 用户的id
     * address   string                 不允许   自提地址/收货地址
     * name      string                 不允许   用户姓名
     * phone     string                   不允许   用户手机号
     * goodsList   array                不允许   订单商品列表
     * receivingMethod    string           不允许   收货方式
     * payMethod          string           不允许   支付方式
     * orderOriginalSum   string           不允许   订单原总价
     * 	orderCurrentSum   string           不允许   订单现总价
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code": 1,
    "message": "提交成功！"
    }

     * 失败：
     * {
    "code": 0,
    "message": "提交失败！"
    }

     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void placeOrder(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前端参数
         */
        //用户的id
        String userId = getPara("userId");
        //自提地址/收货地址
        String address = getPara("address");
        //用户姓名
//        String name = getPara("name");
        //用户手机号
//        String phone = getPara("phone");
        String contact=getPara("contact");
        //订单商品列表
        String goodsString = getPara("goodsList");
        //JSONArray goodsStringList = JSONArray.fromObject(goodsString);
        //收货方式
        String receivingMethod = getPara("receive");
        //支付方式
        String payMethod = getPara("payMethod");
        //订单原总价
        String orderOriginalSumStr = getPara("orderOriginalSum");
        //订单现总价
        String orderCurrentSumStr = getPara("orderCurrentSum");

        //非空验证
        if (StringUtils.isEmpty(userId)){
            jhm.putCode(0).putMessage("用户的id为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(address)){
            jhm.putCode(0).putMessage("自提地址/收货地址为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(contact)){
            jhm.putCode(0).putMessage("联系方式不能为空！");
            renderJson(jhm);
            return;
        }
        String name=contact.substring(contact.indexOf(":")+1,contact.indexOf("　")).trim();
        String phone=contact.substring(contact.lastIndexOf(":")+1).trim();
        if (StringUtils.isEmpty(name)){
            jhm.putCode(0).putMessage("用户姓名为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(phone)){
            jhm.putCode(0).putMessage("用户手机号为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(goodsString)){
            jhm.putCode(0).putMessage("订单商品列表为空!");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(receivingMethod)){
            jhm.putCode(0).putMessage("收货方式为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(payMethod)){
            jhm.putCode(0).putMessage("支付方式为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(orderOriginalSumStr)){
            jhm.putCode(0).putMessage("订单原总价为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(orderCurrentSumStr)){
            jhm.putCode(0).putMessage("订单现总价为空！");
            renderJson(jhm);
            return;
        }

        //订单编号=时间+随机数
        String orderNum=getOrderIdByTime();

        try{
            Map paraMap=new HashMap();
            paraMap.put("userId", userId);
            paraMap.put("address", address);
            paraMap.put("name", name);
            paraMap.put("phone", phone);
            paraMap.put("goodsString", goodsString);
            paraMap.put("receivingMethod", receivingMethod);
            paraMap.put("payMethod", payMethod);
            paraMap.put("orderOriginalSumStr", orderOriginalSumStr);
            paraMap.put("orderCurrentSumStr", orderCurrentSumStr);
            paraMap.put("orderNum", orderNum);
            OrderService srv = enhance(OrderService.class);
            jhm = srv.placeOrder(paraMap);

        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
//        renderJson("{\"code\":1,\"message\":\"提交成功！\"}");
    }


    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	订单查询
     * 描述     默认显示所有订单
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/mobile/order/queryOrder
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	string		            不允许	 用户的id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code": 1,
        "order":[{
        "status":0,
        "goodsList":[{
        "name":"产品1",
        "number":1
        },{
        "name":"产品2",
        "number":1
        }],
        "originalPrice":20.0,
        "presentPrice":18.0,
        "goodsAddress":"地址",
        "consigneeName":"小明",
        "consigneePhone":13130005589,
        "storePhone":"自提点联系电话（客户类型为消费者时需要有此字段）"
        },{
        "status":1,
        "goodsList":[{
        "name":"产品1",
        "number":1
        },{
        "name":"产品2",
        "number":1
        }],
        "originalPrice":20.0,
        "presentPrice":18.0,
        "goodsAddress":"地址",
        "consigneeName":"小明",
        "consigneePhone":13130005589
        }]
    }

     * 失败：
     * {
    "code": 0,
    "message": "查询失败！"
    }

     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void queryOrder(){
        JsonHashMap jhm=new JsonHashMap();
        //接收前端参数：客户id
        String userId=getPara("userId");
        String type=getPara("type");

        //非空验证
        if (StringUtils.isEmpty(userId)){
            jhm.putCode(0).putMessage("userId为空");
            renderJson(jhm);
            return;
        }

        try{
            /**
             * 根据userId查询orderform表的cid得到多个订单编号oid，订单状态status，物流类型otransport_type，支付方式opay_type，
             * 订单原价ooriginal_sum，订单现价ocurrent_sum，订单地址oaddress，收货人姓名oname，联系电话ophone，
             */
            StringBuilder sql1Sb=new StringBuilder("SELECT o.oid AS orderId,o.onum AS orderNum,o.ostatus AS status,case o.ostatus when 'pending_pay' then '待付款' when 'paid' then '已付款' when 'shipped' then '已发货' when 'finished' then '已完成' when 'canceled' then '已取消'  end as status_text,o.otransport_type AS receivingMethod,o.opay_type AS payMethod,\n" +
                    "FORMAT(o.ooriginal_sum,2)AS originalPriceAll,FORMAT(o.ocurrent_sum,2)AS presentPriceAll,o.oaddress AS goodsAddress,\n" +
                    "o.oname AS consigneeName,o.ophone AS consigneePhone FROM w_customer c,w_orderform o WHERE  c.cid=o.cid and c.cid=? ");
            List sql1ParaList=new ArrayList();
            sql1ParaList.add(userId);
            if(type==null || "".equals(type) || "1".equals(type)){//查询全部订单

            }else if("2".equals(type)){//查询待付款
                sql1Sb.append(" and o.ostatus=? ");
                sql1ParaList.add("pending_pay");
            }else if("3".equals(type)){//查询待发货
                sql1Sb.append(" and o.ostatus =? ");
                sql1ParaList.add("paid");
            }
            sql1Sb.append(" ORDER BY o.ocreate_time DESC limit 30");
            //根据oid查询orderform_detail表的oid得到多个 商品名称odname、商品数量odquantity、商品原价odoriginal_price、商品现价odcurrent_price
            StringBuilder sql2=new StringBuilder("SELECT oid AS orderId,GROUP_CONCAT(odname) AS productsN,GROUP_CONCAT(odquantity) AS productsQ,\n" +
                    "GROUP_CONCAT(FORMAT(odoriginal_price,2)) AS productsOP,GROUP_CONCAT(FORMAT(odcurrent_price,2))AS productsCP,GROUP_CONCAT(pid) AS goodsId \n" +
                    "FROM w_orderform_detail GROUP BY oid HAVING oid IN (");

            //订单列表orderList
            List<Record> orderList= Db.find(sql1Sb.toString(),sql1ParaList.toArray());
            List<String> orderIdList=new ArrayList<>();
            if (orderList!=null && orderList.size()>0) {
                //遍历订单列表
                for (Record order : orderList) {
                    orderIdList.add(order.get("orderId"));
                    sql2.append("?,");
                }

                String sql2Str=sql2.substring(0,sql2.length()-1)+")";



                //将订单地址与自提点表的地址比较，得到storePhone
                String sql3="SELECT sphone AS storePhone FROM w_store WHERE saddress=?";

                //根据userId查询
                //订单详情列表
                List<Record> orderDetailList= Db.find(sql2Str,orderIdList.toArray());


                //遍历订单列表
                for (Record order:orderList){
                    Record phone=Db.findFirst(sql3,order.getStr("goodsAddress"));
                    if (phone==null){
                        order.set("storePhone","");
                    }else {
                        order.set("storePhone",phone.getStr("storePhone"));
                    }
                    //遍历订单详情商品列表
                    for (Record orderDetail:orderDetailList){
                        if (StringUtils.equals(order.getStr("orderId"),orderDetail.getStr("orderId"))){
                            String []productsId=orderDetail.getStr("goodsId").split(",");
                            String []productsName=orderDetail.getStr("productsN").split(",");
                            String []productsQuantity=orderDetail.getStr("productsQ").split(",");
                            String productsOP=orderDetail.getStr("productsOP");
                            String []productsOriginalPrice=null;
                            if(productsOP!=null){
                                productsOriginalPrice=productsOP.split(",");
                            }
                            String []productsCurrentPrice=orderDetail.getStr("productsCP").split(",");
                            //订单详情商品列表
                            List<Record> goodsList=new ArrayList<>();
                            //订单详情商品数
                            int goodsListLen=productsName.length;
                            //将四个GROUP_CONCAT的数据分开后，对应起来，再放到goodsList中
                            for (int i=0;i<goodsListLen;i++){
                                Record product=new Record();
                                product.set("goodsId",productsId[i]);
                                product.set("name",productsName[i]);
                                product.set("number",productsQuantity[i]);
                                product.set("originalPrice",productsOriginalPrice[i]);
                                product.set("presentPrice",productsCurrentPrice[i]);
                                goodsList.add(product);
                            }
                            order.set("goodsList",goodsList);
                            break;
                        }
                    }
                }
            }
            jhm.putCode(1);
            jhm.put("orderList",orderList);
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器出现异常！");
        }
        renderJson(jhm);


//        renderJson("{\"code\":1,\"type\":\"客户类型\",\"orderList\":[{\"orderId\":\"订单id\",\"productSum\":\"商品总数\",\"status\":0,\"receivingMethod\":1,\"payMethod\":0,\"goodsList\":[{\"name\":\"产品1\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15},{\"name\":\"产品2\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15}],\"originalPriceAll\":20,\"presentPriceAll\":18,\"goodsAddress\":\"地址\",\"consigneeName\":\"小明\",\"countNum\":5,\"consigneePhone\":13130005589,\"storePhone\":\"自提点联系电话（客户类型为消费者时需要有此字段）\"},{\"orderId\":\"订单id\",\"status\":1,\"receivingMethod\":1,\"payMethod\":0,\"goodsList\":[{\"name\":\"产品1\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15},{\"name\":\"产品2\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15}],\"originalPriceAll\":20,\"presentPriceAll\":18,\"goodsAddress\":\"地址\",\"consigneeName\":\"小明\",\"countNum\":5,\"consigneePhone\":13130005589}]}");
    }

    public void queryOrderById(){
        JsonHashMap jhm=new JsonHashMap();
        //接收前端参数：客户id
        String id=getPara("id");

        try{
            /**
             * 根据userId查询orderform表的cid得到多个订单编号oid，订单状态status，物流类型otransport_type，支付方式opay_type，
             * 订单原价ooriginal_sum，订单现价ocurrent_sum，订单地址oaddress，收货人姓名oname，联系电话ophone，
             */
            StringBuilder sql1Sb=new StringBuilder("SELECT o.oid AS orderId,o.onum AS orderNum,o.ostatus AS status,\n" +
                    "(select name from w_dictionary d where d.parent_id='100' and d.value=o.ostatus ) as status_text,\n" +
                    "o.otransport_type AS receivingMethod,\n" +
                    "(select name from w_dictionary d where d.parent_id='700' and d.value=o.otransport_type ) as receivingMethod_text,\n" +
                    "o.opay_type AS payMethod,\n" +
                    "(select name from w_dictionary d where d.parent_id='800' and d.value=o.opay_type ) as payMethod_text,\n" +
                    "FORMAT(o.ooriginal_sum,2)AS originalPriceAll,FORMAT(o.ocurrent_sum,2)AS presentPriceAll,o.oaddress AS goodsAddress,\n" +
                    "o.oname AS consigneeName,o.ophone AS consigneePhone FROM w_orderform o  WHERE   o.oid=? ");
            //根据oid查询orderform_detail表的oid得到多个 商品名称odname、商品数量odquantity、商品原价odoriginal_price、商品现价odcurrent_price
            StringBuilder sql2=new StringBuilder("SELECT oid AS orderId,GROUP_CONCAT(odname) AS productsN,GROUP_CONCAT(odquantity) AS productsQ,\n" +
                    "GROUP_CONCAT(FORMAT(odoriginal_price,2)) AS productsOP,GROUP_CONCAT(FORMAT(odcurrent_price,2))AS productsCP,GROUP_CONCAT(pid) AS goodsId \n" +
                    "FROM w_orderform_detail GROUP BY oid HAVING oid =?");

            //订单列表orderList
            Record order= Db.findFirst(sql1Sb.toString(),id);
            //将订单地址与自提点表的地址比较，得到storePhone
            String sql3="SELECT sphone AS storePhone FROM w_store WHERE saddress=?";

            //根据userId查询
            //订单详情列表
            List<Record> orderDetailList= Db.find(sql2.toString(),id);

            if (order!=null){
                //遍历订单列表
                Record phone=Db.findFirst(sql3,order.getStr("goodsAddress"));
                if (phone==null){
                    order.set("storePhone","");
                }else {
                    order.set("storePhone",phone.getStr("storePhone"));
                }
                //遍历订单详情商品列表
                for (Record orderDetail:orderDetailList){
                    if (StringUtils.equals(order.getStr("orderId"),orderDetail.getStr("orderId"))){
                        String []productsId=orderDetail.getStr("goodsId").split(",");
                        String []productsName=orderDetail.getStr("productsN").split(",");
                        String []productsQuantity=orderDetail.getStr("productsQ").split(",");
                        String productsOP=orderDetail.getStr("productsOP");
                        String []productsOriginalPrice=null;
                        if(productsOP!=null){
                            productsOriginalPrice=productsOP.split(",");
                        }
                        String []productsCurrentPrice=orderDetail.getStr("productsCP").split(",");
                        //订单详情商品列表
                        List<Record> goodsList=new ArrayList<>();
                        //订单详情商品数
                        int goodsListLen=productsName.length;
                        //将四个GROUP_CONCAT的数据分开后，对应起来，再放到goodsList中
                        for (int i=0;i<goodsListLen;i++){
                            Record product=new Record();
                            product.set("goodsId",productsId[i]);
                            product.set("name",productsName[i]);
                            product.set("number",productsQuantity[i]);
                            product.set("originalPrice",productsOriginalPrice[i]);
                            product.set("presentPrice",productsCurrentPrice[i]);
                            goodsList.add(product);
                        }
                        order.set("goodsList",goodsList);
                        break;
                    }
                }
            }
            jhm.putCode(1);
            jhm.put("order",order);
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器出现异常！");
        }
        renderJson(jhm);


//        renderJson("{\"code\":1,\"type\":\"客户类型\",\"orderList\":[{\"orderId\":\"订单id\",\"productSum\":\"商品总数\",\"status\":0,\"receivingMethod\":1,\"payMethod\":0,\"goodsList\":[{\"name\":\"产品1\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15},{\"name\":\"产品2\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15}],\"originalPriceAll\":20,\"presentPriceAll\":18,\"goodsAddress\":\"地址\",\"consigneeName\":\"小明\",\"countNum\":5,\"consigneePhone\":13130005589,\"storePhone\":\"自提点联系电话（客户类型为消费者时需要有此字段）\"},{\"orderId\":\"订单id\",\"status\":1,\"receivingMethod\":1,\"payMethod\":0,\"goodsList\":[{\"name\":\"产品1\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15},{\"name\":\"产品2\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15}],\"originalPriceAll\":20,\"presentPriceAll\":18,\"goodsAddress\":\"地址\",\"consigneeName\":\"小明\",\"countNum\":5,\"consigneePhone\":13130005589}]}");
    }

}
