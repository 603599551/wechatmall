package com.wechatmall.mobile.shoppingcart;

import com.common.controllers.BaseCtrl;
/**
 * customerCtrl class
 * @author liushiwen
 * @date   2018-9-22
 */
public class ShoppingCartCtrl extends BaseCtrl {

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	购物车商品列表查询
     * 描述     显示购物车的商品
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/shoppingCartList
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
        "code":1,
        "goodsList":[
        {
        "goodsId":"11",
        "url":"图片的地址",
        "originalPrice":20,
        "presentPrice":18,
        "name":"过水手擀面",
        "types":[
        "芥末味",
        "番茄味"
        ],
        "number":1
        },
        {
        "goodsId":"11",
        "url":"图片的地址",
        "originalPrice":20,
        "presentPrice":18,
        "name":"过水手擀面",
        "types":[
        "芥末味",
        "番茄味"
        ],
        "number":2
        }
        ]
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
    public void shoppingCartList(){
        renderJson("{\"code\":1,\"goodsList\":[{\"goodsId\":\"11\",\"url\":\"图片的地址\",\"originalPrice\":20,\"presentPrice\":18,\"name\":\"过水手擀面\",\"types\":[\"芥末味\",\"番茄味\"],\"number\":1},{\"goodsId\":\"11\",\"url\":\"图片的地址\",\"originalPrice\":20,\"presentPrice\":18,\"name\":\"过水手擀面\",\"types\":[\"芥末味\",\"番茄味\"],\"number\":2}]}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	购物车商品-删除
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/deleteGoods
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	string		            不允许	 用户的id
     *listId    string                  不允许   商品的id
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":1,
        "message":"删除成功！"
    }
     * 失败：
     * {
    "code": 0,
    "message": "删除失败！"
    }

     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void deleteGoods(){
        renderJson("{\"code\":1,\"message\":\"删除成功！\"}");
    }

}
