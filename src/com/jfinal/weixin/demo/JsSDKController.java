package com.jfinal.weixin.demo;

import com.jfinal.core.Controller;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.render.Render;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.JsTicket;
import com.jfinal.weixin.sdk.api.JsTicketApi;
import utils.bean.JsonHashMap;

import java.util.Map;
import java.util.TreeMap;

public class JsSDKController extends Controller {

    public void index(){
        JsonHashMap jhm=new JsonHashMap();
        String _wxShareUrl=getPara("_wxShareUrl");

        if (StrKit.notBlank(_wxShareUrl)) {
            _wxShareUrl = _wxShareUrl.split("#")[0];
        } else {
            jhm.putCode(0).putMessage("_wxShareUrl为空");
            renderJson(jhm);
            return;
        }
// 先从参数中获取，获取不到时从配置文件中找
        String appId = getRequest().getParameter("appId");
        if (StrKit.isBlank(appId)) {
            appId = PropKit.get("appId");
        }
// 方便测试 1.9添加参数&test=true
        String isTest = getRequest().getParameter("test");
        if (null == isTest || !isTest.equalsIgnoreCase("true")) {
            isTest = "false";
        }

        ApiConfigKit.setThreadLocalAppId(appId);
        String _wxJsApiTicket = "";
        try {
            JsTicket jsTicket = JsTicketApi.getTicket(JsTicketApi.JsApiType.jsapi);
            _wxJsApiTicket    = jsTicket.getTicket();
        } catch(Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        } finally {
            ApiConfigKit.removeThreadLocalAppId();
        }

        Map<String, String> _wxMap = new TreeMap<String, String>();
//noncestr
        String _wxNoncestr         = StrKit.getRandomUUID();
//timestamp
        String _wxTimestamp        = (System.currentTimeMillis() / 1000) + "";

//参数封装
        _wxMap.put("noncestr", _wxNoncestr);
        _wxMap.put("timestamp", _wxTimestamp );
        _wxMap.put("jsapi_ticket", _wxJsApiTicket);
        _wxMap.put("url", _wxShareUrl);
        _wxMap.put("isTest", isTest);
        _wxMap.put("appId", appId);

// 加密获取signature
        StringBuilder _wxBaseString = new StringBuilder();
        for (Map.Entry<String, String> param : _wxMap.entrySet()) {
            _wxBaseString.append(param.getKey()).append("=").append(param.getValue()).append("&");
        }
        String _wxSignString = _wxBaseString.substring(0, _wxBaseString.length() - 1);
// signature
        String _wxSignature = HashKit.sha1(_wxSignString);
        _wxMap.put("_wxSignature", _wxSignature);

        System.out.println("appId:"+appId);
        System.out.println("_wxTimestamp:"+_wxTimestamp);
        System.out.println("_wxNoncestr:"+_wxNoncestr);
        System.out.println("_wxJsApiTicket:"+_wxJsApiTicket);
        System.out.println("_wxShareUrl:"+_wxShareUrl);
        System.out.println("_wxSignature:"+_wxSignature);

        jhm.put("_wxMap",_wxMap);
        renderJson(jhm);
    }
}
