package com.jfinal.weixin;

import com.jfinal.Config;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;

public class HomeController extends Controller{
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
        redirect(Config.DOMAIN+PropKit.get("wx_home"));
    }
}
