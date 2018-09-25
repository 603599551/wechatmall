package com.wechatmall.pc.system;

import com.common.controllers.BaseCtrl;
/**
 * PayCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class PayCtrl extends BaseCtrl{

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	添加支付方式
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/addPayType
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  name      string                  不允许   	支付方式名称
     *  desc      string                  不允许    支付方式备注
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":"1",
    "message":"添加成功！"
    }
     * 失败：
     * {
    "code":"0",
    "message":"添加失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void addPayType(){
        renderJson("{\"code\":\"1\",\"message\":\"添加成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改支付方式
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/modifyPayTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  name      string                  不允许   	支付方式名称
     *  id        string                  不允许    支付方式编号
     *  desc      string                  不允许    支付方式备注
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":"1",
    "message":"修改成功！"
    }
     * 失败：
     * {
    "code":"0",
    "message":"修改失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */

    public void modifyPayTypeById(){
        renderJson("{\"code\":\"1\",\"message\":\"修改成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	删除支付方式
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/deletePayTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  id        string                  不允许    支付方式编号
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":"1",
    "message":"删除成功！"
    }
     * 失败：
     * {
    "code":"0",
    "message":"删除失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void deletePayTypeById(){
        renderJson("{\"code\":\"1\",\"message\":\"删除成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	根据id查询支付方式
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/showPayTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  id        string                  不允许    支付方式编号
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":1,
        "message":"查询成功",
        "name":"支付方式名称",
        "desc":"支付方式备注"
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

    public void showPayTypeById(){
        renderJson("{\"code\":1,\"message\":\"查询成功\",\"name\":\"支付方式名称\",\"desc\":\"支付方式备注\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询支付方式列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/listPayType
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度 	允许空	 描述
     *  name     string                    不允许    查询添加，根据支付方式名称完全匹配查询
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {{
        "code":1,
        "message":"查询成功",
        "list":[{
        "id":"支付方式编号",
        "name":"支付方式名称",
        "desc":"支付方式备注"
        },{
        "id":"支付方式编号",
        "name":"支付方式名称",
        "desc":"支付方式备注"
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

    public void listPayType(){
        renderJson("{\"code\":1,\"message\":\"查询成功\",\"list\":[{\"id\":\"支付方式编号\",\"name\":\"支付方式名称\",\"desc\":\"支付方式备注\"},{\"id\":\"支付方式编号\",\"name\":\"支付方式名称\",\"desc\":\"支付方式备注\"}]}");
    }
}
