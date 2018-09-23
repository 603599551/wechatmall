package com.wechatmall.mobile.my;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import easy.util.DateTool;
import easy.util.UUIDTool;
import utils.bean.JsonHashMap;

import java.util.List;

public class MyCtrl extends BaseCtrl {

    public void addHarvestAddress(){
        JsonHashMap jhm=new JsonHashMap();

        /**
         * 接收前端参数
         */
        //客户id
        String userId=getPara("userId");
        //收货人姓名
        String name=getPara("name");
        //联系电话
        String phone=getPara("phone");
        //自提点所在省
        String province=getPara("province");
        //市
        String city=getPara("city");
        //区
        String district=getPara("district");
        //街道
        String street=getPara("street");
        //详细地址
        String address=getPara("address");
        //默认状态 0：不是 1：是
        String isDefault=getPara("isDefault");


        //非空验证1
        if (userId==null||userId.length()<=0){
            jhm.putCode(0).putMessage("客户id为空！");
            renderJson(jhm);
            return;
        }
        if (name==null||name.length()<=0){
            jhm.putCode(0).putMessage("收货人姓名为空！");
            renderJson(jhm);
            return;
        }
        if (phone==null||phone.length()<=0){
            jhm.putCode(0).putMessage("联系电话为空！");
            renderJson(jhm);
            return;
        }
        if (province==null||province.length()<=0){
            jhm.putCode(0).putMessage("所在省为空！");
            renderJson(jhm);
            return;
        }
        if (city==null||city.length()<=0){
            jhm.putCode(0).putMessage("所在市为空！");
            renderJson(jhm);
            return;
        }
        if (district==null||district.length()<=0){
            jhm.putCode(0).putMessage("所在区为空！");
            renderJson(jhm);
            return;
        }
        if (street==null||street.length()<=0){
            jhm.putCode(0).putMessage("所在街道为空！");
            renderJson(jhm);
            return;
        }
        if (address==null||address.length()<=0){
            jhm.putCode(0).putMessage("详细地址为空！");
            renderJson(jhm);
            return;
        }
        if (isDefault==null||isDefault.length()<=0){
            jhm.putCode(0).putMessage("默认状态为空！");
            renderJson(jhm);
            return;
        }

        try{

            /**
             * 增加的收货地址记录
             */
            Record record=new Record();
            record.set("caid", UUIDTool.getUUID());
            record.set("cid", userId);
            record.set("sid", "");
            record.set("caname", name);
            record.set("caphone", phone);
            record.set("castatus", isDefault);
            record.set("caprovince", province);
            record.set("cacity", city);
            record.set("cadistrict", district);
            record.set("castreet", street);
            record.set("caaddress", address);
            record.set("cacreate_time", DateTool.GetDateTime());
            record.set("camodify_time", DateTool.GetDateTime());
            record.set("cacreator_id", userId);
            record.set("camodifier_id", userId);
            record.set("cadesc", "");

            boolean flag=Db.save("w_customer_address",record);
            if (flag) {
                jhm.putCode(1).putMessage("新增成功！");
            } else {
                jhm.putCode(0).putMessage("新增失败！");
            }


        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }

        renderJson(jhm);
    }

    public void showHarvestAddress(){
        JsonHashMap jhm = new JsonHashMap();
        String userId = getPara("userId");
        String sql = "select caname name,caphone phone,caaddress address from w_customer_address  where cid = ?";
        try {
            List<Record> showHarvestAddressList = Db.find(sql,userId);
            jhm.put("list",showHarvestAddressList);
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常!");
        }

        renderJson(jhm);
        // renderJson("{\"code\":1,\"list\":[{\"address\":\"地址\",\"name\":\"小明\",\"phone\":13130005589},{\"address\":\"地址\",\"name\":\"小米\",\"phone\":13130005589}]}");
    }

}
