package com.wechatmall.mobile.order;

import com.common.controllers.BaseCtrl;
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
     * URL	    http://localhost:8080/wcos/customer/querySubmitOrder
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
        renderJson("{\"code\":1,\"address\":[{\"address\":\"最近的地址\",\"miles\":75},{\"address\":\"地址二\",\"miles\":75}],\"contacts\":[{\"name\":\"小明\",\"phone\":13130005589,\"isDefult\":1},{\"name\":\"小明\",\"phone\":13130005589,\"isDefult\":0}]}");
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
        renderJson("{\"code\":1,\"order\":[{\"status\":0,\"goodsList\":[{\"name\":\"产品1\",\"number\":1},{\"name\":\"产品2\",\"number\":1}],\"originalPrice\":20,\"presentPrice\":18,\"goodsAddress\":\"地址\",\"consigneeName\":\"小明\",\"consigneePhone\":13130005589,\"storePhone\":\"自提点联系电话（客户类型为消费者时需要有此字段）\"},{\"status\":1,\"goodsList\":[{\"name\":\"产品1\",\"number\":1},{\"name\":\"产品2\",\"number\":1}],\"originalPrice\":20,\"presentPrice\":18,\"goodsAddress\":\"地址\",\"consigneeName\":\"小明\",\"consigneePhone\":13130005589}]}");
    }

}
