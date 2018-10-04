package com.jfinal;

import com.common.controllers.DictionaryCtrl;
import com.common.controllers.JobCtrl;
import com.common.service.DictionaryService;
import com.home.controllers.HomeCtrl;
import com.home.controllers.LoginCtrl;
import com.home.controllers.MenuCtrl;
import com.home.controllers.UserCtrl;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.tx.TxByMethodRegex;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.jfinal.weixin.demo.*;
import com.wechatmall.mobile.mall.MallCtrl;
import com.wechatmall.mobile.my.MyCtrl;
import com.wechatmall.mobile.shoppingcart.ShoppingCartCtrl;
import com.wechatmall.pc.customer.CustomerGroupCtrl;
import com.wechatmall.pc.customer.CustomerInfoCtrl;
import com.wechatmall.pc.finance.FinanceCtrl;
import com.wechatmall.pc.notice.NoticeCtrl;
import com.wechatmall.pc.price.PriceCtrl;
import com.wechatmall.pc.product.ProductCategoryCtrl;
import com.wechatmall.pc.product.ProductManageCtrl;
import com.wechatmall.pc.system.PayCtrl;
import com.wechatmall.pc.system.StoreCtrl;
import com.wechatmall.pc.system.TransportCtrl;
import easy.util.FileUploadPath;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.HomeController;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import org.apache.commons.lang.StringUtils;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class Config extends JFinalConfig {

	/*微信配置*/
	// 本地开发模式
	private boolean isLocalDev = false;
	public static String DOMAIN="";
	//商户相关资料
	public static String appid = "";
	public static String partner = "";
	public static String paternerKey = "";

	/**
	 * 如果生产环境配置文件存在，则优先加载该配置，否则加载开发环境配置文件
	 * @param pro 生产环境配置文件
	 * @param dev 开发环境配置文件
	 */
	public void loadProp(String pro, String dev) {
		try {
			PropKit.use(pro);
		}
		catch (Exception e) {
			PropKit.use(dev);
			isLocalDev = true;
		}
	}


	public static boolean devMode=false;
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


		loadProp("a_little_config_pro.txt", "a_little_config.txt");
//		constants.setDevMode(PropKit.getBoolean("devMode", false));

		/*微信配置*/
		// ApiConfigKit 设为开发模式可以在开发阶段输出请求交互的 xml 与 json 数据
		ApiConfigKit.setDevMode(constants.getDevMode());
	}

	@Override
	public void configRoute(Routes routes) {
		routes.add("/",HomeCtrl.class);
		routes.add("/login", LoginCtrl.class);
		routes.add("/mgr/menu",MenuCtrl.class);
		routes.add("/mgr/user", UserCtrl.class);
		routes.add("/mgr/job", JobCtrl.class);
		routes.add("/mgr/dict", DictionaryCtrl.class);
		routes.add("/wm/mobile/my", MyCtrl.class);
		routes.add("/wm/mobile/mall", MallCtrl.class);
		routes.add("/wm/mobile/order", com.wechatmall.mobile.order.OrderCtrl.class);
		routes.add("/wm/mobile/shoppingcart", ShoppingCartCtrl.class);
		routes.add("/wm/pc/customer/group", CustomerGroupCtrl.class);
		routes.add("/wm/pc/customer/info", CustomerInfoCtrl.class);
		routes.add("/wm/pc/notice", NoticeCtrl.class);
		routes.add("/wm/pc/order", com.wechatmall.pc.order.OrderCtrl.class);
		routes.add("/wm/pc/order/print", com.wechatmall.pc.order.PrintCtrl.class);
		routes.add("/wm/pc/price", PriceCtrl.class);
		routes.add("/wm/pc/product/category", ProductCategoryCtrl.class);
		routes.add("/wm/pc/product/manage", ProductManageCtrl.class);
		routes.add("/wm/pc/system/job", com.wechatmall.pc.system.JobCtrl.class);
		routes.add("/wm/pc/system/pay", PayCtrl.class);
		routes.add("/wm/pc/system/store", StoreCtrl.class);
		routes.add("/wm/pc/system/transport", TransportCtrl.class);
		routes.add("/wm/pc/system/user", com.wechatmall.pc.system.UserCtrl.class);
		routes.add("/wm/pc/finance", FinanceCtrl.class);

		/*微信配置*/
		routes.add("/msg", WeixinMsgController.class);
		routes.add("/api", WeixinApiController.class, "/api");
		routes.add("/pay", WeixinPayController.class);
		routes.add("/wxa/user", WxaUserApiController.class);
		routes.add("/subscribemsg", SubscribeMsgController.class);
		routes.add("/sdk", JsSDKController.class);
		routes.add("/wx/home", HomeController.class);

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
		DictionaryService.loadDictionary();
		//loadDictionary();
		/*初始化微信*/
		wxInit();
	}

	/**
	 * 初始化微信
	 */
	public void wxInit(){

		DOMAIN=PropKit.get("domain");
		appid=PropKit.get("appId");
		partner=PropKit.get("mch_id");
		paternerKey=PropKit.get("paternerKey");

		if(StringUtils.isBlank(DOMAIN)){
			System.out.println("请在配置文件中设置domain！");
		}
		if(StringUtils.isBlank(appid)){
			System.out.println("请在配置文件中设置appid！");
		}
		if(StringUtils.isBlank(partner)){
			System.out.println("请在配置文件中设置partner！");
		}
		if(StringUtils.isBlank(paternerKey)){
			System.out.println("请在配置文件中设置paternerKey！");
		}

		// 1.5 之后支持redis存储access_token、js_ticket，需要先启动RedisPlugin
//        ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache());
		// 1.6新增的2种初始化
//        ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache(Redis.use("weixin")));
//        ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache("weixin"));

		ApiConfig ac = new ApiConfig();
		// 配置微信 API 相关参数
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appId"));
		ac.setAppSecret(PropKit.get("appSecret"));



		/**
		 *  是否对消息进行加密，对应于微信平台的消息加解密方式：
		 *  1：true进行加密且必须配置 encodingAesKey
		 *  2：false采用明文模式，同时也支持混合模式
		 */
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));

		/**
		 * 多个公众号时，重复调用ApiConfigKit.putApiConfig(ac)依次添加即可，第一个添加的是默认。
		 */
		ApiConfigKit.putApiConfig(ac);

		/**
		 * 1.9 新增LocalTestTokenCache用于本地和线上同时使用一套appId时避免本地将线上AccessToken冲掉
		 *
		 * 设计初衷：https://www.oschina.net/question/2702126_2237352
		 *
		 * 注意：
		 * 1. 上线时应保证此处isLocalDev为false，或者注释掉该不分代码！
		 *
		 * 2. 为了安全起见，此处可以自己添加密钥之类的参数，例如：
		 * http://localhost/weixin/api/getToken?secret=xxxx
		 * 然后在WeixinApiController#getToken()方法中判断secret
		 *
		 * @see WeixinApiController#getToken()
		 */
//        if (isLocalDev) {
//            String onLineTokenUrl = "http://localhost/weixin/api/getToken";
//            ApiConfigKit.setAccessTokenCache(new LocalTestTokenCache(onLineTokenUrl));
//        }
        /*
        微信小程序
         */
//        WxaConfig wc = new WxaConfig();
//        wc.setAppId("wx4f53594f9a6b3dcb");
//        wc.setAppSecret("eec6482ba3804df05bd10895bace0579");
//        WxaConfigKit.setWxaConfig(wc);

//        Timer timer=new Timer();
//        TimerTask tt=new TimerTask() {
//            @Override
//            public void run() {
//                AccessTokenApi.
//            }
//        };
	}

	public static void main(String []args){
		JFinal.start("src/main/webapp", 80, "/", 5);
	}
//	private void loadDictionary(){
//		List<Record> dictList = Db.find("select * from w_dictionary order by sort");
//		Map<String, List<Record>> dictMap = new HashMap<>();
//		Map<String, String> dictIdValueMap = new HashMap<>();
//		if(dictList != null && dictList.size() > 0){
//			for(Record r : dictList){
//				List<Record> list = dictMap.computeIfAbsent(r.getStr("parent_id"), k -> new ArrayList<>());
//				list.add(r);
//				dictIdValueMap.put(r.getStr("id"), r.getStr("value"));
//			}
//		}
//		if(dictMap != null && dictMap.size() > 0){
//		    for(String key : dictMap.keySet()){
//		        if("0".equals(key)){
//		            continue;
//                }
//                //根据key（parent_id），找到对应的list
//		        List<Record> list = dictMap.get(key);
//		        //根据parent_id找到一级的value
//		        String dict_key = dictIdValueMap.get(key);
//		        //一个key对应一个map，一个map可以存多个键值对
//		        Map<String, String> stringMap = DictionaryConstants.DICT_STRING_MAP.computeIfAbsent(dict_key, k -> new HashMap<>());
//		        Map<String, Record> recordMap = DictionaryConstants.DICT_RECORD_MAP.computeIfAbsent(dict_key, k -> new HashMap<>());
//		        if(list != null && list.size() > 0){
//		            for(Record r : list){
//		                stringMap.put(r.getStr("value"), r.getStr("name"));
//                        recordMap.put(r.getStr("value"), r);
//                    }
//                }
//            }
//        }
//        //把record_Map中的所有value（record），放到recordList中。
//        if(DictionaryConstants.DICT_RECORD_MAP != null && DictionaryConstants.DICT_RECORD_MAP.size() > 0){
//			for(String key : DictionaryConstants.DICT_RECORD_MAP.keySet()){
//				Map<String, Record> recordMap = DictionaryConstants.DICT_RECORD_MAP.get(key);
//				List<Record> recordList = new ArrayList<>();
//				for(String rKey : recordMap.keySet()){
//					recordList.add(recordMap.get(rKey));
//				}
//				DictionaryConstants.DICT_RECORD_LIST.put(key, recordList);
//			}
//		}
//
//	}
}


//package com.jfinal;
//
//import com.common.controllers.DictionaryCtrl;
//import com.common.controllers.JobCtrl;
//import com.common.service.DictionaryService;
//import com.home.controllers.HomeCtrl;
//import com.home.controllers.LoginCtrl;
//import com.home.controllers.MenuCtrl;
//import com.home.controllers.UserCtrl;
//import com.jfinal.config.*;
//import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
//import com.jfinal.plugin.activerecord.Db;
//import com.jfinal.plugin.activerecord.Record;
//import com.jfinal.plugin.activerecord.tx.TxByMethodRegex;
//import com.jfinal.plugin.druid.DruidPlugin;
//import com.jfinal.plugin.ehcache.EhCachePlugin;
//import com.jfinal.render.ViewType;
//import com.jfinal.template.Engine;
//import com.wechatmall.mobile.mall.MallCtrl;
//import com.wechatmall.mobile.my.MyCtrl;
//import com.wechatmall.mobile.order.OrderCtrl;
//import com.wechatmall.mobile.shoppingcart.ShoppingCartCtrl;
//import com.wechatmall.pc.customer.CustomerGroupCtrl;
//import com.wechatmall.pc.customer.CustomerInfoCtrl;
//import com.wechatmall.pc.finance.FinanceCtrl;
//import com.wechatmall.pc.notice.NoticeCtrl;
//import com.wechatmall.pc.price.PriceCtrl;
//import com.wechatmall.pc.product.ProductCategoryCtrl;
//import com.wechatmall.pc.product.ProductManageCtrl;
//import com.wechatmall.pc.system.PayCtrl;
//import com.wechatmall.pc.system.StoreCtrl;
//import com.wechatmall.pc.system.TransportCtrl;
//import easy.util.FileUploadPath;
//import utils.DictionaryConstants;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//public class Config extends JFinalConfig {
//
//	public static boolean devMode=false;
//	/**
//	 *
//	 */
//	public static File web_inf_path=null;
//	@Override
//	public void configConstant(Constants constants) {
//		String path=Thread.currentThread().getContextClassLoader().getResource("/").getPath();
//		web_inf_path=new File(path).getParentFile();
//
//		loadPropertyFile("config.txt");
//		devMode=getPropertyToBoolean("devMode", false);
//		constants.setDevMode(devMode);
//		constants.setEncoding("utf-8");
//		constants.setViewType(ViewType.JSP);
////		arg0.setError404View("/white.jsp");
////		arg0.setError500View("/500.jsp");
//	}
//
//	@Override
//	public void configRoute(Routes routes) {
//		routes.add("/",HomeCtrl.class);
//		routes.add("/login", LoginCtrl.class);
//		routes.add("/mgr/menu",MenuCtrl.class);
//		routes.add("/mgr/user", UserCtrl.class);
//		routes.add("/mgr/job", JobCtrl.class);
//		routes.add("/mgr/dict", DictionaryCtrl.class);
//		routes.add("/wm/mobile/my", MyCtrl.class);
//		routes.add("/wm/mobile/mall", MallCtrl.class);
//		routes.add("/wm/mobile/order", com.wechatmall.mobile.order.OrderCtrl.class);
//		routes.add("/wm/mobile/shoppingcart", ShoppingCartCtrl.class);
//		routes.add("/wm/pc/customer/group", CustomerGroupCtrl.class);
//		routes.add("/wm/pc/customer/info", CustomerInfoCtrl.class);
//		routes.add("/wm/pc/notice", NoticeCtrl.class);
//		routes.add("/wm/pc/order", com.wechatmall.pc.order.OrderCtrl.class);
//		routes.add("/wm/pc/order/print", com.wechatmall.pc.order.PrintCtrl.class);
//		routes.add("/wm/pc/price", PriceCtrl.class);
//		routes.add("/wm/pc/product/category", ProductCategoryCtrl.class);
//		routes.add("/wm/pc/product/manage", ProductManageCtrl.class);
//		routes.add("/wm/pc/system/job", com.wechatmall.pc.system.JobCtrl.class);
//		routes.add("/wm/pc/system/pay", PayCtrl.class);
//		routes.add("/wm/pc/system/store", StoreCtrl.class);
//		routes.add("/wm/pc/system/transport", TransportCtrl.class);
//		routes.add("/wm/pc/system/user", com.wechatmall.pc.system.UserCtrl.class);
//		routes.add("/wm/pc/finance", FinanceCtrl.class);
//	}
//
//	@Override
//	public void configEngine(Engine engine) {
//
//	}
//
//	@Override
//	public void configPlugin(Plugins plugins) {
//		String databaseURL=getProperty("jdbcUrl");
//		String databaseUser=getProperty("username");
//		String databasePassword=getProperty("password").trim();
//
//		Integer initialPoolSize = getPropertyToInt("initialPoolSize");
//		Integer minIdle = getPropertyToInt("minIdle");
//		Integer maxActivee = getPropertyToInt("maxActivee");
//
//		DruidPlugin druidPlugin = new DruidPlugin(databaseURL,databaseUser,databasePassword);
//		druidPlugin.set(initialPoolSize,minIdle,maxActivee);
//		druidPlugin.setFilters("stat,wall");
//		plugins.add(druidPlugin);
//
//		//实体映射
//		ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
////        activeRecordPlugin.addMapping("news","id", News.class);
//		plugins.add(activeRecordPlugin);
//
//		// ehcache缓存插件
//		plugins.add(new EhCachePlugin());
//	}
//
//	@Override
//	public void configInterceptor(Interceptors interceptors) {
//		// 给service增加事务控制，过滤方法名为save*，update*，delete*
//		interceptors.addGlobalServiceInterceptor(new TxByMethodRegex("(save.*|update.*|delete.*)"));
//	}
//
//	@Override
//	public void configHandler(Handlers handlers) {
//
//	}
//	@Override
//	public void afterJFinalStart() {
//		// TODO Auto-generated method stub
//		super.afterJFinalStart();
//		FileUploadPath.me().init();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String sDate = sdf.format(new Date());
//		System.out.println("当前时间："+sDate);
//		DictionaryService.loadDictionary();
//		//loadDictionary();
//	}
//
////	private void loadDictionary(){
////		List<Record> dictList = Db.find("select * from w_dictionary order by sort");
////		Map<String, List<Record>> dictMap = new HashMap<>();
////		Map<String, String> dictIdValueMap = new HashMap<>();
////		if(dictList != null && dictList.size() > 0){
////			for(Record r : dictList){
////				List<Record> list = dictMap.computeIfAbsent(r.getStr("parent_id"), k -> new ArrayList<>());
////				list.add(r);
////				dictIdValueMap.put(r.getStr("id"), r.getStr("value"));
////			}
////		}
////		if(dictMap != null && dictMap.size() > 0){
////		    for(String key : dictMap.keySet()){
////		        if("0".equals(key)){
////		            continue;
////                }
////                //根据key（parent_id），找到对应的list
////		        List<Record> list = dictMap.get(key);
////		        //根据parent_id找到一级的value
////		        String dict_key = dictIdValueMap.get(key);
////		        //一个key对应一个map，一个map可以存多个键值对
////		        Map<String, String> stringMap = DictionaryConstants.DICT_STRING_MAP.computeIfAbsent(dict_key, k -> new HashMap<>());
////		        Map<String, Record> recordMap = DictionaryConstants.DICT_RECORD_MAP.computeIfAbsent(dict_key, k -> new HashMap<>());
////		        if(list != null && list.size() > 0){
////		            for(Record r : list){
////		                stringMap.put(r.getStr("value"), r.getStr("name"));
////                        recordMap.put(r.getStr("value"), r);
////                    }
////                }
////            }
////        }
////        //把record_Map中的所有value（record），放到recordList中。
////        if(DictionaryConstants.DICT_RECORD_MAP != null && DictionaryConstants.DICT_RECORD_MAP.size() > 0){
////			for(String key : DictionaryConstants.DICT_RECORD_MAP.keySet()){
////				Map<String, Record> recordMap = DictionaryConstants.DICT_RECORD_MAP.get(key);
////				List<Record> recordList = new ArrayList<>();
////				for(String rKey : recordMap.keySet()){
////					recordList.add(recordMap.get(rKey));
////				}
////				DictionaryConstants.DICT_RECORD_LIST.put(key, recordList);
////			}
////		}
////
////	}
//}
