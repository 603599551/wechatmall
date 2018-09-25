package com.wechatmall.mobile.shoppingcart;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.List;

/**
 * customerCtrl class
 * @author liushiwen
 * @date   2018-9-22
 */
public class ShoppingCartCtrl extends BaseCtrl {

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	购物车商品列表查询
     * 描述     显示购物车的商品
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/shoppingCartList
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
    "code":1,
    "goodsList":[
    {
    "goodsId":"11",
    "url":"图片的地址",
    "originalPrice":20,
    "presentPrice":18,
    "name":"过水手擀面",
    "types":[
    "芥末味",
    "番茄味"
    ],
    "number":1
    },
    {
    "goodsId":"11",
    "url":"图片的地址",
    "originalPrice":20,
    "presentPrice":18,
    "name":"过水手擀面",
    "types":[
    "芥末味",
    "番茄味"
    ],
    "number":2
    }
    ]
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
    public void shoppingCartList(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前端参数
         */
        //用户id
        String userId = getPara("userId");

        //非空验证
        if (StringUtils.isEmpty(userId)){
            jhm.putCode(0).putMessage("客户id为空！");
            renderJson(jhm);
            return;
        }
        try{
            /**
             * 查询购物车商品列表
             */
            String sql = "SELECT wpr.pid goodsId,wpr.picture url,wpr.price originalPrice,wpc.pcpcurrent_price presentPrice,wpr.pname name,wpr.pkeyword types,whs.scquantity number,wpr.pstatus status,wcu.ctype ctype from w_customer wcu,w_product wpr,w_shoppingcart whs,w_product_currentprice wpc where (wcu.cid = whs.cid and whs.pid = wpr.pid and wpr.pid = wpc.pid and wcu.cid = ?)";
            List<Record> shoppingCartList = Db.find(sql,userId);
            if(shoppingCartList != null && shoppingCartList.size() > 0){
                String ctype = null;
                for(Record r: shoppingCartList){
                    String typesStr = r.get("types");
                    ctype = r.get("ctype");
                    r.remove("ctype");
                    String[] types = typesStr.split(",");
                    r.set("types",types);
                }
                jhm.put("ctype",ctype);
                jhm.put("goodsList",shoppingCartList);
            }else{
                jhm.putCode(0).putMessage("查询失败!");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"goodsList\":[{\"goodsId\":\"11\",\"url\":\"http://img0.imgtn.bdimg.com/it/u=3405249702,185540826&fm=27&gp=0.jpg\",\"originalPrice\":20,\"presentPrice\":18,\"name\":\"过水手擀面\",\"types\":[\"芥末味\",\"番茄味\"],\"number\":1},{\"goodsId\":\"11\",\"url\":\"http://img0.imgtn.bdimg.com/it/u=3405249702,185540826&fm=27&gp=0.jpg\",\"originalPrice\":20,\"presentPrice\":18,\"name\":\"过水手擀面\",\"types\":[\"芥末味\",\"番茄味\"],\"number\":2}]}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	购物车商品-删除
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/deleteGoods
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	string		            不允许	 用户的id
     *listId    string                  不允许   商品的id
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"删除成功！"
    }
     * 失败：
     * {
    "code": 0,
    "message": "删除失败！"
    }

     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void deleteGoods(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前端参数
         */
        //用户id
        String userId = getPara("userId");
        //商品id
        String listId = getPara("listId");

        //非空验证
        if (StringUtils.isEmpty(userId)){
            jhm.putCode(0).putMessage("客户id为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(listId)){
            jhm.putCode(0).putMessage("商品id为空！");
            renderJson(jhm);
            return;
        }
        try{
            /**
             * 删除购物车商品
             */
            String sql = "DELETE from w_shoppingcart where (pid = ? and cid = ?)";
            int num = Db.update(sql,userId,listId);
            if(num > 0){
                jhm.putCode(1).putMessage("删除成功!");
            }else{
                jhm.putCode(1).putMessage("删除失败!");
            }
        }catch(Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        // renderJson("{\"code\":1,\"message\":\"删除成功！\"}");
    }

}
