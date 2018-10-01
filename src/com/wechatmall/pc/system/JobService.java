package com.wechatmall.pc.system;

import com.common.controllers.BaseCtrl;
import com.common.service.BaseService;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.utils.UserSessionUtil;
import easy.util.DateTool;
import easy.util.UUIDTool;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.Map;


public class JobService extends BaseService{
     /*
    增加事务
     */
    @Before(Tx.class)
    public JsonHashMap addJobSer(Map paraMap){
        JsonHashMap jhm=new JsonHashMap();

        String time = DateTool.GetDateTime();
        String uuid = UUIDTool.getUUID();
        //职务名称
        String jobName = (String) paraMap.get("jobName");
        //职务描述
        String jobDesc = (String) paraMap.get("jobDesc");
        //职务权限
        JSONArray jobPermission = (JSONArray) paraMap.get("jobPermissionList");
        String usu = (String) paraMap.get("userId");

        //根据系统用户id，查找对应的姓名
        String sql = "select username from w_admin where username = ?";
        Record adminNameRecord = Db.findFirst(sql,usu);

        String adminName = adminNameRecord.get("username");
        //判断新增的职务是否重复
        String sql1 = "select count(1) from h_job where name=?";
        int count = Db.queryInt(sql1,jobName);
        if(count > 0){
            jhm.putCode(0).putMessage("职务名称重复!");
            return jhm;
        }
        Record jobRecord = new Record();
        jobRecord.set("id", uuid);
        jobRecord.set("name",jobName);
        jobRecord.set("desc",jobDesc);
        jobRecord.set("create_time", time);
        jobRecord.set("creator",usu);
        jobRecord.set("creator_name",adminName);
        jobRecord.set("modify_time",time);
        jobRecord.set("modifier",usu);
        jobRecord.set("modifier_name",adminName);
        //在职务表里添加数据
        boolean jobFlag = Db.save("h_job",jobRecord);
            if(jobFlag == false){
                jhm.putCode(0).putMessage("操作失败!");
                return jhm;
            }
        for (int i = 0; i < jobPermission.size(); i++) {
            Record jobMenuRecord = new Record();
            jobMenuRecord.set("id",UUIDTool.getUUID());
            jobMenuRecord.set("menu_id",jobPermission.get(i));
            jobMenuRecord.set("job_id",jobRecord.get("id"));
            jobMenuRecord.set("access","1");
            jobMenuRecord.set("creator",usu);
            jobMenuRecord.set("creator_name",adminName);
            jobMenuRecord.set("create_time",time);
            //在职务权限表里添加数据
            Db.save("h_author_job_menu",jobMenuRecord);
        }
        jhm.putCode(1).putMessage("操作成功！");
        return  jhm;
    }

    @Before(Tx.class)
    public JsonHashMap modifyJobSer(Map paraMap){
        JsonHashMap jhm=new JsonHashMap();
        String time = DateTool.GetDateTime();

        //职务id
        String jobId = (String) paraMap.get("jobId");
        //职务名称
        String jobName = (String) paraMap.get("jobName");
        //职务描述
        String jobDesc = (String) paraMap.get("jobDesc");
        //职务权限
        JSONArray jobPermission = (JSONArray) paraMap.get("jobPermissionList");
        String usu = (String) paraMap.get("userId");
        //根据系统用户id，查找对应的姓名
        String sql = "select username from w_admin where username = ?";
        Record adminNameRecord = Db.findFirst(sql,usu);

        String adminName = adminNameRecord.get("username");

        //判断新增的职务是否重复
        String sql1 = "select count(1) from h_job where name=?";
        int count = Db.queryInt(sql1,jobName);
        if(count > 0){
            jhm.putCode(0).putMessage("职务名称重复!");
            return jhm;
        }

        Record modifyJob = new Record();
        modifyJob.set("id",jobId);
        modifyJob.set("name",jobName);
        modifyJob.set("desc",jobDesc);
        modifyJob.set("modify_time",time);
        modifyJob.set("modifier",usu);
        modifyJob.set("modifier_name",adminName);

        boolean jobFlag = Db.update("h_job","id",modifyJob);
        if(jobFlag == false){
            jhm.putCode(0).putMessage("修改失败!");
            return jhm;
        }

        //先将原有的h_author_job_menu中的数据删除
        int sqlNum= Db.delete("delete from h_author_job_menu where job_id=?",jobId);
        if(sqlNum <= 0){
            jhm.putCode(0).putMessage("修改失败!");
            return jhm;
        }
        for (int i = 0; i < jobPermission.size(); i++) {
            Record jobMenuRecord = new Record();
            jobMenuRecord.set("id",UUIDTool.getUUID());
            jobMenuRecord.set("menu_id",jobPermission.get(i));
            jobMenuRecord.set("job_id",jobId);
            jobMenuRecord.set("access","1");
            jobMenuRecord.set("creator",usu);
            jobMenuRecord.set("creator_name",adminName);
            jobMenuRecord.set("create_time",time);
            //在职务权限表里添加数据
            Db.save("h_author_job_menu",jobMenuRecord);
        }
        jhm.putCode(1).putMessage("操作成功！");
        return jhm;
    }


}
