package com.wechatmall.mobile.order;

import com.common.service.BaseService;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import easy.util.DateTool;
import easy.util.UUIDTool;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import utils.bean.JsonHashMap;

import java.util.List;
import java.util.Map;

public class OrderService extends BaseService {
    /*
    增加事务
     */
    @Before(Tx.class)
    public JsonHashMap placeOrder(Map paraMap){
        JsonHashMap jhm=new JsonHashMap();

        String userId = (String) paraMap.get("userId");
        String address = (String) paraMap.get("address");
        String name = (String) paraMap.get("name");
        String phone = (String) paraMap.get("phone");
        String goodsString = (String) paraMap.get("goodsString");
        String receivingMethod = (String) paraMap.get("receivingMethod");
        String payMethod = (String) paraMap.get("payMethod");
        String orderOriginalSumStr = (String) paraMap.get("orderOriginalSumStr");
        String orderCurrentSumStr = (String) paraMap.get("orderCurrentSumStr");

        JSONArray goodsList = JSONArray.fromObject(goodsString);
        //订单原总价
        float orderOriginalSum = Float.valueOf("orderOriginalSum");
        //订单现总价
        float orderCurrentSum = Float.valueOf("orderCurrentSum");

        /**
         * 新增订单
         */
        //订单信息表对象
        Record w_orderform = new Record();
        w_orderform.set("oid", UUIDTool.getUUID());
        w_orderform.set("caid","");
        w_orderform.set("cid",userId);
        w_orderform.set("oname",name);
        w_orderform.set("ophone",phone);
        w_orderform.set("oaddress",address);
        w_orderform.set("ooriginal_sum",orderOriginalSum);
        w_orderform.set("ocurrent_sum",orderCurrentSum);
        w_orderform.set("ostatus","pending_pay");
        w_orderform.set("otransport_type",receivingMethod);
        w_orderform.set("opay_type",payMethod);
        w_orderform.set("ocreate_time", DateTool.GetDateTime());
        w_orderform.set("omodify_time", DateTool.GetDateTime());
        w_orderform.set("ocreator_id",userId);
        w_orderform.set("omodifier_id",userId);
        w_orderform.set("odesc","");
        boolean orderFormFlag = Db.save("w_orderform","oid",w_orderform);
        if(orderFormFlag == false){
            jhm.putCode(0).putMessage("提交失败!");
            return jhm;
        }
        /**
         * 新增订单详情
         */
        //订单详情表对象
        Record w_orderform_detail = new Record();
        //定义新增订单后的布尔值
        boolean orderFormDetailFlag = false;

        String sql = "select pkeyword odkeyword,price odoriginal_price from w_product where pid=?";
        //获取商品
        for(int i = 0; i < goodsList.size(); i++){
            //遍历json数组，转换成json对象
            JSONObject goodsListJSON = goodsList.getJSONObject(i);
            Record product = Db.findFirst(sql,goodsListJSON.get("id"));
            w_orderform_detail.set("odid", UUIDTool.getUUID());
            w_orderform_detail.set("oid",w_orderform.get("oid"));
            w_orderform_detail.set("pid",goodsListJSON.get("id"));
            w_orderform_detail.set("odname",goodsListJSON.get("name"));
            w_orderform_detail.set("odoriginal_price",product.get("odoriginal_price"));
            w_orderform_detail.set("odcurrent_price", goodsListJSON.get("price"));
            w_orderform_detail.set("odquantity",goodsListJSON.get("number"));
            w_orderform_detail.set("odkeyword",product.get("odkeyword"));
            Double sum =  (Double) goodsListJSON.get("price") * (Integer) goodsListJSON.get("number");
            w_orderform_detail.set("odsingle_sum",sum);
            w_orderform_detail.set("odcreate_time",DateTool.GetDateTime());
            w_orderform_detail.set("odmodify_time",DateTool.GetDateTime());
            w_orderform_detail.set("odcreator_id",userId);
            w_orderform_detail.set("odmodifier_id",userId);
            w_orderform_detail.set("oddesc","");
            orderFormDetailFlag = Db.save("w_orderform_detail","odid",w_orderform_detail);
            if(orderFormDetailFlag == false){
                jhm.putCode(0).putMessage("提交失败！");
                return jhm;
            }
        }
        jhm.putMessage("提交成功！");

        return jhm;
    }
}
