package com.wechatmall.mobile.my;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import easy.util.DateTool;
import easy.util.UUIDTool;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.List;
import java.util.Map;


/**
 * customerCtrl class
 * @author liushiwen
 * @date   2018-9-22
 */
public class MyCtrl extends BaseCtrl {
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查看客户信息
     * 描述	    显示客户的详细信息
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/queryInfo
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
    "name":"小明",
    "phone":131355589,
    "sex":"男",
    "type":1
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
    public void queryInfo(){
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
             * 查询客户信息
             */
            String sql = "select cname name, cphone phone, ( select name from w_dictionary where parent_id = 900 and value = cgender ) sex, ctype type from w_customer where cid = ? ";
            Record queryInfoList = Db.findFirst(sql,userId);
            if(queryInfoList != null ){
                String type = queryInfoList.get("type");
                if(type.equals("merchant")){
                    queryInfoList.set("type",1);
                }else{
                    queryInfoList.set("type",0);
                }
                jhm.put("name",queryInfoList.get("name"));
                jhm.put("phone",queryInfoList.get("phone"));
                jhm.put("sex",queryInfoList.get("sex"));
                jhm.put("type",queryInfoList.get("type"));
            }else{
                jhm.putCode(0).putMessage("查询失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
//        renderJson("{\"code\":1,\"name\":\"小明\",\"phone\":131355589,\"sex\":\"男\",\"type\":1}");
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
        Map<String, String[]> paramsMaps = getParaMap();
        //Map<String, String> paramsMap = MethodCommon.converMapArrayToMapStr(paramsMaps);
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
//        if (StringUtils.isEmpty(province)){
//            jhm.putCode(0).putMessage("所在省为空！");
//            renderJson(jhm);
//            return;
//        }
//        if (StringUtils.isEmpty(city)){
//            jhm.putCode(0).putMessage("所在市为空！");
//            renderJson(jhm);
//            return;
//        }
//        if (StringUtils.isEmpty(district)){
//            jhm.putCode(0).putMessage("所在区为空！");
//            renderJson(jhm);
//            return;
//        }
//        if (StringUtils.isEmpty(street)){
//            jhm.putCode(0).putMessage("所在街道为空！");
//            renderJson(jhm);
//            return;
//        }
//        if (StringUtils.isEmpty(address)){
//            jhm.putCode(0).putMessage("详细地址为空！");
//            renderJson(jhm);
//            return;
//        }
        if (StringUtils.isEmpty(isDefault)){
            jhm.putCode(0).putMessage("默认状态为空！");
            renderJson(jhm);
            return;
        }

        String caid=UUIDTool.getUUID();

        try{
            /**
             * 增加的收货地址记录
             */
            Record record=new Record();
            record.set("caid", caid);
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
                if (StringUtils.equals("1",isDefault)){
                    Db.update("UPDATE w_customer_address SET castatus='0' WHERE caid!=?",caid);
                }
                jhm.putCode(1).putMessage("新增成功！");
            } else {
                jhm.putCode(0).putMessage("新增失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }

        renderJson(jhm);
//         renderJson("{\"code\":1,\"message\":\"新增成功！\"}");
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
            int num = Db.delete(sql,listId,userId);
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
//         renderJson("{\"code\":1,\"message\":\"删除成功！\"}");
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
            String sql="select caid id,caname name,caphone phone,caprovince province,cacity city,cadistrict district,castreet street,caaddress address,castatus isDefault from w_customer_address  where cid = ?";
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
//         renderJson("{\"code\":1,\"list\":[{\"address\":\"地址\",\"name\":\"小明\",\"phone\":13130005589},{\"address\":\"地址\",\"name\":\"小米\",\"phone\":13130005589}]}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	增加用户信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/addInfo
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * name      string                 不允许   客户姓名
     * phone      int                   不允许   客户电话
     * sex      string                  不允许   客户性别
     * type     string                  不允许   客户类型
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"增加成功！"
    }

     * 失败：
     * {
    "code": 0,
    "message": "增加失败！"
    }

     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void addInfo(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前端参数
         */
        //收货人姓名
        String name=getPara("name");
        //联系电话
        String phone=getPara("phone");
        //客户性别
        String sex = getPara("sex");
        //客户类型
        String type = getPara("type");

        //非空验证
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
        if (StringUtils.isEmpty(sex)){
            jhm.putCode(0).putMessage("客户性别为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(type)){
            jhm.putCode(0).putMessage("客户类型为空！");
            renderJson(jhm);
            return;
        }
        try{
            /**
             * 增加的用户记录
             */
            Record record=new Record();
            record.set("cid", UUIDTool.getUUID());
            record.set("cgid", "");
            record.set("cname", name);
            record.set("cgender", sex);
            record.set("cphone", phone);
            record.set("ctype", type);
            record.set("cwechat", "");
            record.set("cwxName", "");
            record.set("ccreate_time", DateTool.GetDateTime());
            record.set("cmodify_time", DateTool.GetDateTime());
            record.set("ccreator_id", UUIDTool.getUUID());
            record.set("cmodifier_id", UUIDTool.getUUID());
            record.set("cremark", "");

            boolean flag=Db.save("w_customer",record);
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
//        renderJson("{\"code\":1,\"message\":\"增加成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改联系人
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/editInfo
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	string		            不允许	 用户的id
     * name      string                 不允许   客户姓名
     * phone      int                   不允许   客户电话
     * sex      string                  不允许   客户性别
     * type     string                  不允许   客户类型
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"修改成功！"
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
    public void editInfo(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前端参数
         */
        //客户id
        String userId = getPara("userId");
        //收货人姓名
        String name=getPara("name");
        //联系电话
        String phone=getPara("phone");
        //客户性别
        String sex = getPara("sex");
        //客户类型
        String type = getPara("type");

        if (StringUtils.isEmpty(userId)){
            jhm.putCode(0).putMessage("客户id为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(name)){
            jhm.putCode(0).putMessage("客户姓名为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(phone)){
            jhm.putCode(0).putMessage("客户电话为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(sex)){
            jhm.putCode(0).putMessage("客户性别为空！");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(type)){
            jhm.putCode(0).putMessage("客户类型为空！");
            renderJson(jhm);
            return;
        }
        try{
            /**
             * 修改用户信息
             */
            Record modifyContacts = new Record();
            modifyContacts.set("cid", userId);
            modifyContacts.set("cgid", "");
            modifyContacts.set("cname", name);
            modifyContacts.set("cgender", sex);
            modifyContacts.set("cphone", phone);
            modifyContacts.set("ctype", type);
            modifyContacts.set("cwechat", "");
            modifyContacts.set("cwxName", "");
            modifyContacts.set("ccreate_time", "");
            modifyContacts.set("cmodify_time", DateTool.GetDateTime());
            modifyContacts.set("ccreator_id", "");
            modifyContacts.set("cmodifier_id", UUIDTool.getUUID());
            modifyContacts.set("cremark", "");
            boolean flag = Db.update("w_customer","cid", modifyContacts);
            if(flag){
                jhm.putMessage("修改成功！");
            }else {
                jhm.putCode(0).putMessage("修改失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
//         renderJson("{\"code\":1,\"message\":\"修改成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改收货地址
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/modifyHarvestInformation
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * userId	string		            不允许	 用户的id
     * listId   string                   不允许   记录的id
     * province   string                不允许   省
     * name     string                  不允许   收货姓名
     * phone    string                  不允许   收货手机号
     * city       string                不允许   市
     * district   string                不允许   区
     * street     string                不允许   街道
     * address  string                  不允许   详细地址
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"修改成功！"
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
    public void modifyHarvestInformation(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前端参数
         */
        //客户id
        String userId=getPara("userId");
        //记录id
        String listId = getPara("listId");
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
        //默认状态
        String isDefault=getPara("isDefault");

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
        if (StringUtils.isEmpty(isDefault)){
            jhm.putCode(0).putMessage("默认状态为空！");
            renderJson(jhm);
            return;
        }
//        if (StringUtils.isEmpty(province)){
//            jhm.putCode(0).putMessage("所在省为空！");
//            renderJson(jhm);
//            return;
//        }
//        if (StringUtils.isEmpty(city)){
//            jhm.putCode(0).putMessage("所在市为空！");
//            renderJson(jhm);
//            return;
//        }
//        if (StringUtils.isEmpty(district)){
//            jhm.putCode(0).putMessage("所在区为空！");
//            renderJson(jhm);
//            return;
//        }
//        if (StringUtils.isEmpty(street)){
//            jhm.putCode(0).putMessage("所在街道为空！");
//            renderJson(jhm);
//            return;
//        }
//        if (StringUtils.isEmpty(address)){
//            jhm.putCode(0).putMessage("详细地址为空！");
//            renderJson(jhm);
//            return;
//        }
        try{
            /**
             * 修改的收货地址记录
             */
            Record modifyHarvestAddress = new Record();
            modifyHarvestAddress.set("caid",listId);
            modifyHarvestAddress.set("cid",userId);
            modifyHarvestAddress.set("caname",name);
            modifyHarvestAddress.set("caphone",phone);
            modifyHarvestAddress.set("caprovince",province);
            modifyHarvestAddress.set("cacity",city);
            modifyHarvestAddress.set("cadistrict",district);
            modifyHarvestAddress.set("castreet",street);
            modifyHarvestAddress.set("caaddress",address);
            modifyHarvestAddress.set("camodify_time",DateTool.GetDateTime());
            modifyHarvestAddress.set("camodifier_id",UUIDTool.getUUID());
            modifyHarvestAddress.set("castatus",isDefault);
            boolean flag = Db.update("w_customer_address","caid" ,modifyHarvestAddress);
            if(flag){
                if (StringUtils.equals("1",isDefault)){
                    Db.update("UPDATE w_customer_address SET castatus='0' WHERE caid!=?",listId);
                }
                jhm.putMessage("修改成功！");
            }else {
                jhm.putCode(0).putMessage("修改失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
//        renderJson("{\"code\":1,\"message\":\"修改成功！\"}");
    }
}
