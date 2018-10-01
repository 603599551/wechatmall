package com.wechatmall.pc.system;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.utils.UserSessionUtil;
import easy.util.DateTool;
import easy.util.NumberUtils;
import easy.util.UUIDTool;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.ArrayList;
import java.util.List;

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
        List<Object> params = new ArrayList<>();
        List<Object> list = new ArrayList<>();
        String select = "select *";
        String sql = " from (select wa.username, wa.`password`, wa. name nickname,(select id from h_job where wa.job_id = h_job.id) job_id,( select name from h_job where wa.job_id = h_job.id ) job, wa.status, wa.creater_id, wa.id from w_admin wa order by wa.creater_id desc) a where 1=1";
        if(name != null && name.length() > 0){
            name = "%" + name + "%";
            sql += "  and a.nickname like ? ";
            params.add(name);

        }
        if(job != null && job.length() > 0){
            job = "%" + job + "%";
            sql += "  and a.job_id like ? ";
            params.add(job);
        }
        try{
            /**
             * 查询用户列表
             */
            Page<Record> page = Db.paginate(pageNum, pageSize,select, sql,params.toArray());
            if(page != null && page.getList().size() > 0){
                jhm.putMessage("查询成功！");
                jhm.put("list",page);
            }else{
                jhm.put("list",list);
                renderJson(jhm);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
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
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //登录名
        String username = getPara("username");
        //登录密码
        String password = getPara("password");
        //姓名
        String name = getPara("nickname");
        //职位
        String job = getPara("job");
        //在职状态
        String status = getPara("status");
        UserSessionUtil usu = new UserSessionUtil(getRequest());
        //非空验证
        if(StringUtils.isEmpty(username)){
            jhm.putCode(0).putMessage("登录名为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(password)){
            jhm.putCode(0).putMessage("登录密码为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(name)){
            jhm.putCode(0).putMessage("姓名为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(job)){
            jhm.putCode(0).putMessage("职位为空！");
            renderJson(jhm);
            return;
        }
        Record addUser = new Record();
        /*
        * 查询该用户是否存在
        * */

        /*select name from h_job where wa.job_id = h_job.id*/
        String sql = "select count(1) from w_admin where username=? ";
        try{
           int num = Db.queryInt(sql,username);
            if(num > 0){
                jhm.putCode(0).putMessage("添加失败！该用户已存在");
                renderJson(jhm);
                return;
            }
            addUser.set("id", UUIDTool.getUUID());
            addUser.set("username",username);
            addUser.set("password",password);
            addUser.set("name",name);
            addUser.set("job_id",job);
            addUser.set("status",status);
            String time = DateTool.GetDateTime();
            addUser.set("creater_id", usu.getUserId());
            addUser.set("modifier_id", usu.getUserId());
            addUser.set("create_time", time);
            addUser.set("modify_time", time);
            boolean flag = Db.save("w_admin",addUser);
            if(flag){
                jhm.putMessage("添加成功！");
            }else{
                jhm.putCode(0).putMessage("添加失败！");
            }

        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(0).putMessage("服务器发生异常!");
        }
        renderJson(jhm);
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
        JsonHashMap jhm = new JsonHashMap();
        //用户id
        String id = getPara("id");
        //登录名
        String username = getPara("username");
        //登录密码
        String password = getPara("password");
        //姓名
        String name = getPara("nickname");
        //职位
        String job = getPara("job");
        //在职状态
        String status = getPara("status");
        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("用户id为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(username)){
            jhm.putCode(0).putMessage("登录名为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(password)){
            jhm.putCode(0).putMessage("登录密码为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(name)){
            jhm.putCode(0).putMessage("姓名为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(job)){
            jhm.putCode(0).putMessage("职位为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(status)){
            jhm.putCode(0).putMessage("在职状态为空！");
            renderJson(jhm);
            return;
        }

        /*
        * 添加用户
        * */

        Record modifyUserById = new Record();
        modifyUserById.set("id", id);
        modifyUserById.set("username", username);
        modifyUserById.set("password", password);
        modifyUserById.set("name", name);
        modifyUserById.set("job_id", job);
        modifyUserById.set("status", status);

        try{
            /**
             * 修改用户信息
             */
            boolean flag = Db.update("w_admin","id",modifyUserById);
            if(flag){
                jhm.putMessage("修改成功！");
            }else{
                jhm.putCode(0).putMessage("修改失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
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
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //系统用户id
        String id = getPara("id");
        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("系统用户id为空!");
            renderJson(jhm);
            return;
        }
        try{
            /**
             * 根据id删除该用户
             */
            String sql = "DELETE from w_admin where id=? ";
            int num = Db.update(sql,id);
            if(num > 0){
                jhm.putCode(1).putMessage("删除成功！");
                renderJson(jhm);
                return;
            }else{
                jhm.putCode(0).putMessage("删除失败！");
                renderJson(jhm);
                return;
            }

        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
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
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //系统用户id
        String id = getPara("id");
        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("系统用户id为空!");
            renderJson(jhm);
            return;
        }
        try{
            /**
             * 查询该用户
             */
            String sql = "select wa.username, wa.`password`, wa. name nickname, ( select name from h_job where wa.job_id = h_job.id ) job, wa.status from w_admin wa where id=?";
            Record idRecord = Db.findFirst(sql,id);
            if(idRecord!=null){
                jhm.putCode(1).putMessage("查询成功");
                renderJson(jhm);
                return;
            }else{
                jhm.putCode(0).putMessage("查询失败");
                renderJson(jhm);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"data\":{\"username\":\"登录名\",\"password\":\"登录密码\",\"nickname\":\"姓名\",\"job\":\"职位\",\"status\":\"在职状态\"}}");
    }
}
