package com.wechatmall.pc.price;

import com.common.controllers.BaseCtrl;

/**
 * PriceCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class PriceCtrl extends BaseCtrl{
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询定价信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/listPricesByGroupId
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  groupIdList    array                  不允许      选中的分组
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "productsList":[
    {
    "productName":"海飞丝",
    "originalPrice":"200.00",
    "presentPrice":"180.00"
    },
    {
    "productName":"海飞丝",
    "originalPrice":"200.00",
    "presentPrice":"180.00"
    }
    ]
    }
     * 失败：
     * {
    "code":"0",
    "message":"查看定价信息失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void listPricesByGroupId(){
        renderJson("{\"code\":1,\"productsList\":[{\"productName\":\"海飞丝\",\"originalPrice\":\"200.00\",\"presentPrice\":\"180.00\"},{\"productName\":\"海飞丝\",\"originalPrice\":\"200.00\",\"presentPrice\":\"180.00\"}]}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	提交定价信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/submitPrices
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  groupIdList    array                  不允许      选中的分组列表，内为选中分组ID
     *  pricesList     json                 不允许       商品列表带有商品现价
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"更新定价成功"
    }
     * 失败：
     * {
    "code":"0",
    "message":"更新定价失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void submitPrices(){
        renderJson("{\"code\":1,\"message\":\"更新定价成功\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询分组信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/listGroupsAndCustomers
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  groupName      String                  不允许      分组名称
     *  groupNumber     String                 不允许      分组编号
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "groupList":[
    {
    "groupId":"gx00001",
    "groupName":"分组1",
    "list":[
    {
    "merchantNumber":"mc00001",
    "merchantName":"商户1"
    },
    {
    "merchantNumber":"mc00002",
    "merchantName":"商户2"
    }
    ]
    },
    {
    "groupId":"gx00001",
    "groupName":"分组1",
    "list":[
    {
    "merchantNumber":"mc00001",
    "merchantName":"商户1"
    },
    {
    "merchantNumber":"mc00002",
    "merchantName":"商户2"
    }
    ]
    }
    ]
    }
     * 失败：
     * {
    "code":"0",
    "message":"查询分组失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void listGroupsAndCustomers(){
        renderJson("{\"code\":1,\"groupList\":[{\"groupId\":\"gx00001\",\"groupName\":\"分组1\",\"list\":[{\"merchantNumber\":\"mc00001\",\"merchantName\":\"商户1\"},{\"merchantNumber\":\"mc00002\",\"merchantName\":\"商户2\"}]},{\"groupId\":\"gx00001\",\"groupName\":\"分组1\",\"list\":[{\"merchantNumber\":\"mc00001\",\"merchantName\":\"商户1\"},{\"merchantNumber\":\"mc00002\",\"merchantName\":\"商户2\"}]}]}");
    }
}
