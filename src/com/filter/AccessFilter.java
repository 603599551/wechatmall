package com.filter;

import com.alibaba.fastjson.JSONObject;
import com.utils.UserSessionUtil;
import easy.web.UrlKit;
import utils.bean.JsonHashMap;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mym
 * 访问过滤器，有记录日志的功能，所有访问者，都会记录需要在sys_conf表中配置
 * access_log=true，开启访问日志
 * access_log_max_record，日志的最大记录数，默认为2万条
 */
public class AccessFilter implements Filter{

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse resp=(HttpServletResponse)response;
		resp.setContentType("text/html;charset=UTF-8");

//		String ip=request.getRemoteAddr();
//		System.out.println(getClass()+"::"+ip);
		resp.setHeader("Access-Control-Allow-Origin", "*");
//		resp.setHeader("Access-Control-Allow-Origin", "http://192.168.1.102:8080");
		String domain=UrlKit.getDomain(req);
		req.setAttribute("domain",domain);

//		boolean isLogin=isLong4Hrms(req,resp);//处理自动登录
//		if(isLogin){
//			chain.doFilter(request, response);
//		}
		//将请求转发给过滤器链的下一个Filter,如果没有Filter则是请求的资源。
		//如果不写这一句，请求将会被卡在这里。
		chain.doFilter(request, response);
	}

	private static final String[] STATIC_SUCCESS_RESOURCES = {"/static","/login","/index","/mobile"};

	private boolean isLong4Hrms(HttpServletRequest req,HttpServletResponse resp){
		String servletPath = req.getServletPath().toLowerCase();
		UserSessionUtil usu = new UserSessionUtil(req);

		if("admin".equals(usu.getUsername())){
			return true;
		}else{
			for(String s : STATIC_SUCCESS_RESOURCES){
				if(servletPath.startsWith(s)){
					return true;
				}
			}
		}

		if(servletPath.startsWith("/mgr/mobile/") ){
			if(usu.getUserBean()==null){
				JsonHashMap jhm = new JsonHashMap();
				jhm.putCode(0).putMessage("登录超时，请重新登录！");
				try {
					resp.getWriter().write(JSONObject.toJSONString(jhm));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}else{
				return true;
			}
		}else{
			if(usu.getUserBean()==null){
				JsonHashMap jhm = new JsonHashMap();
				jhm.put("code", "nosid");
				try {
					resp.getWriter().write(JSONObject.toJSONString(jhm));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}else{
				return true;
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
