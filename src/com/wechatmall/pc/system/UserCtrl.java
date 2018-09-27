package com.wechatmall.pc.system;

import com.common.controllers.BaseCtrl;
import easy.util.NumberUtils;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

/**
 * TransportCtrl class
 * @author zhanglei
 * @date   2018-9-27
 */

public class UserCtrl extends BaseCtrl{

    /**
     * @author zhanglei
     * @date 2018-9-27
     * 名称  	查看系统用户列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/system/user/listUser
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	    类型	     最大长度	     允许空	    描述
     *  name       string                不允许      查询添加，根据用户姓名模糊查询
     *  job        string                不允许      查询添加，根据用户职位模糊查询
     *  pageSize   string                不允许      每页限制的行数
     *  pageNumber string                不允许      页码
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "data":{
    "firstPage":false,
    "lastPage":false,
    "pageNumber":"2",
    "pageSize":"10",
    "totalPage":"20",
    "totalRow":"200",
    "list":[
    {
    "username":"登录名",
    "password":"登录密码",
    "nickname":"姓名",
    "job":"职位",
    "status":"在职状态",
    "creatorId":"创建人id"
    },
    {
    "username":"登录名",
    "password":"登录密码",
    "nickname":"姓名",
    "job":"职位",
    "status":"在职状态",
    "creatorId":"创建人id"
    }
    ]
    }
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

    public void listUser(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //用户姓名
        String name = getPara("name");
        //用户职位
        String job = getPara("job");
        //当前页
        String pageNumStr = getPara("pageNum");
        //页面显示的条数
        String pageSizeStr = getPara("pageSize");
        int pageNum,pageSize;
        //非空验证
//        if(StringUtils.isEmpty(name)){
//            jhm.putCode(0).putMessage("用户姓名为空!");
//            renderJson(jhm);
//            return;
//        }
//        if(StringUtils.isEmpty(job)){
//            jhm.putCode(0).putMessage("用户职位为空!");
//            renderJson(jhm);
//            return;
//        }
        if(StringUtils.isEmpty(pageNumStr)){
            pageNum = NumberUtils.parseInt(pageNumStr,1);
        }else {
            pageNum = Integer.parseInt(pageNumStr);
        }
        if(StringUtils.isEmpty(pageSizeStr)){
            pageSize = NumberUtils.parseInt(pageSizeStr,10);
        }else {
            pageSize = Integer.parseInt(pageSizeStr);
        }
       // renderJson("{\"code\":1,\"data\":{\"firstPage\":false,\"lastPage\":false,\"pageNumber\":\"2\",\"pageSize\":\"10\",\"totalPage\":\"20\",\"totalRow\":\"200\",\"list\":[{\"username\":\"登录名\",\"password\":\"登录密码\",\"nickname\":\"姓名\",\"job\":\"职位\",\"status\":\"在职状态\",\"creatorId\":\"创建人id\"},{\"username\":\"登录名\",\"password\":\"登录密码\",\"nickname\":\"姓名\",\"job\":\"职位\",\"status\":\"在职状态\",\"creatorId\":\"创建人id\"}]}}");
    }

    /**
     * @author zhanglei
     * @date 2018-9-27
     * 名称  	添加系统用户
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/system/user/addUser
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	    类型	     最大长度	     允许空	    描述
     *  username    string               不允许      登录名
     *  password    string               不允许      登录密码
     *  nickname    string               不允许      姓名
     *  job         string               不允许      职位
     *  status      string               不允许      在职状态
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"添加成功",
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

    public void addUser(){
       // renderJson("{\"code\":\"1\",\"message\":\"修改成功！\"}");
    }

    /**
     * @author zhanglei
     * @date 2018-9-27
     * 名称  	修改系统用户
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/system/user/modifyUserById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	    类型	     最大长度	     允许空	    描述
     *  id          string               不允许      系统用户id
     *  username    string               不允许      登录名
     *  password    string               不允许      登录密码
     *  nickname    string               不允许      姓名
     *  job         string               不允许      职位
     *  status      string               不允许      在职状态
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
    public void modifyUserById(){
        //renderJson("{\"code\":1,\"message\":\"修改成功\"}");
    }

    /**
     * @author zhanglei
     * @date 2018-9-27
     * 名称  	删除系统用户
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/system/user/deleteUserById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	    类型	     最大长度	     允许空	    描述
     *  id          string               不允许      系统用户id
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
    public void deleteUserById(){
        //renderJson("{\"code\":\"1\",\"message\":\"删除成功！\"}");
    }

    /**
     * @author zhanglei
     * @date 2018-9-27
     * 名称  	根据id查询系统用户信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/system/user/showUserById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	    类型	     最大长度	     允许空	    描述
     *  id          string               不允许      系统用户id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "data":{
    "username":"登录名",
    "password":"登录密码",
    "nickname":"姓名",
    "job":"职位",
    "status":"在职状态"
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
    public void showUserById(){
        //renderJson("{\"code\":1,\"data\":{\"username\":\"登录名\",\"password\":\"登录密码\",\"nickname\":\"姓名\",\"job\":\"职位\",\"status\":\"在职状态\"}}");
    }
}
