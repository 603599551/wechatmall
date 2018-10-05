package com.wechatmall.pc.customer;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sun.org.apache.regexp.internal.RE;
import com.utils.UserSessionUtil;
import easy.util.DateTool;
import easy.util.NumberUtils;
import easy.util.UUIDTool;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CustomerGroupCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class CustomerGroupCtrl extends BaseCtrl{
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询分组列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/customer/group/listCustomerGroup
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * 	keyword	string		            不允许	 查询添加，按照分组名称模糊查询
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code": "1",
    "data": {
    "totalRow": "1",
    "pageNumber": "1",
    "firstPage": "true",
    "lastPage": "true",
    "totalPage": "1",
    "pageSize": "10",
    "list": [{
    "id": "分组id",
    "groupName": "分组名称",
    "sort": "分组排序"
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

    public void listCustomerGroup(){
        JsonHashMap jhm=new JsonHashMap();
        /**
         * 接收前端参数
         */
        //分组名称
        String keyword=getPara("keyword");
        //页码
        String pageNumStr=getPara("pageNumber");
        //每页限制的记录数
        String pageSizeStr=getPara("pageSize");

        //为空时赋予默认值
        int pageNum = NumberUtils.parseInt(pageNumStr, 1);
        int pageSize = NumberUtils.parseInt(pageSizeStr, 10);

        //查询customer_group表 得到分组id，名称，排序
        String select="SELECT cgid AS id,cgname AS groupName,cgsort AS sort  ";

        StringBuilder sql=new StringBuilder("FROM w_customer_group");

        List<Object> params = new ArrayList<>();

        //查询添加，按照分组名称模糊查询
        if (StringUtils.isNotEmpty(keyword)){
            sql=sql.append(" WHERE cgname LIKE CONCAT('%',?,'%')");
            params.add(keyword);
        }

        sql=sql.append(" ORDER BY cgsort ASC");

        try{
            //分页查询
            Page<Record> page = Db.paginate(pageNum, pageSize, select, sql.toString(), params.toArray());
            jhm.putCode(1);
            jhm.put("data", page);
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record异常");
        }

        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"data\":{\"totalRow\":\"1\",\"pageNumber\":\"1\",\"firstPage\":\"true\",\"lastPage\":\"true\",\"totalPage\":\"1\",\"pageSize\":\"10\",\"list\":[{\"id\":\"分组id\",\"groupName\":\"分组名称\",\"sort\":\"分组排序\"}]}}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-25
     * 名称  	添加分组
     * 描述
     * 验证
     * 权限	    无
     * URL	   http://localhost:8080/weChatMallMgr/wm/pc/customer/group/addCustomerGroup
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * groupName	string		          不允许	 分组名称
     * sort         string                不允许     分组排序
     * id           string                不允许     分组id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "caozuo":"记得往商品定价表中增加记录！！",
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
    public void addCustomerGroup(){
        JsonHashMap jhm=new JsonHashMap();

        UserSessionUtil usu=new UserSessionUtil(getRequest());

        /**
         * 接收前端参数
         */
        //分组名称
        String groupName=getPara("groupName");
        //分组排序
        String sortStr=getPara("sort");

        //非空验证
        if (StringUtils.isEmpty(groupName)){
            jhm.putCode(0).putMessage("分组名称为空");
            renderJson(jhm);
            return;
        }
//        if (StringUtils.isEmpty(sort)){
//            jhm.putCode(0).putMessage("分组排序为空");
//            renderJson(jhm);
//            return;
//        }

        int sort = NumberUtils.parseInt(sortStr, 1);

        try{
            String userId=usu.getUserId();
            Map paraMap =new HashMap();
            paraMap.put("groupName", groupName);
            paraMap.put("sort", sort);
            paraMap.put("userId", userId);
            CustomerGroupService srv = enhance(CustomerGroupService.class);
            jhm = srv.addCustomerGroup(paraMap);

        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record异常");
        }

        renderJson(jhm);
        //renderJson("{\"caozuo\":\"记得往商品定价表中增加记录！！\",\"code\":\"1\",\"message\":\"添加成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-25
     * 名称  	修改分组
     * 描述
     * 验证
     * 权限	    无
     * URL	   http://localhost:8080/weChatMallMgr/wm/pc/customer/group/updateCustomerGroupById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * groupName	string		          不允许	 分组名称
     * sort         string                不允许     分组排序
     * id           string                不允许     分组id
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
    public void updateCustomerGroupById(){
        JsonHashMap jhm=new JsonHashMap();
        /**
         * 接收前端参数
         */
        //分组id
        String id=getPara("id");
        //分组名称
        String groupName=getPara("groupName");
        //	分组排序
        String sort=getPara("sort");

        //非空验证
        if (StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("分组id为空");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(groupName)){
            jhm.putCode(0).putMessage("分组名称为空");
            renderJson(jhm);
            return;
        }
//        if (StringUtils.isEmpty(sort)){
//            jhm.putCode(0).putMessage("分组排序为空");
//            renderJson(jhm);
//            return;
//        }

        try{
            Db.update("UPDATE w_customer_group SET cgname=?,cgsort=? WHERE cgid=?",groupName,sort,id);
            jhm.putCode(1).putMessage("修改成功");
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record异常");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"message\":\"修改成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-25
     * 名称  	删除分组
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/customer/group/deleteCustomerGroupById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * id           string                不允许     分组id
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
    public void deleteCustomerGroupById(){
        JsonHashMap jhm=new JsonHashMap();
        /**
         * 接收前端参数
         */
        //分组id
        String id=getPara("id");

        //非空验证
        if (StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("分组id为空");
            renderJson(jhm);
            return;
        }

        try{
            Map paraMap =new HashMap();
            paraMap.put("id", id);
            CustomerGroupService srv = enhance(CustomerGroupService.class);
            jhm = srv.deleteCustomerGroupById(paraMap);
           ;
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record异常");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"message\":\"删除成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-25
     * 名称  	查看分组
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/customer/group/showCustomerGroupById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * id           string                不允许     分组id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":"1",
        "data":{
        "id":"分组id",
        "groupName":"分组名称",
        "sort":"分组排序"
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
    public void showCustomerGroupById(){
        JsonHashMap jhm=new JsonHashMap();
        //分组id
        String id=getPara("id");

        //非空验证
        if (StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("分组id为空");
            renderJson(jhm);
            return;
        }

        String sql1="SELECT cgid AS id,cgname AS groupName,cgsort AS sort FROM w_customer_group WHERE cgid=?";

        try{

            Record r=Db.findFirst(sql1,id);
            jhm.putCode(1);
            jhm.put("data",r);

        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record异常");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"data\":{\"id\":\"分组id\",\"groupName\":\"分组名称\",\"sort\":\"分组排序\"}}");
    }

}
