package com.wechatmall.pc.notice;

import com.common.controllers.BaseCtrl;
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
 * NoticeCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class NoticeCtrl extends BaseCtrl {

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	新增通知
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/addNotice
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  type          string                  不允许      	通知类型
     *  content       string                  不允许       通知内容

     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"提交成功"
    }
     * 失败：
     * {
    "code":"0",
    "message":"提交失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void addNotice(){
        JsonHashMap jhm = new JsonHashMap();
        UserSessionUtil usu=new UserSessionUtil(getRequest());
        /**
         * 接收前端参数
         */
        //通知类型
        String type = getPara("type");
        //通知内容
        String content = getPara("content");

        //非空验证
        if(StringUtils.isEmpty(type)){
            jhm.putCode(0).putMessage("通知类型为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(content)){
            jhm.putCode(0).putMessage("通知内容为空！");
            renderJson(jhm);
            return;
        }
        try{
            /**
             * 新增通知
             */
            Record addNoticeRecord = new Record();
            addNoticeRecord.set("nid", UUIDTool.getUUID());
            addNoticeRecord.set("ncontent",content);
            addNoticeRecord.set("ntype",type);
            addNoticeRecord.set("ncreate_time", DateTool.GetDateTime());
            addNoticeRecord.set("nmodify_time",DateTool.GetDateTime());
            addNoticeRecord.set("ncreator_id",usu.getUserId());
            addNoticeRecord.set("nmodifier_id",usu.getUserId());
            addNoticeRecord.set("ndesc","");
            boolean flag = Db.save("w_notice","nid",addNoticeRecord);
            if(flag){
                jhm.putMessage("提交成功!");
            }else{
                jhm.putCode(0).putMessage("提交失败!");
            }
        }catch(Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"message\":\"提交成功\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改通知
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/updateNoticeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  id             string               不允许       通知id
     *  type           string               不允许       通知类型
     *  content        string               不允许       通知内容
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"修改成功"
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
    public void updateNoticeById(){
        JsonHashMap jhm = new JsonHashMap();
        UserSessionUtil usu=new UserSessionUtil(getRequest());
        /**
         * 接收前端参数
         */
        //通知id
        String id = getPara("id");
        //通知类型
        String type = getPara("type");
        //通知内容
        String content = getPara("content");

        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("通知id为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(type)){
            jhm.putCode(0).putMessage("通知类型为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(content)){
            jhm.putCode(0).putMessage("通知内容为空！");
            renderJson(jhm);
            return;
        }
        try{
            /**
             * 修改通知
             */
            Record updateNoticeById = new Record();
            updateNoticeById.set("nid",id);
            updateNoticeById.set("ncontent",content);
            updateNoticeById.set("ntype",type);
            updateNoticeById.set("nmodify_time",DateTool.GetDateTime());
            updateNoticeById.set("nmodifier_id",usu.getUserId());
            boolean flag = Db.update("w_notice","nid",updateNoticeById);
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
      //  renderJson("{\"code\":1,\"message\":\"修改成功\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	删除通知
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/deleteNoticeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  id             string               不允许       通知id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"删除成功"
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
    public void deleteNoticeById(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前端参数
         */
        //通知id
        String id = getPara("id");

        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("通知id为空！");
            renderJson(jhm);
            return;
        }
        try{
            /**
             * 根据id删通知
             */
            String sql = "DELETE from w_notice where  nid = ?";
            int num = Db.delete(sql,id);
            if(num > 0){
                jhm.putMessage("删除成功！");
            }else {
                jhm.putCode(0).putMessage("删除失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
       // renderJson("{\"code\":1,\"message\":\"删除成功\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询通知列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/listNotice
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  content             string               不允许       查询添加，根据通知内容模糊查询
     *  type                string                不允许      查询添加，根据通知类型完全匹配查询
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"修改成功！",
    "data":{
    "totalRow":1,
    "pageNumber":1,
    "firstPage":true,
    "lastPage":true,
    "totalPage":1,
    "pageSize":10,
    "list":[
    {
    "id":"通知id",
    "content":"通知内容",
    "type":"通知类型",
    "createTime":"创建时间",
    "modifyTime":"修改时间"
    }
    ]
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
    public void listNotice(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前端参数
         */
        //通知内容
        String content = getPara("content");
        //通知类型
        String type = getPara("type");
        //当前页
        String pageNumStr = getPara("pageNum");
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
        //查询通知id，通知内容，通知类型，通知创建时间，通知修改时间。通知内容模糊查询，通知类型完全匹配查询
        String select = "select nid id,ncontent content,ntype type,ncreate_time createTime,nmodify_time modifyTime  ";
        String sql = " from w_notice where 1=1 ";
        if(content != null && content.length() > 0){
            content = "%" + content + "%";
            sql += " and ncontent like ? ";
            params.add(content);
        }
        if(type != null && type.length() > 0){
            sql += " and ntype = ? ";
            params.add(type);
        }
        sql += "order by nmodify_time desc";
        try{
            /**
              * 查询通知列表
              */
            Page<Record> page = Db.paginate(pageNum, pageSize,select, sql,params.toArray());
            if(page != null && page.getList().size() > 0){
                jhm.putMessage("查询成功！");
                jhm.put("data",page);
            }else{
                jhm.putCode(0).putMessage("查询失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"message\":\"修改成功！\",\"data\":{\"totalRow\":1,\"pageNumber\":1,\"firstPage\":true,\"lastPage\":true,\"totalPage\":1,\"pageSize\":10,\"list\":[{\"id\":\"通知id\",\"content\":\"通知内容\",\"type\":\"通知类型\",\"createTime\":\"创建时间\",\"modifyTime\":\"修改时间\"}]}}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	根据id查看通知
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/showNoticeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  id             string               不允许       通知id
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code": "1",
    "data": {
    "id": "通知id",
    "content": "通知内容",
    "type":"通知类型"
    }
    }
     * 失败：
     * {
    "code":"0",
    "message":"查看失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void showNoticeById(){
        JsonHashMap jhm = new JsonHashMap();
        /**
         * 接收前端参数
         */
        //通知id
        String id = getPara("id");
        //非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("通知id为空！");
            renderJson(jhm);
            return;
        }

        try{
            /**
             * 根据id查看通知
             */
            String sql = "select nid id,ncontent content,ntype type from w_notice where nid = ?";
            Record noticeById = Db.findFirst(sql,id);
            if(noticeById != null){
                jhm.put("data",noticeById);
            }else{
                jhm.putCode(0).putMessage("查看失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"data\":{\"id\":\"通知id\",\"content\":\"通知内容\",\"type\":\"通知类型\"}}");
    }
}
