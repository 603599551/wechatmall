package com.jfinal.weixin;

import com.jfinal.Config;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import easy.util.DateTool;
import easy.util.UUIDTool;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import javax.servlet.http.Cookie;
import java.util.UUID;

public class HomeController extends Controller{

    //进入首页

    public void index(){
        String appId= PropKit.get("appId");
        String redirect= Config.DOMAIN+"/wx/home/getCode";
        String url=SnsAccessTokenApi.getAuthorizeURL(appId,redirect,true);
        redirect(url);
    }

    public void getCode(){
        String code=getPara("code");
        String appId= PropKit.get("appId");
        String appSecret= PropKit.get("appSecret");
        SnsAccessToken sat=SnsAccessTokenApi.getSnsAccessToken(appId,appSecret,code);
        String accessToken=sat.getAccessToken();
        String openId=sat.getOpenid();
        setSessionAttr("wx_code",code);
        setSessionAttr("wx_access_token",accessToken);
        setSessionAttr("wx_open_id",openId);

        System.out.println("HOME openId:"+openId);
//        System.out.println("ip:"+getRequest().getRemoteAddr());
//        System.out.println("jsessionid:"+getRequest().getSession().getId());

        /*增加客户信息*/
        JsonHashMap jhm=new JsonHashMap();

        String id=UUIDTool.getUUID();
        String time=DateTool.GetDateTime();

        try{
            Record rf=Db.findFirst("SELECT count(*) AS count FROM w_customer WHERE cwechat=?",openId);
            if (StringUtils.equals(rf.getStr("count"),"0")){
                int len=openId.length();
                Record r=new Record();
                r.set("cid",id);
                r.set("cgender","unknown");
                r.set("cphone","unknown");
                r.set("cwxName","unknown");
                r.set("ccreator_id","weChat");
                r.set("cmodifier_id","weChat");
                r.set("cname", "微信新用户"+openId.substring(len-5,len));
                r.set("cwechat", openId);
                r.set("cgid","0fa26d8989954540855013d9659b0ba6");
                r.set("ctype","individual");
                r.set("ccreate_time",time );
                r.set("cmodify_time", time);
                boolean flag=Db.save("w_customer",r);
                if (flag){
                    jhm.putCode(1).putMessage("添加成功");
                }else{
                    jhm.putCode(0).putMessage("添加失败");
                }
            }else{
                Record r2=Db.findFirst("SELECT cid FROM w_customer WHERE cwechat=?",openId);
                id=r2.getStr("cid");
            }

        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常");
        }
        renderJson(jhm);
//        System.out.println("wx_open_id:::::HomeController::::::"+openId);
        redirect(Config.DOMAIN+PropKit.get("wx_home")+"?openId="+openId+"&userId="+id);



//        redirect(Config.DOMAIN+PropKit.get("wx_home"));

//         /*把openId和id存到cookie中*/
//        Cookie c1=new Cookie("openId",openId);
//        Cookie c2=new Cookie("userId",id);
//        c1.setMaxAge(24 * 60 * 60);
//        c2.setMaxAge(24 * 60 * 60);
//        getResponse().addCookie(c1);
//        getResponse().addCookie(c2);
    }


}
