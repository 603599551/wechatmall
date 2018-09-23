package com.home.controllers;

import com.jfinal.core.Controller;
import com.utils.UserSessionUtil;

public class HomeCtrl extends Controller {
    public void index(){
        UserSessionUtil usu=new UserSessionUtil(getRequest());
        if(usu.isLogin()){
            redirect("/index.html");
        }else {
//            JsonHashMap jhm = new JsonHashMap();
//            jhm.put("code", "nosid");
//            renderJson(jhm);
            redirect("/index.html");
        }
    }
}
