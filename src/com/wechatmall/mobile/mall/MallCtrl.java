package com.wechatmall.mobile.mall;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import easy.util.DateTool;
import easy.util.UUIDTool;
import utils.bean.JsonHashMap;

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
     * URL	    http://localhost:8080/wcos/customer/showGoodsList
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
    public void showGoodsList(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前端参数
         */
        //用户id
        String userId = getPara("userId");

        //非空验证
        if (userId==null||userId.length()<=0){
            jhm.putCode(0).putMessage("客户id为空！");
            renderJson(jhm);
            return;
        }
        try{
            /**
             * 商品列表查询
             */



        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        //renderJson("{\"code\":1,\"goodsId\":\"11\",\"goodsList\":{\"人气热卖\":{\"name\":\"过水手擀面\",\"originalPrice\":\"20.0\",\"presentPrice\":\"18.0\",\"pic\":\"图片地址\",\"label\":[\"标签1\",\"标签2\"]},\"荤菜\":{\"name\":\"过水手擀面\",\"originalPrice\":\"20.0\",\"presentPrice\":\"18.0\",\"pic\":\"图片地址\",\"label\":[\"标签1\",\"标签2\"]}},\"notice\":\"我是公告\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	商品详细信息查询
     * 描述	    默认显示商品的所有信息
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/showCommodityDetails
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
        if (userId == null||userId.length() <= 0){
            jhm.putCode(0).putMessage("客户id为空！");
            renderJson(jhm);
            return;
        }

        //商品id非空验证
        if(goodsId == null||goodsId.length() <= 0) {
            jhm.putCode(0).putMessage("商品id为空！");
            renderJson(jhm);
            return;
        }

        try {
            /*
             *查询商品详细信息
             *查询w_product表中pname,pcost,price,picture,pintroduction,pdetail,pkeyword字段。
             */
            String sql = "select pname name,pcost originalPrice,price presentPrice,picture url,pintroduction desTit,pdetail detail,pkeyword types from w_product where pid = ?";
            Record viewGoods = Db.findFirst(sql,goodsId);
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
        //renderJson("{\"code\":1,\"goodsList\":{\"url\":\"http://pic34.photophoto.cn/20150113/0006019095934688_b.jpg\",\"desTit\":\"商品的描述\",\"originalPrice\":20,\"presentPrice\":18,\"name\":\"过水手擀面\",\"describe\":\"描述详情\",\"types\":[\"芥末味\",\"番茄味\"]}}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	商品列表点击加入购物车（不存在新建，已存在更新数量）
     * 描述     添加购物车  商品存在，更新数量。商品不存在，新建商品
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/modifyShoppingCart
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
        if (userId == null||userId.length() <= 0){
            jhm.putCode(0).putMessage("客户id为空！");
            renderJson(jhm);
            return;
        }

        //商品id非空验证
        if(goodsId == null||goodsId.length() <= 0) {
            jhm.putCode(0).putMessage("商品id为空！");
            renderJson(jhm);
            return;
        }

        //商品数量非空验证
        if(goodsNum == null || goodsNum.length() <=0){
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
        //renderJson("{\"code\":1,\"message\":\"提交成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	消息查询
     * 描述     显示公告，按时间倒序显示
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/queryMessage
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
        if(userId == null||userId.length() <=0) {
            jhm.putCode(0).putMessage("客户id为空");
            renderJson(jhm);
            return;
        }
        try {
            /*
            *消息查询
            * 从w_notice表中查询ncontent和nmodify_time字段
             */
            String sql = "select ncontent content,nmodify_time time from w_notice where ncreator_id = ?";
            List<Record> showHarvestMassageList = Db.find(sql,userId);
            if(showHarvestMassageList != null && showHarvestMassageList.size()>0) {
                jhm.put("notice",showHarvestMassageList);
            } else {
                jhm.putCode(0).putMessage("查询失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常!");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"notice\":[{\"content\":\"内容\",\"time\":\"2018-01-01\"},{\"content\":\"内容\",\"time\":\"2018-01-01\"}]}");
    }

}
