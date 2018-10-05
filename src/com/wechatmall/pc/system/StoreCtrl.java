package com.wechatmall.pc.system;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.utils.UserSessionUtil;
import easy.util.DateTool;
import easy.util.NumberUtils;
import easy.util.UUIDTool;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * StoreCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class StoreCtrl extends BaseCtrl {

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	自提点列表
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/system/store/listStore
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	    类型	       最大长度	允许空	 描述
     *  storeName      string                不允许  查询添加，根据自提点名称模糊查询
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code": 1,
    "data": {
    "totalRow": 1,
    "pageNumber": 1,
    "firstPage": true,
    "lastPage": true,
    "totalPage": 1,
    "pageSize": 10,
    "list": [{
    "id": "自提点id",
    "storeName": "自提点名称",
    "managerName": "管理员姓名",
    "cityName": "城市名称",
    "address": "地址",
    "workTime": "工作时间",
    "managerPhone": "联系电话",
    "status": "启用状态"
    }]
    }
    }
     * 失败：
     * {
    "code":"0",
    "message":"显示失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void listStore(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //支付方式名称
        String storeName = getPara("storeName");
        //当前页
        String pageNumStr = getPara("pageNumber");
        //页面显示的条数
        String pageSizeStr = getPara("pageSize");

        int pageNum,pageSize;
        //非空验证
        if(StringUtils.isEmpty(pageNumStr)){
            pageNum = NumberUtils.parseInt(pageNumStr,1);
        }else {
            pageNum = Integer.parseInt(pageNumStr);
        }
        if(StringUtils.isEmpty(pageSizeStr)){
            pageSize = NumberUtils.parseInt(pageSizeStr,10);
        }else {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        //新建集合，放入替换参数
        List<Object> params = new ArrayList<>();
        //查询自提点id，自提点名字，自提点城市，自提点联系人姓名，自提点地址，自提点联系电话，状态，运营时间
        String select = "select sid as id,sname as storeName,scity as cityName,sadmin as managerName,saddress as address,sphone as managerPhone,sstatus as 'status',(select name from w_dictionary w where w.`value` = w_store.sstatus and w.parent_id = 300) as status_text,stime as workTime   ";
        String sql = " from w_store where 1=1  ";
        if(storeName != null && storeName.length() > 0){
            storeName = "%" + storeName + "%";
            sql += "  and sname like ? ";
            params.add(storeName);
        }

        sql += " order by  sstatus ,screate_time desc ";
        try{
            /**
             * 查询自提点列表
             */
            Page<Record> page = Db.paginate(pageNum, pageSize,select, sql,params.toArray());
            jhm.put("list",page);
            jhm.putCode(1);

        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record发生异常!");
        }
        renderJson(jhm);
       // renderJson("{\"code\":1,\"data\":{\"totalRow\":1,\"pageNumber\":1,\"firstPage\":true,\"lastPage\":true,\"totalPage\":1,\"pageSize\":10,\"list\":[{\"id\":\"自提点id\",\"storeName\":\"自提点名称\",\"managerName\":\"管理员姓名\",\"cityName\":\"城市名称\",\"address\":\"地址\",\"workTime\":\"工作时间\",\"managerPhone\":\"联系电话\",\"status\":\"启用状态\"}]}}");
    }


    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	添加自提点
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/system/store/addStore
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  storeName       string                不允许   自提点名称
     *  cityName        string                不允许   城市名称
     *  managerName     string                不允许   管理员姓名
     *  address         string                不允许   自提点地址
     *  workTime        string                不允许   营业时间
     *  phone           string                不允许   联系电话
     *  longitude       string                不允许   经度
     *  latitude        string                不允许   纬度
     *  province        string                不允许   自提点所在省
     *  district        string                不允许   自提点所在区
     *  street          string                不允许   自提点所在街道
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":"1",
        "message":"添加成功！"
    }
     * 失败：
     * {
    "code":"0",
    "message":"添加失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void addStore(){
        JsonHashMap jhm = new JsonHashMap();
        UserSessionUtil usu=new UserSessionUtil(getRequest());
        /**
         * 接收前台参数
         */
        //自提点名称
        String  storeName = getPara("storeName");
        //城市名称
        String  cityName = getPara("cityName");
        //管理员姓名
        String  managerName = getPara("managerName");
        //自提点地址
        String  address = getPara("address");
        //营业时间
        String  workTime = getPara("workTime");
        //联系电话
        String  phone = getPara("phone");
        //经度
        String  longitudeStr = getPara("longitude");
        //纬度
        String  latitudeStr = getPara("latitude");
        //自提点所在省
        String  province = getPara("province");
        //自提点所在区
        String  district = getPara("district");
        //自提点所在街道
        String  street = getPara("street");

        //非空验证
        if(StringUtils.isEmpty(storeName)){
            jhm.putCode(0).putMessage("自提点名称为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(cityName)){
            jhm.putCode(0).putMessage("城市名称为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(managerName)){
            jhm.putCode(0).putMessage("管理员姓名为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(address)){
            jhm.putCode(0).putMessage("自提点地址为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(workTime)){
            jhm.putCode(0).putMessage("营业时间为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(phone)){
            jhm.putCode(0).putMessage("联系电话为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(longitudeStr)){
            jhm.putCode(0).putMessage("经度为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(latitudeStr)){
            jhm.putCode(0).putMessage("纬度为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(province)){
            jhm.putCode(0).putMessage("自提点所在省为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(district)){
            jhm.putCode(0).putMessage("自提点所在区为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(street)){
            jhm.putCode(0).putMessage("自提点所在街道为空!");
            renderJson(jhm);
            return;
        }

        float longitude=Float.valueOf(longitudeStr);
        float latitude=Float.valueOf(latitudeStr);

        try{
            /**
             * 新增自提点
             * */
            Record addStoreRecord = new Record();
            addStoreRecord.set("sid", UUIDTool.getUUID());
            addStoreRecord.set("sname",storeName);
            addStoreRecord.set("saddress",address);
            addStoreRecord.set("stime",workTime);
            addStoreRecord.set("sadmin",managerName);
            addStoreRecord.set("sphone",phone);
            addStoreRecord.set("sprovince",province);
            addStoreRecord.set("scity",cityName);
            addStoreRecord.set("sdistrict",district);
            addStoreRecord.set("sstreet",street);
            addStoreRecord.set("slatitude",latitude);
            addStoreRecord.set("slongitude",longitude);
            addStoreRecord.set("sstatus","start_using");
            addStoreRecord.set("screate_time", DateTool.GetDateTime());
            addStoreRecord.set("smodify_time",DateTool.GetDateTime());
            addStoreRecord.set("screator_id",usu.getUserId());
            addStoreRecord.set("smodifier_id",usu.getUserId());
            addStoreRecord.set("sdesc","");
            boolean flag = Db.save("w_store",addStoreRecord);
            if(flag){
                jhm.putCode(1).putMessage("添加成功!");
            }else{
                jhm.putCode(0).putMessage("添加失败!");
            }
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record发生异常!");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"message\":\"添加成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改自提点
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/system/store/updateStoreById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  storeName       string                不允许   自提点名称
     *  cityName        string                不允许   城市名称
     *  managerName     string                不允许   管理员姓名
     *  address         string                不允许   自提点地址
     *  workTime        string                不允许   营业时间
     *  phone           string                不允许   联系电话
     *  longitude       string                不允许   经度
     *  latitude        string                不允许   纬度
     *  province        string                不允许   自提点所在省
     *  district        string                不允许   自提点所在区
     *  street          string                不允许   自提点所在街道
     *  id              string                不允许   自提点id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":"1",
    "message":"修改成功！"
    }
     * 失败：
     * {
    "code":"0",
    "message":"修改失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void updateStoreById(){
        JsonHashMap jhm = new JsonHashMap();
        UserSessionUtil usu=new UserSessionUtil(getRequest());
        /**
         * 接收前台参数
         */
        //自提点名称
        String  storeName = getPara("storeName");
        //城市名称
        String  cityName = getPara("cityName");
        //管理员姓名
        String  managerName = getPara("managerName");
        //自提点地址
        String  address = getPara("address");
        //营业时间
        String  workTime = getPara("workTime");
        //联系电话
        String  phone = getPara("phone");
        //经度
        String  longitude = getPara("longitude");
        //纬度
        String  latitude = getPara("latitude");
        //自提点所在省
        String  province = getPara("province");
        //自提点所在区
        String  district = getPara("district");
        //自提点所在街道
        String  street = getPara("street");
        //自提点id
        String id = getPara("id");

        //非空验证
        if(StringUtils.isEmpty(storeName)){
            jhm.putCode(0).putMessage("自提点名称为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(cityName)){
            jhm.putCode(0).putMessage("城市名称为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(managerName)){
            jhm.putCode(0).putMessage("管理员姓名为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(address)){
            jhm.putCode(0).putMessage("自提点地址为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(workTime)){
            jhm.putCode(0).putMessage("营业时间为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(phone)){
            jhm.putCode(0).putMessage("联系电话为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(longitude)){
            jhm.putCode(0).putMessage("经度为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(latitude)){
            jhm.putCode(0).putMessage("纬度为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(province)){
            jhm.putCode(0).putMessage("自提点所在省为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(district)){
            jhm.putCode(0).putMessage("自提点所在区为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(street)){
            jhm.putCode(0).putMessage("自提点所在街道为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("自提点id为空!");
            renderJson(jhm);
            return;
        }
        try{
            Record updateStoreRecord = new Record();
            updateStoreRecord.set("sid", id);
            updateStoreRecord.set("sname",storeName);
            updateStoreRecord.set("saddress",address);
            updateStoreRecord.set("stime",workTime);
            updateStoreRecord.set("sadmin",managerName);
            updateStoreRecord.set("sphone",phone);
            updateStoreRecord.set("sprovince",province);
            updateStoreRecord.set("scity",cityName);
            updateStoreRecord.set("sdistrict",district);
            updateStoreRecord.set("sstreet",street);
            updateStoreRecord.set("slatitude",latitude);
            updateStoreRecord.set("slongitude",longitude);
            updateStoreRecord.set("smodify_time",DateTool.GetDateTime());
            updateStoreRecord.set("smodifier_id",usu.getUserId());
            boolean flag = Db.update("w_store","sid",updateStoreRecord);
            if(flag){
                jhm.putMessage("修改成功!");
            }else{
                jhm.putCode(0).putMessage("修改失败!");
            }
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record发生异常!");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"message\":\"修改成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	删除自提点
     * 描述
     * 验证
     * 权限	    无
     * URL	     http://localhost:8080/weChatMallMgr/wm/pc/system/store/deleteStoreById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  id       string                     不允许      自提点id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":"1",
    "message":"删除成功！"
    }
     * 失败：
     * {
    "code":"0",
    "message":"删除失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void deleteStoreById(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         * */
        //自提点id
        String id = getPara("id");
        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("自提点id为空!");
            renderJson(jhm);
            return;
        }
        try{
            /**
             * 删除自提点
             */
            String sql = "DELETE from w_store where sid=? ";
            int num = Db.update(sql,id);
            if(num > 0){
                jhm.putCode(1).putMessage("删除成功！");
            }else{
                jhm.putCode(0).putMessage("删除失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常!");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"message\":\"删除成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	根据id查询自提点信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/system/store/showStoreById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  id       string                     不允许      自提点id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code": "1",
    "message":"查询成功！",
    "data": {
    "id": "自提点id",
    "storeName": "自提点名称",
    "cityName": "城市名称",
    "managerName":"管理者姓名",
    "address": "地址",
    "phone": "联系电话",
    "longitude": "经度",
    "latitude":"纬度"
    }
    }
     * 失败：
     * {
    "code":"0",
    "message":"查询失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void showStoreById(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //自提点id
        String id = getPara("id");
        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("自提点id为空!");
            renderJson(jhm);
            return;
        }
        //查询自提点id，自提点名字，自提点城市，自提点联系人姓名，自提点地址，自提点联系电话，自提点经度，纬度
        String sql = "select sid id,sname storeName,scity cityName,sadmin managerName,saddress address,sphone phone,slongitude longitude,slatitude latitude from w_store where  sid = ? ";
        try{
            /**
             * 根据id查询物流分类
             */
            Record showStore = Db.findFirst(sql,id);
            if(showStore != null){
                jhm.putMessage("查询成功");
                jhm.put("data",showStore);
            }else{
                jhm.putCode(0).putMessage("查询失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
       // renderJson("{\"code\":\"1\",\"message\":\"查询成功！\",\"data\":{\"id\":\"自提点id\",\"storeName\":\"自提点名称\",\"cityName\":\"城市名称\",\"managerName\":\"管理者姓名\",\"address\":\"地址\",\"phone\":\"联系电话\",\"longitude\":\"经度\",\"latitude\":\"纬度\"}}");
    }

    /***
     * http://localhost:8080/weChatMallMgr/wm/pc/system/store/setStoreTypeById
     */

    public void setStoreTypeById(){
        JsonHashMap jhm=new JsonHashMap();
        /**
         * 接收前端参数
         */
        //自提点id
        String id=getPara("id");
        //自提点状态
        String statusStr=getPara("status");
        String status = "";
        if(statusStr.equals("start_using")){
            status = "stop_using";
        }
        if(statusStr.equals("stop_using")) {
            status = "start_using";
        }

        //非空验证
        if (StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("自提点id为空");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(status)){
            jhm.putCode(0).putMessage("自提点状态为空");
            renderJson(jhm);
            return;
        }
        try{
            Db.update("UPDATE w_store SET sstatus=? WHERE sid=?",status,id);
            jhm.putCode(1).putMessage("设置成功");
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record异常");
        }
        renderJson(jhm);

    }
}
