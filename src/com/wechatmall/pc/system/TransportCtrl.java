package com.wechatmall.pc.system;

import com.common.controllers.BaseCtrl;
import com.common.service.DictionaryService;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import easy.util.NumberUtils;
import easy.util.UUIDTool;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * TransportCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class TransportCtrl extends BaseCtrl {

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	添加物流类型
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/system/transport/addTransportType
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  name      string                  不允许   物流类型名称
     *  desc      string                  不允许   物流类型备注
     *
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
    public void addTransportType(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //物流类型名称
        String name = getPara("name");
        //物流类型备注
        String desc = getPara("desc");
        //物流类型英文名
        String value = getPara("value");
        //物流分类值
        String sortStr = getPara("sort");

        //非空验证
        if(StringUtils.isEmpty(name)){
            jhm.putCode(0).putMessage("物流类型名称为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(desc)){
            jhm.putCode(0).putMessage("物流类型备注为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(value)){
            jhm.putCode(0).putMessage("物流类型英文名为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(sortStr)){
            jhm.putCode(0).putMessage("物流分类值为空！");
            renderJson(jhm);
            return;
        }
        int sort = Integer.valueOf(sortStr);
        Record addTransportType = new Record();

        //查找字典值中英文字段和分类字段是否有重复的
        String sql = "SELECT count(1) count from w_dictionary where value = ? or sort = ?  ";
        try{
            int idRecord = Db.queryInt(sql,value,sort);
            if(idRecord > 0){
                jhm.putCode(0).putMessage("添加失败！字段名重复！");
                renderJson(jhm);
                return;
            }
            addTransportType.set("id", UUIDTool.getUUID());
            addTransportType.set("parent_id","700");
            addTransportType.set("name",name);
            addTransportType.set("value",value);
            addTransportType.set("sort",sort);
            addTransportType.set("desc",desc);
            boolean flag = Db.save("w_dictionary","id",addTransportType);
            if(flag){
                DictionaryService.loadDictionary();
                jhm.putMessage("添加成功！");
            }else{
                jhm.putCode(0).putMessage("添加失败！");
            }

        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常!");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"message\":\"添加成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改物流类型
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/system/transport/modifyTransportTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  name      string                  不允许   物流类型名称
     *  id        string                  不允许   物流类型id
     *  desc      string                  不允许   物流类型备注
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

    public  void modifyTransportTypeById(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //物流类型id
        String id = getPara("id");
        //物流类型名称
        String name = getPara("name");
        //物流类型备注
        String desc = getPara("desc");
        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("物流类型id为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(name)){
            jhm.putCode(0).putMessage("物流类型名称为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(desc)){
            jhm.putCode(0).putMessage("物流类型备注为空！");
            renderJson(jhm);
            return;
        }

        Record modifyTransportType = new Record();
        modifyTransportType.set("id",id);
        modifyTransportType.set("name",name);
        modifyTransportType.set("desc",desc);
        try{
            /**
             * 修改物流类型
             */
            boolean flag = Db.update("w_dictionary","id",modifyTransportType);
            if(flag){
                DictionaryService.loadDictionary();
                jhm.putMessage("修改成功！");
            }else{
                jhm.putCode(0).putMessage("修改失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"message\":\"修改成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	删除物流类型
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/system/transport/deleteTransportTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  id        string                  不允许   物流类型id
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
    public void deleteTransportTypeById(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //物流类型id
        String id = getPara("id");
        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("物流类型id为空!");
            renderJson(jhm);
            return;
        }
        try{
        /**
         * 删除物流分类
         */
            String sql = "DELETE from w_dictionary where id=? ";
            int num = Db.update(sql,id);
            if(num > 0){
                DictionaryService.loadDictionary();
                jhm.putCode(1).putMessage("删除成功！");
            }else{
                jhm.putCode(0).putMessage("删除失败！");
            }

        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
       // renderJson("{\"code\":\"1\",\"message\":\"删除成功！\"}");
    }
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	根据id查询物流类型
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/system/transport/showTransportTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  id        string                  不允许   物流类型编号
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":1,
        "message":"查询成功",
        "name":"物流类型名称",
        "desc":"物流类型备注"
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
    public void showTransportTypeById(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //物流类型id
        String id = getPara("id");
        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("物流类型id为空!");
            renderJson(jhm);
            return;
        }
        String sql = "select name ,'desc' from w_dictionary where id = ?";
        try{
            /**
             * 根据id查询物流分类
             */
            Record transportType = Db.findFirst(sql,id);
            if(transportType != null){
                jhm.putMessage("查询成功");
                jhm.put("name",transportType.get("name"));
                jhm.put("desc",transportType.get("desc"));
            }else{
                jhm.putCode(0).putMessage("查询失败！");
            }
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"message\":\"查询成功\",\"name\":\"物流类型名称\",\"desc\":\"物流类型备注\"}");
    }
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询物流类型列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/system/transport/listTransportType
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  type     string               不允许   查询添加，根据物流类型名称模糊查询
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":1,
        "message":"查询成功",
        "list":[{
        "id":"物流类型编号",
        "name":"物流类型名称",
        "desc":"物流类型备注"
        },{
        "id":"物流类型编号",
        "name":"物流类型名称",
        "desc":"物流类型备注"
        }]
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
    public void listTransportType(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //物流类型名称
        String type = getPara("type");
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
        String select = "select d.id id ,d.name name, d.desc  'desc'  ";
        String sql = " from w_dictionary d  where d.parent_id = '700'  ";
        if(type != null && type.length() > 0){
            type = "%" + type + "%";
            sql += "  and d.value like ? ";
            params.add(type);
        }
        try{
            /**
             * 查询物流类型列表
             */
            Page<Record> page = Db.paginate(pageNum, pageSize,select, sql,params.toArray());
            if(page != null && page.getList().size() > 0){
                jhm.putMessage("查询成功！");
                jhm.put("list",page);
            }else{
                jhm.putCode(0).putMessage("查询失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"message\":\"查询成功\",\"list\":[{\"id\":\"物流类型编号\",\"name\":\"物流类型名称\",\"desc\":\"物流类型备注\"},{\"id\":\"物流类型编号\",\"name\":\"物流类型名称\",\"desc\":\"物流类型备注\"}]}");
    }
}
