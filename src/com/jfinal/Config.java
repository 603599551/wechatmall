package com.jfinal;

import com.common.controllers.DictionaryCtrl;
import com.common.controllers.JobCtrl;
import com.home.controllers.HomeCtrl;
import com.home.controllers.LoginCtrl;
import com.home.controllers.MenuCtrl;
import com.home.controllers.UserCtrl;
import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.TxByMethodRegex;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import easy.util.FileUploadPath;
import utils.DictionaryConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class Config extends JFinalConfig {

	public static boolean devMode=false;
	/**
	 *
	 */
	public static File web_inf_path=null;
	@Override
	public void configConstant(Constants constants) {
		String path=Thread.currentThread().getContextClassLoader().getResource("/").getPath();
		web_inf_path=new File(path).getParentFile();

		loadPropertyFile("config.txt");
		devMode=getPropertyToBoolean("devMode", false);
		constants.setDevMode(devMode);
		constants.setEncoding("utf-8");
		constants.setViewType(ViewType.JSP);
//		arg0.setError404View("/white.jsp");
//		arg0.setError500View("/500.jsp");
	}

	@Override
	public void configRoute(Routes routes) {
		routes.add("/",HomeCtrl.class);
		routes.add("/login", LoginCtrl.class);
		routes.add("/mgr/menu",MenuCtrl.class);
		routes.add("/mgr/user", UserCtrl.class);
		routes.add("/mgr/job", JobCtrl.class);

		routes.add("/mgr/dict", DictionaryCtrl.class);

	}

	@Override
	public void configEngine(Engine engine) {

	}

	@Override
	public void configPlugin(Plugins plugins) {
		String databaseURL=getProperty("jdbcUrl");
		String databaseUser=getProperty("username");
		String databasePassword=getProperty("password").trim();

		Integer initialPoolSize = getPropertyToInt("initialPoolSize");
		Integer minIdle = getPropertyToInt("minIdle");
		Integer maxActivee = getPropertyToInt("maxActivee");

		DruidPlugin druidPlugin = new DruidPlugin(databaseURL,databaseUser,databasePassword);
		druidPlugin.set(initialPoolSize,minIdle,maxActivee);
		druidPlugin.setFilters("stat,wall");
		plugins.add(druidPlugin);

		//实体映射
		ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
//        activeRecordPlugin.addMapping("news","id", News.class);
		plugins.add(activeRecordPlugin);

		// ehcache缓存插件
		plugins.add(new EhCachePlugin());
	}

	@Override
	public void configInterceptor(Interceptors interceptors) {
		// 给service增加事务控制，过滤方法名为save*，update*，delete*
		interceptors.addGlobalServiceInterceptor(new TxByMethodRegex("(save.*|update.*|delete.*)"));
	}

	@Override
	public void configHandler(Handlers handlers) {

	}
	@Override
	public void afterJFinalStart() {
		// TODO Auto-generated method stub
		super.afterJFinalStart();
		FileUploadPath.me().init();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDate = sdf.format(new Date());
		System.out.println("当前时间："+sDate);
		loadDictionary();
	}

	private void loadDictionary(){
		List<Record> dictList = Db.find("select * from h_dictionary order by sort");
		Map<String, List<Record>> dictMap = new HashMap<>();
		Map<String, String> dictIdValueMap = new HashMap<>();
		if(dictList != null && dictList.size() > 0){
			for(Record r : dictList){
				List<Record> list = dictMap.computeIfAbsent(r.getStr("parent_id"), k -> new ArrayList<>());
				list.add(r);
				dictIdValueMap.put(r.getStr("id"), r.getStr("value"));
			}
		}
		if(dictMap != null && dictMap.size() > 0){
		    for(String key : dictMap.keySet()){
		        if("0".equals(key)){
		            continue;
                }
		        List<Record> list = dictMap.get(key);
		        String dict_key = dictIdValueMap.get(key);
		        Map<String, String> stringMap = DictionaryConstants.DICT_STRING_MAP.computeIfAbsent(dict_key, k -> new HashMap<>());
		        Map<String, Record> recordMap = DictionaryConstants.DICT_RECORD_MAP.computeIfAbsent(dict_key, k -> new HashMap<>());
		        if(list != null && list.size() > 0){
		            for(Record r : list){
		                stringMap.put(r.getStr("value"), r.getStr("name"));
                        recordMap.put(r.getStr("value"), r);
                    }
                }
            }
        }
	}
}
