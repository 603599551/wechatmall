package com.wechatmall.pc.system;

import com.common.controllers.BaseCtrl;

/**
 * TransportCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class TransportCtrl extends BaseCtrl {

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	添加物流类型
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/addTransportType
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  name      string                  不允许   物流类型名称
     *  desc      string                  不允许   物流类型备注
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
    public void addTransportType(){
        renderJson("{\"code\":\"1\",\"message\":\"添加成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改物流类型
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/modifyTransportTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  name      string                  不允许   物流类型名称
     *  id        string                  不允许   物流类型id
     *  desc      string                  不允许   物流类型备注
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

    public  void modifyTransportTypeById(){
        renderJson("{\"code\":\"1\",\"message\":\"修改成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	删除物流类型
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/deleteTransportTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  id        string                  不允许   物流类型id
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
    public void deleteTransportTypeById(){
        renderJson("{\"code\":\"1\",\"message\":\"删除成功！\"}");
    }
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	根据id查询物流类型
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/showTransportTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  id        string                  不允许   物流类型编号
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":1,
        "message":"查询成功",
        "name":"物流类型名称",
        "desc":"物流类型备注"
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
    public void showTransportTypeById(){
        renderJson("{\"code\":1,\"message\":\"查询成功\",\"name\":\"物流类型名称\",\"desc\":\"物流类型备注\"}");
    }
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询物流类型列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/listTransportType
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  type     string               不允许   查询添加，根据物流类型名称模糊查询
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":1,
        "message":"查询成功",
        "list":[{
        "id":"物流类型编号",
        "name":"物流类型名称",
        "desc":"物流类型备注"
        },{
        "id":"物流类型编号",
        "name":"物流类型名称",
        "desc":"物流类型备注"
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
    public void listTransportType(){
        renderJson("{\"code\":1,\"message\":\"查询成功\",\"list\":[{\"id\":\"物流类型编号\",\"name\":\"物流类型名称\",\"desc\":\"物流类型备注\"},{\"id\":\"物流类型编号\",\"name\":\"物流类型名称\",\"desc\":\"物流类型备注\"}]}");
    }
}
