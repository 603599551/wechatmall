package com.wechatmall.pc.system;

import com.common.controllers.BaseCtrl;

/**
 * JobCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class JobCtrl extends BaseCtrl{

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	新增职务
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/addJob
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  jobName          string                  不允许      	职务名称
     *  jobDesc          string                  不允许         职务描述
     *  jobPermission    string                  不允许        	职务权限
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"操作成功"
    }
     * 失败：
     * {
    "code":"0",
    "message":"操作失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void addJob(){
        renderJson("{\"code\":1,\"message\":\"操作成功\"}");
    }
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改职务
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/modifyJobById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  jobName          string                  不允许      	职务名称
     *  jobId            string                  不允许          职务id
     *  jobDesc          string                  不允许         职务描述
     *  jobPermission    string                  不允许        	职务权限
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"修改成功"
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
    public void modifyJobById(){
        renderJson("{\"code\":1,\"message\":\"修改成功\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	删除职务
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/deleteJobById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  jobId            string                  不允许          职务id
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"删除成功"
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
    public void deleteJobById(){
        renderJson("{\"code\":1,\"message\":\"删除成功\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	根据id查询职务信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/showJobById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  jobId            string                  不允许          职务id
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "job":{
    "name":"职务名称",
    "desc":"职务描述",
    "id":"职务id"
    },
    "menuList":[
    "5",
    "30",
    "6",
    "40",
    "8",
    "7",
    "43",
    "24",
    "11",
    "31",
    "21",
    "42",
    "29",
    "39",
    "38"
    ]
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
    public void showJobById(){
        renderJson("{\"code\":1,\"job\":{\"name\":\"职务名称\",\"desc\":\"职务描述\",\"id\":\"职务id\"},\"menuList\":[\"5\",\"30\",\"6\",\"40\",\"8\",\"7\",\"43\",\"24\",\"11\",\"31\",\"21\",\"42\",\"29\",\"39\",\"38\"]}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询职务列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/showJobList
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":"1",
    "list":[
    {
    "name":"职务名称",
    "id":"职务id",
    "staffCount":"人数",
    "staffsName":"人员名字"
    },
    {
    "name":"职务名称",
    "id":"职务id",
    "staffCount":"人数",
    "staffsName":"人员名字"
    }
    ]
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
    public void showJobList(){
        renderJson("{\"code\":\"1\",\"list\":[{\"name\":\"职务名称\",\"id\":\"职务id\",\"staffCount\":\"人数\",\"staffsName\":\"人员名字\"},{\"name\":\"职务名称\",\"id\":\"职务id\",\"staffCount\":\"人数\",\"staffsName\":\"人员名字\"}]}");
    }
}
