package com.wechatmall.pc.order;

import com.common.controllers.BaseCtrl;

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
        renderJson("{\"code\":1,\"list\":[{\"orderId\":\"11da\",\"time\":\"2018/9/9\",\"customerName\":\"客户姓名\",\"receiverName\":\"收货人姓名\",\"phone\":1313005589,\"type\":0,\"address\":\"收货地址\",\"originalPrice\":20,\"presentPrice\":18,\"status\":0},{\"orderId\":\"11da\",\"time\":\"2018/9/9\",\"customerName\":\"客户姓名\",\"receiverName\":\"收货人姓名\",\"phone\":1313005589,\"type\":0,\"address\":\"收货地址\",\"originalPrice\":20,\"presentPrice\":18,\"status\":0}]}");
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
        renderJson("{\"code\":1,\"customerType\":0,\"orderId\":\"A5454845648\",\"transportType\":\"物流类型\",\"payType\":\"支付方式\",\"products\":[{\"name\":\"大米\",\"originalPrice\":20,\"presentPrice\":18,\"number\":1,\"singleSum\":20},{\"name\":\"大米\",\"originalPrice\":20,\"presentPrice\":18,\"number\":1,\"singleSum\":20}],\"orderOriginalSum\":400,\"orderPresentSum\":380}");
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
        renderJson("{\"code\":1,\"list\":[{\"orderId\":\"A6464546548945\",\"customerName\":\"小明\",\"customerPhone\":13130005589,\"address\":\"用户地址\",\"createTime\":\"2018-09-16 23:00:00\",\"transportType\":\"物流类型\",\"payType\":\"支付方式\",\"orderOriginalSum\":20,\"orderPresentSum\":19,\"productsList\":[{\"productId\":\"51564157854\",\"productName\":\"大米\",\"productNum\":1,\"productPrice\":18}]},{\"orderId\":\"A6464546548945\",\"customerName\":\"小明\",\"customerPhone\":13130005589,\"address\":\"用户地址\",\"createTime\":\"2018-09-16 23:00:00\",\"transportType\":\"物流类型\",\"payType\":\"支付方式\",\"orderOriginalSum\":20,\"orderPresentSum\":19,\"productsList\":[{\"productId\":\"15615648748794\",\"productName\":\"大米\",\"productNum\":1,\"productPrice\":18}]}]}");
    }

}
