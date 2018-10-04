package com.wechatmall.mobile.mall;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import easy.util.DateTool;
import easy.util.UUIDTool;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * customerCtrl class
 * @author liushiwen
 * @date   2018-9-22
 */

public class MallCtrl extends BaseCtrl {
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	商城商品列表查询
     * 描述	    默认显示商城商品的所有信息
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/mobile/mall/showGoodsList
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	string		            不允许	 用户的id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code": 1,
        "goodsId":"11",
        "goodsList": {
        "人气热卖": {
        "name": "过水手擀面",
        "originalPrice": "20.0",
        "presentPrice": "18.0",
        "pic": "图片地址",
        "label": ["标签1", "标签2"]
        },
        "荤菜": {
        "name": "过水手擀面",
        "originalPrice": "20.0",
        "presentPrice": "18.0",
        "pic": "图片地址",
        "label": ["标签1", "标签2"]
        }
        },
        "notice": "我是公告"
        }
     * 失败：
     * {
        "code": 0,
        "message": "商品获取失败！"
        }

     * 报错：
     * {
        "code": -1,
        "message": "服务器发生异常！"
         * }
     */
    public void showGoodsList() {
        JsonHashMap jhm = new JsonHashMap();

        String userId = getPara("userId");

        JsonHashMap goodsList = new JsonHashMap();

        //进行非空验证
        if(StringUtils.isEmpty(userId)){
            jhm.putCode(0).putMessage("id不能为空！");
            renderJson(jhm);
            return;
        }

        try {
            String sql = "SELECT wp.pid AS goodsId,wp.pname as name,ww.pcname,wpc.pcpcurrent_price AS presentPrice,wp.price AS originalPrice,wp.picture AS pic,wp.pkeyword AS keyword from w_customer wc,w_customer_group wcg,w_product_currentprice wpc,w_product wp,w_product_category ww WHERE ww.pcid = wp.pcid AND wc.cgid=wcg.cgid AND wcg.cgid=wpc.cgid AND wpc.pid = wp.pid AND wc.cid= ? AND wp.pstatus='on_sale' ORDER BY ww.pcname ";
            List<Record> recordList = Db.find(sql, userId);

            //分类名为key，插入数据到value里返回前台
            String[] classify = new String[recordList.size()];
            int i = 0 , t = 0;
            List<Record> resultList = new ArrayList<>();
            for(Record r : recordList){
                r.set("label", r.getStr("keyword").split(","));
                r.remove("keyword");
                t++;
                if(!StringUtils.isEmpty(classify[i]) && StringUtils.equals(classify[i],r.getStr("pcname")) && t != recordList.size()){  //上一段与本条数据分类名相同
                    resultList.add(r);
                } else if((!StringUtils.isEmpty(classify[i]) && !StringUtils.equals(classify[i],r.getStr("pcname"))) && t!=recordList.size()){  //上一条数据与本条数据分类名不同
                    goodsList.put(classify[i], resultList);
                    i++;
                    classify[i] = r.getStr("pcname");
                    resultList = new ArrayList<>();
                    resultList.add(r);
                } else if (t==recordList.size()){                      //最后一条数据
                    if(StringUtils.equals(classify[i],r.getStr("pcname"))){   //与上一条数据分类名相同
                        resultList.add(r);
                        goodsList.put(classify[i], resultList);
                    } else {
                        goodsList.put(classify[i], resultList);
                        i++;
                        resultList = new ArrayList<>();
                        resultList.add(r);
                        classify[i] = r.getStr("pcname");
                        goodsList.put(classify[i], resultList);
                    }
                } else if(StringUtils.isEmpty(classify[0])){   //className为空字符串的时候
                    classify[i] = r.getStr("pcname");
                    resultList.add(r);
                }
            }

            i = 0;
            for (String s : classify){
                if(!StringUtils.isEmpty(s)){
                    i++;
                }
            }
            String[] resultClass = new String[i];

            for(int j = 0; j < resultClass.length; j++){
                resultClass[j] = classify[j];
            }

            jhm.putCode(1);
            jhm.put("classify", resultClass);
            jhm.put("goodsList", goodsList);

            /**
             //             *根据w_notice查询:最新公告：ncontent
             //             */
            String noticeSql = "SELECT ncontent from w_notice ORDER BY nmodify_time desc LIMIT 1";
            Record record = Db.findFirst(noticeSql);
            if (record==null){
                jhm.put("notice", "");
            } else {
                jhm.put("notice", record.getStr("ncontent"));
            }
        } catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage(e.toString());
        }
        renderJson(jhm);
    }




//    public void showGoodsList() {
//        JsonHashMap jhm = new JsonHashMap();
//        /**
//         * 接收前端参数
//         */
//        //用户id
//        String userId = getPara("userId");
//
//
//        //非空验证
//        if (StringUtils.isEmpty(userId)){
//            jhm.putCode(0).putMessage("客户id为空！");
//            renderJson(jhm);
//            return;
//        }
//        try{
//            //分类名字
//            List<String> classify = new ArrayList<String>();
//
//            /**
//             * 根据w_product_currentprice, w_customer 和 w_customer_group三表关联查询：商品id：goodsId, 商品名称name,
//             * 商品原价:originalPrice, 商品现价:presentPrice, 商品标签:label, 客户类型:customerType
//             */
//            String sql = "SELECT wp.pid as goodsId, wp.pname as name, pc.pcname ,FORMAT(wp.price,2)AS originalPrice,FORMAT(w.pcpcurrent_price,2)as presentPrice, wp.picture as pic, wp.pkeyword, w.ctype from w_product_category pc, w_product wp, (SELECT pid, wpc.cgid, ctype, wpc.pcpcurrent_price from (SELECT pid, cgid, pcpcurrent_price FROM w_product_currentprice)wpc ,(SELECT cgid, ctype FROM w_customer WHERE cid = ?)wc WHERE wpc.cgid = wc.cgid)w where w.pid = wp.pid and pc.pcid = wp.pcid and wp.pstatus='on_sale' ORDER BY pcname ";
//            List<Record> recordList = Db.find(sql, userId);
//            if(recordList.size() == 0){
//                jhm.putCode(0).putMessage("商品为空！");
//                renderJson(jhm);
//                return;
//            }
//
//            /**
//             *根据w_notice查询:最新公告：ncontent
//             */
//            String noticeSql = "SELECT ncontent from w_notice ORDER BY nmodify_time desc";
//            Record record = Db.findFirst(noticeSql);
//            if (record==null){
//                jhm.put("notice", "");
//            } else {
//                jhm.put("notice", record.getStr("ncontent"));
//            }
//
//            //记录分类名称
//            String className = "";
//
//            //记录每一个分类的商品
//            List<Record>records = new ArrayList<Record>();
//
//            //商品标签
//            String[] label = new String [10];
//
//            jhm.put("customerType", recordList.get(0).getStr("ctype"));
//
//            //对查出来的商品数据按照分类进行分类并放入jsonHashMap
//            for(int i = 0; i < recordList.size(); i++ ){
//                className = recordList.get(i).getStr("pcname");
//                label = recordList.get(i).getStr("pkeyword").split(",");
//                recordList.get(i).set("label",label);
//                recordList.get(i).remove("pkeyword");
//                recordList.get(i).remove("ctype");
//                recordList.get(i).remove("pcname");
//                records.add(recordList.get(i));
//                if(i != recordList.size()-1 && !StringUtils.equals(recordList.get(i+1).getStr("pcname"), className)){
//                    jhm.put(className,records);
//                    classify.add(className);
//                    records.clear();
//                } else if(i == recordList.size() -1){
//                    jhm.put(className,records);
//                    classify.add(className);
//                }
//            }
//            jhm.put("classify", classify);
//            jhm.putCode(1);
//
//        }catch (Exception e){
//            e.printStackTrace();
//            jhm.putCode(-1).putMessage("服务器发生异常！");
//        }
//        renderJson(jhm);
////        renderJson("{\"code\":1,\"customerType\":\"客户类型\",\"classify\":[\"人气热卖\",\"荤菜\"],\"人气热卖\":[{\"goodsId\":\"11\",\"name\":\"过水手擀面\",\"originalPrice\":\"20.0\",\"presentPrice\":\"18.0\",\"pic\":\"http://a4.att.hudong.com/12/20/20300000929429131579208829151.jpg\",\"label\":[\"标签1\",\"标签2\"]},{\"goodsId\":\"11\",\"name\":\"过水手擀面\",\"originalPrice\":\"20.0\",\"presentPrice\":\"18.0\",\"pic\":\"http://a4.att.hudong.com/12/20/20300000929429131579208829151.jpg\",\"label\":[\"标签1\",\"标签2\"]}],\"荤菜\":[{\"goodsId\":\"11\",\"name\":\"过水手擀面\",\"originalPrice\":\"20.0\",\"presentPrice\":\"18.0\",\"pic\":\"http://a4.att.hudong.com/12/20/20300000929429131579208829151.jpg\",\"label\":[\"标签1\",\"标签2\"]}],\"notice\":\"我是公告\"}");
//    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	商品详细信息查询
     * 描述	    默认显示商品的所有信息
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/mobile/mall/showCommodityDetails
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	 string		            不允许	 用户的id
     * goodsId   string                 不允许     商品的id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code": 1,
        "goodsList": {
        "url": "图片的地址",
        "desTit": "商品的描述",
        "originalPrice": 20.0,
        "presentPrice": 18.0,
        "name": "过水手擀面",
        "detail": "描述详情",
        "types": ["芥末味", "番茄味"]
        }
    }
     * 失败：
     * {
    "code": 0,
    "message": "查询失败！"
    }

     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void showCommodityDetails(){
        JsonHashMap jhm=new JsonHashMap();
        /**
         * 接收前端参数
         */

        //客户id
        String userId  = getPara("userId");

        //商品id
        String goodsId = getPara("goodsId");


        //用户id非空验证
        if (StringUtils.isEmpty(userId)){
            jhm.putCode(0).putMessage("客户id为空！");
            renderJson(jhm);
            return;
        }

        //商品id非空验证
        if(StringUtils.isEmpty(goodsId)) {
            jhm.putCode(0).putMessage("商品id为空！");
            renderJson(jhm);
            return;
        }

        try {
            /*
             *查询商品详细信息
             *查询w_product表中pname,pcost,price,picture,pintroduction,pdetail,pkeyword字段。
             */
            String sql = "select wp.pname name,FORMAT(wp.price,2)AS originalPrice,FORMAT(wpc.pcpcurrent_price,2)AS presentPrice, wp.picture url, wp.pintroduction desTit, wp.pdetail detail, wp.pkeyword types from w_product wp, w_product_currentprice wpc where wp.pid = wpc.pid and wpc.cgid = ( select cgid from w_customer where cid = ? ) and wp.pid = ?";
            Record viewGoods = Db.findFirst(sql,userId,goodsId);
            if (viewGoods != null){
                String []types=viewGoods.getStr("types").split(",");
                viewGoods.set("types",types);
                jhm.put("goodsList",viewGoods);
            } else {
                jhm.putCode(0).putMessage("查询失败!");
            }
        } catch(Exception e) {
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
//        renderJson("{\"code\":1,\"goodsList\":{\"url\":\"http://pic34.photophoto.cn/20150113/0006019095934688_b.jpg\",\"desTit\":\"商品的描述\",\"originalPrice\":20,\"presentPrice\":18,\"name\":\"过水手擀面\",\"describe\":\"描述详情\",\"types\":[\"芥末味\",\"番茄味\"]}}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	商品列表点击加入购物车（不存在新建，已存在更新数量）
     * 描述     添加购物车  商品存在，更新数量。商品不存在，新建商品
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/mobile/mall/modifyShoppingCart
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	 string		            不允许	 用户的id
     * goodsId   string                 不允许     商品的id
     * goodsNum  string                 不允许     商品的数量
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code": 1,
        "message": "提交成功！"
    }
     * 失败：
     * {
        "code": 0,
        "message": "修改失败！"
    }

     * 报错：
     * {
        "code": -1,
        "message": "服务器发生异常！"
     * }
     */
    public void modifyShoppingCart(){
        JsonHashMap jhm=new JsonHashMap();
        /**
         * 接收前端参数
         */

        //客户id
        String userId  = getPara("userId");

        //商品id
        String goodsId = getPara("goodsId");

        //商品数量
        String goodsNum = getPara("goodsNum");

        //用户id非空验证
        if (StringUtils.isEmpty(userId)){
            jhm.putCode(0).putMessage("客户id为空！");
            renderJson(jhm);
            return;
        }

        //商品id非空验证
        if(StringUtils.isEmpty(goodsId)) {
            jhm.putCode(0).putMessage("商品id为空！");
            renderJson(jhm);
            return;
        }

        //商品数量非空验证
        if(StringUtils.isEmpty(goodsNum)){
            jhm.putCode(0).putMessage("商品数量为空！");
            renderJson(jhm);
            return;
        }

        try{
            /**
             *商品列表点击加入购物车
             * 根据商品id和用户id修改时间，id，数量
             */
            String modifyTime = DateTool.GetDateTime();
            int flag = Db.update("update w_shoppingcart set scquantity = ?, scmodifier = ? ,scmodify_time = ? where pid=? and cid=?",goodsNum,userId,modifyTime,goodsId,userId);
            if(flag>0){
                jhm.putMessage("修改成功！");
            }else {
                jhm.putCode(0).putMessage("修改失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
//        renderJson("{\"code\":1,\"message\":\"提交成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	消息查询
     * 描述     显示公告，按时间倒序显示
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/mobile/mall/queryMessage
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	 string		            不允许	 用户的id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code": 1,
        "notice":[{
        "content":"内容",
        "time":"2018-01-01"
        },{
        "content":"内容",
        "time":"2018-01-01"
        }]
    }
     * 失败：
     * {
        "code": 0,
        "message": "查询失败！"
    }

     * 报错：
     * {
        "code": -1,
        "message": "服务器发生异常！"
     * }
     */
    public void queryMessage(){
        JsonHashMap jhm = new JsonHashMap();
        /**
        * 接收前端参数
        */
        //用户的id
        String userId = getPara("userId");
        //非空验证
        if(StringUtils.isEmpty(userId)) {
            jhm.putCode(0).putMessage("客户id为空");
            renderJson(jhm);
            return;
        }
        try {
            /*
            *消息查询
            * 从w_notice表中查询ncontent和nmodify_time字段
             */
            String sql = "select ncontent content,nmodify_time time from w_notice ORDER BY nmodify_time desc";
            List <Record> recordList = Db.find(sql);
            jhm.putCode(1).put("notice",recordList);
        } catch (Exception e) {
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常!");
        }
        renderJson(jhm);
//        renderJson("{\"code\":1,\"notice\":[{\"content\":\"内容\",\"time\":\"2018-01-01\"},{\"content\":\"内容\",\"time\":\"2018-01-01\"}]}");
    }

}
