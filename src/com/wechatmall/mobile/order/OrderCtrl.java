package com.wechatmall.mobile.order;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import easy.util.DateTool;
import easy.util.UUIDTool;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.ArrayList;
import java.util.List;

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
     * URL	    http://localhost:8081/wm/mobile/order/querySubmitOrder
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
        String sql1="SELECT saddress AS address FROM w_store ";
        //根据userId查询customer_address表得到多个 收货人姓名name，联系电话phone，收货地址province+city+district+address,默认状态isDefault
        String sql2="SELECT caname AS name,caphone AS phone,CONCAT(caprovince,cacity,cadistrict,caaddress) AS address,castatus AS isDefault FROM w_customer_address WHERE cid=?";
        //根据value值查询dictionary表得到 物流类型和支付类型
        String sql3="SELECT dname AS name FROM w_dictionary WHERE dparent_id = (SELECT did FROM w_dictionary WHERE dvalue =?)";

        try{
            //自提点列表
            List<Record> storeList=Db.find(sql1);
            //收货地址列表
            List<Record> contactList=Db.find(sql2,userId);
            //物流类型列表
            List<Record> transportList=Db.find(sql3,transportType);
            List<String> receivingMethod=new ArrayList<>(10);
            if (transportList!=null||transportList.size()>0){
                for (Record tl:transportList){
                    receivingMethod.add(tl.getStr("name"));
                }
            }
            //支付类型列表
            List<Record> payList=Db.find(sql3,payType);
            List<String> payMethod =new ArrayList<>(10);
            if (payList!=null||payList.size()>0){
                for (Record pl:payList){
                    payMethod.add(pl.getStr("name"));
                }
            }
            jhm.putCode(1).putMessage("查询成功");
            jhm.put("selfAddressedAddress",storeList);
            jhm.put("contacts",contactList);
            jhm.put("receivingMethod",receivingMethod);
            jhm.put("payMethod",payMethod);

        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"selfAddressedAddress\":[{\"address\":\"最近的地址\",\"miles\":75},{\"address\":\"地址二\",\"miles\":75}],\"contacts\":[{\"name\":\"小明\",\"phone\":13130005589,\"address\":\"地址\",\"isDefult\":1},{\"name\":\"小明\",\"phone\":13130005589,\"address\":\"地址\",\"isDefult\":0}],\"receivingMethod\":[\"自提\",\"快递\"],\"payMethod\":[\"微信\",\"账期\"]}");
    }
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	提交订单
     * 描述     将订单的内容存入数据库
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/placeOrder
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
        String name = getPara("name");
        //用户手机号
        String phone = getPara("phone");
        //订单商品列表
        String goodsString = getPara("goodsList");
        JSONArray goodsList = JSONArray.fromObject(goodsString);

        //收货方式
        String receivingMethod = getPara("receivingMethod");
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
        if (goodsList==null||goodsList.size()<=0){
            jhm.putCode(0).putMessage("订单商品列表为空！");
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
        //订单原总价
        float orderOriginalSum = Float.valueOf("orderOriginalSum");
        //订单现总价
        float orderCurrentSum = Float.valueOf("orderCurrentSum");

        try{
            /**
             * 新增订单
             */
            //订单信息表对象
            Record w_orderform = new Record();
            w_orderform.set("oid", UUIDTool.getUUID());
            w_orderform.set("caid","");
            w_orderform.set("cid",userId);
            w_orderform.set("oname",name);
            w_orderform.set("ophone",phone);
            w_orderform.set("oaddress",address);
            w_orderform.set("ooriginal_sum",orderOriginalSum);
            w_orderform.set("ocurrent_sum",orderCurrentSum);
            w_orderform.set("ostatus","pending_pay");
            w_orderform.set("otransport_type",receivingMethod);
            w_orderform.set("opay_type",payMethod);
            w_orderform.set("ocreate_time", DateTool.GetDateTime());
            w_orderform.set("omodify_time", DateTool.GetDateTime());
            w_orderform.set("ocreator_id",userId);
            w_orderform.set("omodifier_id",userId);
            w_orderform.set("odesc","");
            boolean orderFormFlag = Db.save("w_orderform","oid",w_orderform);
            if(orderFormFlag == false){
                jhm.putCode(0).putMessage("提交失败!");
                renderJson(jhm);
                return;
            }
            /**
             * 新增订单详情
             */
            //订单详情表对象
            Record w_orderform_detail = new Record();
            //定义新增订单后的布尔值
            boolean orderFormDetailFlag = false;

            String sql = "select pkeyword odkeyword,price odoriginal_price from w_product where pid=?";
            //获取商品
            for(int i = 0; i < goodsList.size(); i++){
                //遍历json数组，转换成json对象
                JSONObject goodsListJSON = goodsList.getJSONObject(i);
                Record product = Db.findFirst(sql,goodsListJSON.get("id"));
                w_orderform_detail.set("odid", UUIDTool.getUUID());
                w_orderform_detail.set("oid",w_orderform.get("oid"));
                w_orderform_detail.set("pid",goodsListJSON.get("id"));
                w_orderform_detail.set("odname",goodsListJSON.get("name"));
                w_orderform_detail.set("odoriginal_price",product.get("odoriginal_price"));
                w_orderform_detail.set("odcurrent_price", goodsListJSON.get("price"));
                w_orderform_detail.set("odquantity",goodsListJSON.get("number"));
                w_orderform_detail.set("odkeyword",product.get("odkeyword"));
                Double sum =  (Double) goodsListJSON.get("price") * (Integer) goodsListJSON.get("number");
                w_orderform_detail.set("odsingle_sum",sum);
                w_orderform_detail.set("odcreate_time",DateTool.GetDateTime());
                w_orderform_detail.set("odmodify_time",DateTool.GetDateTime());
                w_orderform_detail.set("odcreator_id",userId);
                w_orderform_detail.set("odmodifier_id",userId);
                w_orderform_detail.set("oddesc","");
                orderFormDetailFlag = Db.save("w_orderform_detail","odid",w_orderform_detail);
                if(orderFormDetailFlag == false){
                    jhm.putCode(0).putMessage("提交失败！");
                    renderJson(jhm);
                    return;
                }
            }
                jhm.putMessage("提交成功！");

        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"message\":\"提交成功！\"}");
    }


    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	订单查询
     * 描述     默认显示所有订单
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/queryOrder
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
            String sql1="SELECT o.oid AS orderId,o.ostatus AS status,o.otransport_type AS receivingMethod,o.opay_type AS payMethod,\n" +
                    "o.ooriginal_sum AS originalPriceAll,o.ocurrent_sum AS presentPriceAll,o.oaddress AS goodsAddress,\n" +
                    "o.oname AS consigneeName,o.ophone AS consigneePhone FROM w_customer c,w_orderform o WHERE c.cid=? AND o.cid=?";
            //订单列表orderList
            List<Record> orderList= Db.find(sql1,userId,userId);

            //根据oid查询orderform_detail表的oid得到多个 商品名称odname、商品数量odquantity、商品原价odoriginal_price、商品现价odcurrent_price
            String sql2="SELECT oid AS orderId,GROUP_CONCAT(odname) AS productsN,GROUP_CONCAT(odquantity) AS productsQ,\n" +
                    "GROUP_CONCAT(odoriginal_price) AS productsOP,GROUP_CONCAT(odcurrent_price)AS productsCP \n" +
                    "FROM w_orderform_detail GROUP BY oid HAVING oid IN (SELECT oid FROM w_orderform WHERE cid=?)";

            //将订单地址与自提点表的地址比较，得到storePhone
            String sql3="SELECT sphone AS storePhone FROM w_store WHERE saddress=?";

            //根据userId查询
            //订单详情列表
            List<Record> orderDetailList= Db.find(sql2,userId);

            if (orderList!=null||orderList.size()>0){
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
                            String []productsName=orderDetail.getStr("productsN").split(",");
                            String []productsQuantity=orderDetail.getStr("productsQ").split(",");
                            String []productsOriginalPrice=orderDetail.getStr("productsOP").split(",");
                            String []productsCurrentPrice=orderDetail.getStr("productsCP").split(",");
                            //订单详情商品列表
                            List<Record> goodsList=new ArrayList<>();
                            //订单详情商品项数
                            int goodsListLen=productsName.length;
                            //商品个数=每个商品的数量总和
                            int productSum=0;
                            //将四个GROUP_CONCAT的数据分开后，对应起来，再放到goodsList中
                            for (int i=0;i<goodsListLen;i++){
                                Record product=new Record();
                                product.set("name",productsName[i]);
                                product.set("number",productsQuantity[i]);
                                product.set("originalPrice",productsOriginalPrice[i]);
                                product.set("presentPrice",productsCurrentPrice[i]);
                                goodsList.add(product);
                                productSum+=Integer.parseInt(productsQuantity[i]);
                            }
                            order.set("productSum",productSum);
                            order.set("goodsList",goodsList);
                            break;
                        }
                    }
                }
            }
            jhm.putCode(1).putMessage("查询成功");
            jhm.put("orderList",orderList);
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器出现异常！");
        }
//        renderJson(jhm);


        renderJson("{\"code\":1,\"type\":\"客户类型\",\"orderList\":[{\"orderId\":\"订单id\",\"productSum\":\"商品总数\",\"status\":0,\"receivingMethod\":1,\"payMethod\":0,\"goodsList\":[{\"name\":\"产品1\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15},{\"name\":\"产品2\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15}],\"originalPriceAll\":20,\"presentPriceAll\":18,\"goodsAddress\":\"地址\",\"consigneeName\":\"小明\",\"countNum\":5,\"consigneePhone\":13130005589,\"storePhone\":\"自提点联系电话（客户类型为消费者时需要有此字段）\"},{\"orderId\":\"订单id\",\"status\":1,\"receivingMethod\":1,\"payMethod\":0,\"goodsList\":[{\"name\":\"产品1\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15},{\"name\":\"产品2\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15}],\"originalPriceAll\":20,\"presentPriceAll\":18,\"goodsAddress\":\"地址\",\"consigneeName\":\"小明\",\"countNum\":5,\"consigneePhone\":13130005589}]}");
    }

}
