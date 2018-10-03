package com.wechatmall.mobile.order;

import com.alibaba.fastjson.JSONObject;
import com.common.service.BaseService;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import easy.util.DateTool;
import easy.util.UUIDTool;
import net.sf.json.JSONArray;
import utils.bean.JsonHashMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class OrderService extends BaseService {

    public static String getOrderIdByTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate=sdf.format(new Date());
        String result="";
        Random random=new Random();
        for(int i=0;i<3;i++){
            result+=random.nextInt(10);
        }
        return newDate+result;
    }


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
        String goodsString = (String)paraMap.get("goodsString");
        String receivingMethod = (String) paraMap.get("receivingMethod");
        String payMethod = (String) paraMap.get("payMethod");
        String orderOriginalSumStr = (String) paraMap.get("orderOriginalSumStr");
        String orderCurrentSumStr = (String) paraMap.get("orderCurrentSumStr");

        JSONArray goodsStringList = JSONArray.fromObject(goodsString);
        //订单原总价
        float orderOriginalSum = Float.valueOf(orderOriginalSumStr);
        //订单现总价
        float orderCurrentSum = Float.valueOf(orderCurrentSumStr);
        //订单编号=时间+随机数
        String orderNum=getOrderIdByTime();


        /**
         * 新增订单
         */
        //订单信息表对象
        Record w_orderform = new Record();
        w_orderform.set("oid", UUIDTool.getUUID());
        w_orderform.set("onum", orderNum);
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
        //获取商品  goodsStringList 里面是{{id,name..}{id,name..}{id,name..}}
        for(int i = 0; i < goodsStringList.size(); i++){
            //遍历json数组，转换成json对象
            Record product = Db.findFirst(sql,goodsStringList.getJSONObject(i).getString("goodsId"));
            w_orderform_detail.set("odid", UUIDTool.getUUID());
            w_orderform_detail.set("oid",w_orderform.get("oid"));
            w_orderform_detail.set("pid",goodsStringList.getJSONObject(i).getString("goodsId"));
            w_orderform_detail.set("odname",goodsStringList.getJSONObject(i).getString("goodsName"));
            w_orderform_detail.set("odoriginal_price",product.get("odoriginal_price"));
            w_orderform_detail.set("odcurrent_price",goodsStringList.getJSONObject(i).getString("goodsPresentPrice"));
            w_orderform_detail.set("odquantity",goodsStringList.getJSONObject(i).getString("goodsNum"));
            w_orderform_detail.set("odkeyword",product.get("odkeyword"));
            Float sum =   Float.valueOf(goodsStringList.getJSONObject(i).getString("goodsPresentPrice"))  * Float.valueOf(goodsStringList.getJSONObject(i).getString("goodsPresentPrice"));
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
        jhm.putCode(1).putMessage("提交成功！");

        return jhm;
    }
}
