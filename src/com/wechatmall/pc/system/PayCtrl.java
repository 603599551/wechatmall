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
 * PayCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class PayCtrl extends BaseCtrl{

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	添加支付方式
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/addPayType
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  name      string                  不允许   	支付方式名称
     *  desc      string                  不允许    支付方式备注
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
    public void addPayType(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //支付类型名称
        String name = getPara("name");
        //支付类型备注
        String desc = getPara("desc");
        //支付类型英文名
        String value = getPara("value");
        //支付分类值
        String sortStr = getPara("sort");

        //非空验证
        if(StringUtils.isEmpty(name)){
            jhm.putCode(0).putMessage("支付类型名称为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(desc)){
            jhm.putCode(0).putMessage("支付类型备注为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(value)){
            jhm.putCode(0).putMessage("支付类型英文名为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(sortStr)){
            jhm.putCode(0).putMessage("支付分类值为空！");
            renderJson(jhm);
            return;
        }
        int sort = Integer.valueOf(sortStr);
        Record addPayType = new Record();
        //查找字典值中英文字段和分类字段是否有重复的
        String sql = "SELECT count(1) count from w_dictionary where value = ? or sort = ?  ";
        try{
           int idRecord = Db.queryInt(sql,value,sort);
            if(idRecord > 0){
                jhm.putCode(0).putMessage("添加失败！字段名重复！");
                renderJson(jhm);
                return;
            }
            addPayType.set("id", UUIDTool.getUUID());
            addPayType.set("parent_id","800");
            addPayType.set("name",name);
            addPayType.set("value",value);
            addPayType.set("sort",sort);
            addPayType.set("status_color","");
            addPayType.set("desc",desc);
            boolean flag = Db.save("w_dictionary","id",addPayType);
            if(flag){
                jhm.putMessage("添加成功！");
            }else{
                jhm.putCode(0).putMessage("添加失败！");
            }
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(0).putMessage("Record发生异常!");
        }
        renderJson(jhm);
       // renderJson("{\"code\":\"1\",\"message\":\"添加成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改支付方式
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/modifyPayTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  name      string                  不允许   	支付方式名称
     *  id        string                  不允许    支付方式编号
     *  desc      string                  不允许    支付方式备注
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

    public void modifyPayTypeById(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //支付方式编号
        String id = getPara("id");
        //支付方式名称
        String name = getPara("name");
        //支付方式备注
        String desc = getPara("desc");
        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("支付方式编号为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(name)){
            jhm.putCode(0).putMessage("支付方式名称为空!");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(desc)){
            jhm.putCode(0).putMessage("支付方式备注为空！");
            renderJson(jhm);
            return;
        }
        Record modifyPayType = new Record();
        modifyPayType.set("id",id);
        modifyPayType.set("name",name);
        modifyPayType.set("desc",desc);
        try{
            /**
             * 修改支付类型
             */
            boolean flag = Db.update("w_dictionary","id",modifyPayType);
            if(flag){
                DictionaryService.loadDictionary();
                jhm.putMessage("修改成功！");
            }else{
                jhm.putCode(0).putMessage("修改失败！");
            }
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(0).putMessage("Record发生异常!");
        }
        renderJson(jhm);
       // renderJson("{\"code\":\"1\",\"message\":\"修改成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	删除支付方式
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/deletePayTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  id        string                  不允许    支付方式编号
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
    public void deletePayTypeById(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        /**
         * 接收前台参数
         */
        //支付方式编号
        String id = getPara("id");
        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("物流类型id为空!");
            renderJson(jhm);
            return;
        }
        try{
            /**
             * 删除支付方式
             */
            String sql = "DELETE from w_dictionary where id=? ";
            int num = Db.update(sql,id);
            if(num > 0){
                jhm.putCode(1).putMessage("删除成功！");
            }else{
                jhm.putCode(0).putMessage("删除失败！");
            }
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(0).putMessage("Record发生异常!");
        }
        renderJson(jhm);
      //  renderJson("{\"code\":\"1\",\"message\":\"删除成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	根据id查询支付方式
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/showPayTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  id        string                  不允许    支付方式编号
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":1,
        "message":"查询成功",
        "name":"支付方式名称",
        "desc":"支付方式备注"
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

    public void showPayTypeById(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //支付方式编号
        String id = getPara("id");
        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("支付方式编号为空!");
            renderJson(jhm);
            return;
        }
        String sql = "select name ,'desc' from w_dictionary where id = ?";
        try{
            /**
             * 根据id查询支付方式
             */
            Record showPayType = Db.findFirst(sql,id);
            if(showPayType != null){
                jhm.putMessage("查询成功");
                jhm.put("name",showPayType.get("name"));
                jhm.put("desc",showPayType.get("desc"));
            }else{
                jhm.putCode(0).putMessage("查询失败！");
            }
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(0).putMessage("Record发生异常!");
        }
        renderJson(jhm);
      //  renderJson("{\"code\":1,\"message\":\"查询成功\",\"name\":\"支付方式名称\",\"desc\":\"支付方式备注\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询支付方式列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/listPayType
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度 	允许空	 描述
     *  name     string                    不允许    查询添加，根据支付方式名称完全匹配查询
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {{
        "code":1,
        "message":"查询成功",
        "list":[{
        "id":"支付方式编号",
        "name":"支付方式名称",
        "desc":"支付方式备注"
        },{
        "id":"支付方式编号",
        "name":"支付方式名称",
        "desc":"支付方式备注"
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

    public void listPayType(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前台参数
         */
        //支付方式名称
        String name = getPara("name");
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
        String sql = " from w_dictionary d where d.parent_id = '800'  ";
        if(name != null && name.length() > 0){
            sql += "  and d.value = ? ";
            params.add(name);
        }
        try{
            /**
             * 查询型列表
             */
            Page<Record> page = Db.paginate(pageNum, pageSize,select, sql,params.toArray());
            if(page != null && page.getList().size() > 0){
                jhm.putMessage("查询成功！");
                jhm.put("list",page);
            }else{
                jhm.putCode(0).putMessage("查询失败！");
            }
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(0).putMessage("Record发生异常!");
        }
        renderJson(jhm);
       // renderJson("{\"code\":1,\"message\":\"查询成功\",\"list\":[{\"id\":\"支付方式编号\",\"name\":\"支付方式名称\",\"desc\":\"支付方式备注\"},{\"id\":\"支付方式编号\",\"name\":\"支付方式名称\",\"desc\":\"支付方式备注\"}]}");
    }
}
