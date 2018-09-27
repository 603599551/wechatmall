package com.wechatmall.pc.system;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.utils.UserSessionUtil;
import easy.util.DateTool;
import easy.util.NumberUtils;
import easy.util.UUIDTool;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.ArrayList;
import java.util.List;


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
     *  adminId          string                  不允许         系统用户id
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
        JsonHashMap jhm = new JsonHashMap();
        UserSessionUtil usu=new UserSessionUtil(getRequest());
        /**
         * 接收前台参数
         */
        //职务名称
        String jobName = getPara("jobName");
        //职务描述
        String jobDesc = getPara("jobDesc");
        //职务权限
        String jobPermission = getPara("jobPermission");
        JSONArray jobPermissionList = JSONArray.fromObject(jobPermission);

        //非空验证
        if(StringUtils.isEmpty(jobName)){
            jhm.putCode(0).putMessage("职务名称为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(jobDesc)){
            jhm.putCode(0).putMessage("职务描述为空!");
            renderJson(jhm);
            return;
        }
        if(jobPermissionList != null && jobPermissionList.size() > 0){
            jhm.putCode(0).putMessage("职务权限为空!");
            renderJson(jhm);
            return;
        }
        List lists = new ArrayList<>();
        for (int i = 0; i < jobPermissionList.size(); i++) {
            lists.add(i,jobPermissionList.getJSONObject(i).get("jobPermission"));
        }
        //通过循环，将json数组中key为words的数据添加到list集合中
        String jobPermissionStr = lists.toString();
        try{
            //根据系统用户id，查找对应的姓名
            String sql = "select adname from w_admin where adid = ?";
            Record adminName = Db.findFirst(sql,usu.getUserId());
            //判断新增的职务是否重复
            String sql1 = "select count(1) from h_job where name=?";
            int count = Db.queryInt(sql1,jobName);
            if(count > 0){
                jhm.putCode(0).putMessage("职务名称重复!");
                renderJson(jhm);
                return;
            }

            Record jobRecord = new Record();
            jobRecord.set("id", UUIDTool.getUUID());
            jobRecord.set("name",jobName);
            jobRecord.set("desc",jobDesc);
            jobRecord.set("create_time", DateTool.GetDateTime());
            jobRecord.set("creator",usu.getUserId());
            jobRecord.set("creator_name",adminName);
            jobRecord.set("modify_time",DateTool.GetDateTime());
            jobRecord.set("modifier",usu.getUserId());
            jobRecord.set("modifier_name",adminName);
            //在职务表里添加数据
            boolean jobFlag = Db.save("h_job",jobRecord);
            if(jobFlag == false){
                jhm.putCode(0).putMessage("操作失败!");
                renderJson(jhm);
                return;
            }

            Record jobMenuRecord = new Record();
            jobMenuRecord.set("id",UUIDTool.getUUID());
            jobMenuRecord.set("menu_id",jobPermissionStr);
            jobMenuRecord.set("job_id",jobRecord.get("id"));
            jobMenuRecord.set("access","1");
            jobMenuRecord.set("creator",usu.getUserId());
            jobMenuRecord.set("creator_name",adminName);
            jobMenuRecord.set("create_time",DateTool.GetDateTime());
            //在职务权限表里添加数据
            boolean jobMenuFlag = Db.save("h_author_job_menu",jobMenuRecord);
            if(jobMenuFlag == false){
                jhm.putCode(0).putMessage("操作失败!");
                renderJson(jhm);
                return;
            }else{
                jhm.putMessage("操作成功!");
            }
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(0).putMessage("Record发生异常!");
        }

        renderJson(jhm);
       // renderJson("{\"code\":1,\"message\":\"操作成功\"}");
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
        JsonHashMap jhm = new JsonHashMap();
        UserSessionUtil usu=new UserSessionUtil(getRequest());
        /**
         * 接收前台参数
         */
        //职务id
        String jobId = getPara("jobId");
        //职务名称
        String jobName = getPara("jobName");
        //职务描述
        String jobDesc = getPara("jobDesc");
        //职务权限
        String jobPermission = getPara("jobPermission");
        JSONArray jobPermissionList = JSONArray.fromObject(jobPermission);

        //非空验证
        if(StringUtils.isEmpty(jobId)){
            jhm.putCode(0).putMessage("职务id为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(jobName)){
            jhm.putCode(0).putMessage("职务名称为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(jobDesc)){
            jhm.putCode(0).putMessage("职务描述为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(jobPermission)){
            jhm.putCode(0).putMessage("职务权限为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(jobId)){
            jhm.putCode(0).putMessage("系统用户id为空!");
            renderJson(jhm);
            return;
        }
        if(jobPermissionList != null && jobPermissionList.size() > 0){
            jhm.putCode(0).putMessage("职务权限为空!");
            renderJson(jhm);
            return;
        }
        List lists = new ArrayList<>();
        for (int i = 0; i < jobPermissionList.size(); i++) {
            lists.add(i,jobPermissionList.getJSONObject(i).get("jobPermission"));
        }
        //通过循环，将json数组中key为words的数据添加到list集合中
        String jobPermissionStr = lists.toString();
        try{
            //根据系统用户id，查找对应的姓名
            String sql = "select adname from w_admin where adid = ?";
            Record adminName = Db.findFirst(sql,usu.getUserId());

            Record modifyJob = new Record();
            modifyJob.set("id",jobId);
            modifyJob.set("name",jobName);
            modifyJob.set("desc",jobDesc);
            modifyJob.set("modify_time",DateTool.GetDateTime());
            modifyJob.set("modifier",usu.getUserId());
            modifyJob.set("modifier_name",adminName);

            boolean jobFlag = Db.update("h_job","id",modifyJob);
            if(jobFlag == false){
                jhm.putCode(0).putMessage("修改失败!");
                renderJson(jhm);
                return;
            }

            Record modifyJobMenu = new Record();
            modifyJobMenu.set("menu_id",jobPermissionStr);
            modifyJobMenu.set("job_id",jobId);
            boolean jobMenuFlag = Db.update("h_author_job_menu","id",modifyJobMenu);
            if(jobMenuFlag == false){
                jhm.putCode(0).putMessage("修改失败!");
                renderJson(jhm);
                return;
            }else {
                jhm.putCode(0).putMessage("修改成功!");
            }

        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(0).putMessage("服务器发生异常!");
        }
        renderJson(jhm);
        //srenderJson("{\"code\":1,\"message\":\"修改成功\"}");
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
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //职务id
        String jobId = getPara("jobId");
        //非空验证
        if(StringUtils.isEmpty(jobId)){
            jhm.putCode(0).putMessage("职务id为空!");
            renderJson(jhm);
            return;
        }
        try{
            String sql = "DELETE from h_job where id=?";
            int num = Db.delete(sql,jobId);
            if(num <= 0){
                jhm.putCode(0).putMessage("删除失败!");
                renderJson(jhm);
                return;
            }
            String sql1 = "DELETE from h_author_job_menu where job_id=?";
            int num1 = Db.delete(sql,jobId);
            if(num1 <= 0){
                jhm.putCode(0).putMessage("删除失败!");
                renderJson(jhm);
                return;
            }else {
                jhm.putMessage("删除成功!");
            }

        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(0).putMessage("服务器发生异常!");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"message\":\"删除成功\"}");
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
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //职务id
        String jobId = getPara("jobId");
        //非空验证
        if(StringUtils.isEmpty(jobId)){
            jhm.putCode(0).putMessage("职务id为空!");
            renderJson(jhm);
            return;
        }

        try{
            String sql = "SELECT hj.id,hj. name name, hj.`desc` 'desc' from h_job hj where hj.id = ? ";
            //根据id查询职务名称,职务描述，职务id
            Record showJob = Db.findFirst(sql,jobId);
            if(showJob == null){
                jhm.putCode(0).putMessage("查询失败!");
                renderJson(jhm);
                return;
            }
            //根据职务id查询权限
            String sql1 = "select menu_id menuList from h_author_job_menu where job_id = ?";
            Record showJobMenu = Db.findFirst(sql1,jobId);
            if(showJob != null){
                jhm.put("job",showJob);
                jhm.put("menuList",showJobMenu);
            }else{
                jhm.putCode(0).putMessage("查询失败!");
                renderJson(jhm);
                return;
            }

        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(0).putMessage("服务器发生异常!");
        }
        renderJson(jhm);
       // renderJson("{\"code\":1,\"job\":{\"name\":\"职务名称\",\"desc\":\"职务描述\",\"id\":\"职务id\"},\"menuList\":[\"5\",\"30\",\"6\",\"40\",\"8\",\"7\",\"43\",\"24\",\"11\",\"31\",\"21\",\"42\",\"29\",\"39\",\"38\"]}");
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
     * pageSize       String                 允许    每页限制的记录数
     * pageNumber     String                 允许    页码
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
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
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

        //分页查询，在job表查询职务列表id，职务名称。查询职位的员工数，根据用户名分组查询
        String select = "select id, name, ( select count(1) from w_admin where job_id = h_job.id ) staffCount, ( select GROUP_CONCAT(username) from w_admin ) staffsName  ";
        String sql = " from h_job ";
        try{
            /**
             * 查询职务列表页面
             */
            Page<Record> page = Db.paginate(pageNum, pageSize,select, sql);
            if(page != null && page.getList().size() > 0){
                jhm.putMessage("查询成功！");
                jhm.put("list",page);
            }else{
                jhm.putCode(0).putMessage("查询失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(0).putMessage("服务器发生异常!");
        }
        renderJson(jhm);
       // renderJson("{\"code\":\"1\",\"list\":[{\"name\":\"职务名称\",\"id\":\"职务id\",\"staffCount\":\"人数\",\"staffsName\":\"人员名字\"},{\"name\":\"职务名称\",\"id\":\"职务id\",\"staffCount\":\"人数\",\"staffsName\":\"人员名字\"}]}");
    }
}
