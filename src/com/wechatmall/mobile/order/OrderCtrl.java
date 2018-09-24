package com.wechatmall.mobile.order;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
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
        renderJson("{\"code\":1,\"selfAddressedAddress\":[{\"address\":\"最近的地址\",\"miles\":75},{\"address\":\"地址二\",\"miles\":75}],\"contacts\":[{\"name\":\"小明\",\"phone\":13130005589,\"address\":\"地址\",\"isDefult\":1},{\"name\":\"小明\",\"phone\":13130005589,\"address\":\"地址\",\"isDefult\":0}],\"receivingMethod\":[\"自提\",\"快递\"],\"payMethod\":[\"微信\",\"账期\"]}");
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
     * phone      int                   不允许   用户手机号
     * goodsList   array                不允许   订单商品列表
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
        renderJson("{\"code\":1,\"message\":\"提交成功！\"}");
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
        renderJson(jhm);


//        renderJson("{\"code\":1,\"type\":\"客户类型\",\"orderList\":[{\"orderId\":\"订单id\",\"status\":0,\"receivingMethod\":1,\"payMethod\":0,\"goodsList\":[{\"name\":\"产品1\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15},{\"name\":\"产品2\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15}],\"originalPrice\":20,\"presentPrice\":18,\"goodsAddress\":\"地址\",\"consigneeName\":\"小明\",\"consigneePhone\":13130005589,\"storePhone\":\"自提点联系电话（客户类型为消费者时需要有此字段）\"},{\"orderId\":\"订单id\",\"status\":1,\"receivingMethod\":1,\"payMethod\":0,\"goodsList\":[{\"name\":\"产品1\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15},{\"name\":\"产品2\",\"number\":1,\"originalPrice\":18,\"presentPrice\":15}],\"originalPriceAll\":20,\"presentPriceAll\":18,\"goodsAddress\":\"地址\",\"consigneeName\":\"小明\",\"consigneePhone\":13130005589}]}");
    }

}
