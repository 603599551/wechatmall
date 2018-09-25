package com.wechatmall.pc.notice;

import com.common.controllers.BaseCtrl;

/**
 * NoticeCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class NoticeCtrl extends BaseCtrl {

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	新增通知
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/addNotice
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  type          string                  不允许      	通知类型
     *  content      string                 不允许       通知内容
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"提交成功"
    }
     * 失败：
     * {
    "code":"0",
    "message":"提交失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void addNotice(){
        renderJson("{\"code\":1,\"message\":\"提交成功\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改通知
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/updateNoticeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  id             string               不允许       通知id
     *  type           string               不允许       通知类型
     *  content        string               不允许       通知内容
     *
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
    public void updateNoticeById(){
        renderJson("{\"code\":1,\"message\":\"提交成功\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	删除通知
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/deleteNoticeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  id             string               不允许       通知id
     *
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
    public void deleteNoticeById(){
        renderJson("{\"code\":1,\"message\":\"删除成功\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询通知列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/listNotice
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  content             string               不允许       查询添加，根据通知内容模糊查询
     *  type                string                不允许      查询添加，根据通知类型完全匹配查询
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"修改成功！",
    "data":{
    "totalRow":1,
    "pageNumber":1,
    "firstPage":true,
    "lastPage":true,
    "totalPage":1,
    "pageSize":10,
    "list":[
    {
    "id":"通知id",
    "content":"通知内容",
    "type":"通知类型",
    "createTime":"创建时间",
    "modifyTime":"修改时间"
    }
    ]
    }
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
    public void listNotice(){
        renderJson("{\"code\":1,\"message\":\"修改成功！\",\"data\":{\"totalRow\":1,\"pageNumber\":1,\"firstPage\":true,\"lastPage\":true,\"totalPage\":1,\"pageSize\":10,\"list\":[{\"id\":\"通知id\",\"content\":\"通知内容\",\"type\":\"通知类型\",\"createTime\":\"创建时间\",\"modifyTime\":\"修改时间\"}]}}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	根据id查看通知
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/showNoticeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  id             string               不允许       通知id
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code": "1",
    "data": {
    "id": "通知id",
    "content": "通知内容",
    "type":"通知类型"
    }
    }
     * 失败：
     * {
    "code":"0",
    "message":"查看失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void showNoticeById(){
        renderJson("{\"code\":\"1\",\"data\":{\"id\":\"通知id\",\"content\":\"通知内容\",\"type\":\"通知类型\"}}");
    }
}
