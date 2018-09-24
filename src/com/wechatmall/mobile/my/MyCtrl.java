package com.wechatmall.mobile.my;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import easy.util.DateTool;
import easy.util.UUIDTool;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.List;


/**
 * customerCtrl class
 * @author liushiwen
 * @date   2018-9-22
 */
public class MyCtrl extends BaseCtrl {
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查看联系人
     * 描述	    显示所有联系人，默认联系人显示在第一行
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/viewContact
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
        "contacts":[{
        "name":"名字",
        "phone":1313000589,
        "isDefault":0
        },{
        "name":"名字",
        "phone":1313000589,
        "isDefault":0
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
    public void viewContact(){
        JsonHashMap jhm=new JsonHashMap();
        /**
         * 接收前端参数
         */
        //客户id
        String userId  = getPara("userId");

        //非空验证
        if (StringUtils.isEmpty(userId)){
            jhm.putCode(0).putMessage("客户id为空！");
            renderJson(jhm);
            return;
        }

        try {
            /**
             * 查询联系人
             */
            String sql = "select caname name,caphone phone,castatus isDefault from w_customer_address where cid =  ?";
            List<Record> viewContactList = Db.find(sql,userId);
            if(viewContactList != null && viewContactList.size() > 0){
                jhm.put("contacts",viewContactList);
            }else{
                jhm.putCode(0).putMessage("查询失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"contacts\":[{\"name\":\"名字\",\"phone\":1313000589,\"isDefault\":0},{\"name\":\"名字\",\"phone\":1313000589,\"isDefault\":0}]}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	增加收货地址
     * 描述	    新增的信息不能重复
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/addHarvestAddress
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	string		            不允许	 用户的id
     * province   string                不允许   省
     * name     string                  不允许   收获姓名
     * phone    string                  不允许   收获手机号
     * isDefult   int                   不允许   是否设置为默认地址
     * city       string                不允许   市
     * district   string                不允许   区
     * street     string                不允许   街道
     * address  string                  不允许   详细地址
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":1,
        "message":"新增成功！"
    }
     * 失败：
     * {
        "code": 0,
        "message": "新增失败！"
    }

     * 报错：
     * {
        "code": -1,
        "message": "服务器发生异常！"
     * }
     */
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

        //非空验证
        if (StringUtils.isEmpty(userId)){
            jhm.putCode(0).putMessage("客户id为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(name)){
            jhm.putCode(0).putMessage("收货人姓名为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(phone)){
            jhm.putCode(0).putMessage("联系电话为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(province)){
            jhm.putCode(0).putMessage("所在省为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(city)){
            jhm.putCode(0).putMessage("所在市为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(district)){
            jhm.putCode(0).putMessage("所在区为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(street)){
            jhm.putCode(0).putMessage("所在街道为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(address)){
            jhm.putCode(0).putMessage("详细地址为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(isDefault)){
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
       // renderJson("{\"code\":1,\"message\":\"新增成功！\"}");
    }


    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	删除收货地址
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/deleteHarvestAddress
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	string		            不允许	 用户的id
     * listId  string                  不允许    记录的id
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
    public void deleteHarvestAddress(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前端参数
         */
        //客户的id
        String userId = getPara("userId");
        //记录的id（收货地址id）
        String listId = getPara("listId");
        //非空验证
        if (StringUtils.isEmpty(userId)){
            jhm.putCode(0).putMessage("客户id为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(listId)){
            jhm.putCode(0).putMessage("记录id为空！");
            renderJson(jhm);
            return;
        }
        try{
            /**
             * 删除收货地址记录
             */
            String sql = "DELETE from w_customer_address where (caid = ? and cid = ?)";
            int num = Db.delete(sql,userId,listId);
            if(num > 0){
                jhm.putCode(1).putMessage("删除成功！");
            }else{
                jhm.putCode(0).putMessage("删除失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
       // renderJson("{\"code\":1,\"message\":\"删除成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查看收货地址
     * 描述     显示收货地址
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/showHarvestAddress
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	string		            不允许	 用户的id
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":1,
        "list":[{
        "address":"地址",
        "name":"小明",
        "phone":13130005589
        },{
        "address":"地址",
        "name":"小米",
        "phone":13130005589
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
    public void showHarvestAddress(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前端参数
         */
        //客户id
        String userId = getPara("userId");
        //非空验证
        if (StringUtils.isEmpty(userId)){
            jhm.putCode(0).putMessage("客户id为空！");
            renderJson(jhm);
            return;
        }
        try {
            /**
             * 查看收货地址
             */
            String sql = "select caname name,caphone phone,caaddress address from w_customer_address  where cid = ?";
            List<Record>  showHarvestAddressList = Db.find(sql,userId);
            if(showHarvestAddressList != null && showHarvestAddressList.size() > 0){
                jhm.put("list",showHarvestAddressList);
            }else{
                jhm.putCode(0).putMessage("查询失败！");
            }
        }catch (Exception e){
                e.printStackTrace();
                jhm.putCode(-1).putMessage("服务器发生异常!");
            }
        renderJson(jhm);
      // renderJson("{\"code\":1,\"list\":[{\"address\":\"地址\",\"name\":\"小明\",\"phone\":13130005589},{\"address\":\"地址\",\"name\":\"小米\",\"phone\":13130005589}]}");
    }

}
