package com.home.controllers;

import com.common.controllers.BaseCtrl;
import com.jfinal.KEY;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.utils.UserSessionUtil;
import utils.bean.JsonHashMap;
import utils.jfinal.RecordUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * 当前用户操作
 */
public class UserCtrl extends BaseCtrl {


    /**
     * 修改自己密码
     */
    public void modifyMyPwd(){
        JsonHashMap jhm=new JsonHashMap();
        try{
            //JSONObject json= RequestTool.getJson(getRequest());
//            String currentPwd=getPara("currentPwd");
            String confirmPwd=getPara("confirmPwd");
//            if(currentPwd==null || "".equalsIgnoreCase(currentPwd)){
//                jhm.putCode(-1);
//                jhm.putMessage("请输入原密码！");
//                renderJson(jhm);
//                return;
//            }
            if(confirmPwd==null || "".equalsIgnoreCase(confirmPwd)){
                jhm.putCode(-1);
                jhm.putMessage("请输入新密码！");
                renderJson(jhm);
                return;
            }
            UserSessionUtil usu=new UserSessionUtil(getRequest());
//            Record r= Db.findFirst("select * from w_admin where id=? and password=?",usu.getUserId(),currentPwd);
//            if(r!=null){
                int sqlNum= Db.update("update w_admin set password=? where id=? ",confirmPwd,usu.getUserId());
                if(sqlNum>0){
                    jhm.putCode(1);
                    jhm.putMessage("更新成功！");
                }else{
                    jhm.putCode(-1);
                    jhm.putMessage("更新失败！");
                }
//            }else{
//                jhm.putCode(-1);
//                jhm.putMessage("密码错误！");
//            }
        }catch(Exception e){
            e.printStackTrace();

            jhm.putCode(-1);
            jhm.putMessage(e.toString());
        }
        renderJson(jhm);
    }
    public void showMyDetail(){
        JsonHashMap jhm = new JsonHashMap();
        UserSessionUtil usu = new UserSessionUtil(getRequest());
        if(!usu.isLogin()){
            jhm.putCode(-1);
            jhm.putMessage("请先登录！");
            renderJson(jhm);
            return ;
        }
        try{
            Record r= Db.findFirst("select s.*,case gender when 1 then '男' when 0 then '女' end as gender_text,(select name from h_dictionary where id=s.status) as status_text,(select name from h_job where id=s.job) as job_text,dept.name as dept_text from h_staff s  left join (select id,name from h_store ) as dept on s.dept_id=dept.id where s.id=?",usu.getUserId());
            r.remove("password");
            RecordUtils.obj2str(r);
            jhm.putCode(1).put("data",r);
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage(e.toString());
        }
        renderJson(jhm);
    }
    public void getUserInfo(){
        JsonHashMap jhm = new JsonHashMap();
        try {
            UserSessionUtil usu = new UserSessionUtil(getRequest());
            if(!usu.isLogin()){
                jhm.putCode(-1);
                jhm.putMessage("请先登录！");
                renderJson(jhm);
                return ;
            }
            jhm.put("id",usu.getUserId());
            jhm.put("name",usu.getRealName());
            jhm.put("deptId",usu.getUserBean().getDeptId());
            jhm.put("type","1");
            jhm.put("job",usu.getUserBean().getJobId());
            jhm.putCode(1);
            jhm.putMessage("");
        }catch(Exception e){
            e.printStackTrace();
            jhm.putCode(-1);
            jhm.putMessage(e.toString());

        }
        renderJson(jhm);
    }

    public void loginout(){
        JsonHashMap jhm=new JsonHashMap();
        try {
            HttpSession session = getSession();
            session.removeAttribute(KEY.SESSION_USER);
            session.removeAttribute(KEY.SESSION_ADMIN);
            session.invalidate();
            session = null;
            //清空cookie
            Cookie cookies[] = getCookieObjects();
            for(int i=0;i<cookies.length;i++){
                cookies[i].setMaxAge(0);
                setCookie(cookies[i]);
            }
            jhm.putCode(1);
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage(e.toString());
        }
        renderJson(jhm);
    }
}
