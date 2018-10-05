package com.wechatmall.pc.system;

import com.common.controllers.BaseCtrl;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.utils.UserSessionUtil;
import easy.util.DateTool;
import easy.util.NumberUtils;
import easy.util.UUIDTool;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/system/job/addJob
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

        UserSessionUtil usu=new UserSessionUtil(getRequest());

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
        if(jobPermissionList == null && jobPermissionList.size() <= 0){
            jhm.putCode(0).putMessage("职务权限为空!");
            renderJson(jhm);
            return;
        }

        try{
            String userId=usu.getUserId();
            Map paraMap =new HashMap();
            paraMap.put("userId", userId);
            paraMap.put("jobName", jobName);
            paraMap.put("jobDesc", jobDesc);
            paraMap.put("jobPermissionList", jobPermissionList);
            JobService srv = enhance(JobService.class);
            jhm = srv.addJobSer(paraMap);
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record发生异常!");
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
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/system/job/modifyJobById
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
        String time = DateTool.GetDateTime();
        String uuid = UUIDTool.getUUID();
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
        if(StringUtils.isEmpty(jobId)){
            jhm.putCode(0).putMessage("系统用户id为空!");
            renderJson(jhm);
            return;
        }
        if(jobPermissionList == null && jobPermissionList.size() <= 0){
            jhm.putCode(0).putMessage("职务权限为空!");
            renderJson(jhm);
            return;
        }
        try{
            String userId=usu.getUserId();
            Map paraMap =new HashMap();
            paraMap.put("userId", userId);
            paraMap.put("jobId",jobId);
            paraMap.put("jobName", jobName);
            paraMap.put("jobDesc", jobDesc);
            paraMap.put("jobPermissionList", jobPermissionList);
            JobService srv = enhance(JobService.class);
            jhm = srv.modifyJobSer(paraMap);
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常!");
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
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/system/job/deleteJobById
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
            String sql = "delete hj ,hjm  from h_job hj left join h_author_job_menu hjm on hj.id = hjm.job_id where hj.id = ? ";
            int num = Db.delete(sql,jobId);
            if(num <= 0){
                jhm.putCode(0).putMessage("删除失败!");
                renderJson(jhm);
                return;
            }else{
                jhm.putMessage("删除成功!");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常!");
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
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/system/job/showJobById
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
            List<Record> showJobMenu = Db.find(sql1,jobId);
            String[] menuArray = new String[showJobMenu.size()];
            for(int i = 0; i < showJobMenu.size(); i++){
                menuArray[i] = showJobMenu.get(i).get("menuList");
            }
            showJob.set("menuList",menuArray);

            if(showJob != null){
                jhm.put("job",showJob);
            }else{
                jhm.putCode(0).putMessage("查询失败!");
                renderJson(jhm);
                return;
            }

        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常!");
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
     * URL	   http://localhost:8080/weChatMallMgr/wm/pc/system/job/showJobList
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
        String pageNumStr = getPara("pageNumber");
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
        String select = "select id, name, ( select count(1) from w_admin where job_id = h_job.id ) staffCount, ( select GROUP_CONCAT(username) from w_admin where job_id = h_job.id ) staffsName  ";
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
            jhm.putCode(-1).putMessage("服务器发生异常!");
        }
        renderJson(jhm);
       // renderJson("{\"code\":\"1\",\"list\":[{\"name\":\"职务名称\",\"id\":\"职务id\",\"staffCount\":\"人数\",\"staffsName\":\"人员名字\"},{\"name\":\"职务名称\",\"id\":\"职务id\",\"staffCount\":\"人数\",\"staffsName\":\"人员名字\"}]}");
    }

    /**
     * URL   http://localhost:8080/weChatMallMgr/wm/pc/system/job/showJobs
     * */
    public void showJobs(){
        JsonHashMap jhm = new JsonHashMap();

        String sql = " SELECT id AS value,name FROM h_job ";
        try{
            List<Record> list=Db.find(sql);
            jhm.putCode(1).putMessage("查询成功");
            jhm.put("list",list);

        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常!");
        }
        renderJson(jhm);
    }

}
