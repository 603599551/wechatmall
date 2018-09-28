package com.wechatmall.pc.product;

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
 * ProductManageCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class ProductManageCtrl extends BaseCtrl {

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查看商品列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/listProduct
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * 	type	string		            允许	 查询添加，按照分组名称模糊查询
     *  name    string                  允许   查询添加，按照商品名称模糊查询
     *  status  string                  允许   查询添加，按照上架状态完全匹配查询
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":1,
        "data":{
        "totalRow":1,
        "pageNumber":1,
        "firstPage":true,
        "lastPage":true,
        "totalPage":1,
        "pageSize":10,
        "list":[
        {
        "id":"商品id",
        "type":"所属分类",
        "name":"商品名称",
        "pictureUrl":"商品图片",
        "price":"商品价格",
        "status":"上架状态",
        "creator":"发布人",
        "createTime":"创建时间"
        }
        ]
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

    public void listProduct(){
        JsonHashMap jhm = new JsonHashMap();

        List<Object> params = new ArrayList<>();

        String type = getPara("type");
        String name = getPara("name");
        String status = getPara("status");
        String pageNumStr = getPara("pageNum");
        String pageSizeStr = getPara("pageSize");

        //为空时赋予默认值
        int pageNum = NumberUtils.parseInt(pageNumStr, 1);
        int pageSize = NumberUtils.parseInt(pageSizeStr, 10);

        /**
         * 通过商品信息表w_product，商品分类表w_product_category和客户信息表w_customer三表关联查询 : "id":"商品id" , "type":"所属分类名称",
         *"name":"商品名称" , "pictureUrl":"商品图片" , "price":"商品价格" , "status":"上架状态" , "creator":"发布人" , "createTime":"创建时间"
         */
        String select = "SELECT p.pid AS id,pc.pcname AS 'type',p.pname AS name,p.picture AS pictureUrl,p.price AS price,d.name AS 'status',p.pstatus AS value,a.name AS creator,p.pcreate_time AS createTime,p.pkeyword AS keyword,p.pintroduction AS introduction,p.pdetail AS detail ";

        StringBuilder sql = new StringBuilder("  FROM w_product p,w_product_category pc,w_dictionary d,w_admin a WHERE p.pcid=pc.pcid AND a.id=p.pcreator_id AND FIND_IN_SET(p.pstatus,d.value)");

        //根据要求添加检索条件
        if(!StringUtils.isEmpty(type)){
            sql.append(" and p.pcid = ? ");
            params.add(type);
        }

        if (!StringUtils.isEmpty(name)) {
            name = "%" + name + "%";
            sql.append(" and p.pname like ?  ");
            params.add(name);
        }

        if(!StringUtils.isEmpty(status)){
            sql.append(" and p.pstatus = ? ");
            params.add(status);
        }

        sql.append(" ORDER BY p.pcreate_time DESC");

        try {
            Page<Record> page = Db.paginate(pageNum, pageSize, select, sql.toString(), params.toArray());
            jhm.putCode(1).put("data", page);
        } catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);

        //renderJson("{\"code\":1,\"data\":{\"totalRow\":1,\"pageNumber\":1,\"firstPage\":true,\"lastPage\":true,\"totalPage\":1,\"pageSize\":10,\"list\":[{\"id\":\"商品id\",\"type\":\"所属分类\",\"name\":\"商品名称\",\"pictureUrl\":\"商品图片\",\"price\":\"商品价格\",\"status\":\"上架状态\",\"creator\":\"发布人\",\"createTime\":\"创建时间\"}]}}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	添加商品
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/addProduct
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * 	type	string		            不允许	 所属分类
     *  name    string                  不允许   商品名称
     *  price  string                   不允许   商品价格
     *  keyword     string              不允许   关键字
     *  pictureUrl  string              不允许   商品图片
     *  content     string              不允许   详细描述
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

        public void addProduct(){
            JsonHashMap jhm = new JsonHashMap();
            UserSessionUtil usu = new UserSessionUtil(getRequest());

            String type = getPara("type");
            String name = getPara("name");
            String price = getPara("price");
            String keyword = getPara("keyword");
            String pictureUrl = getPara("pictureUrl");
            String content = getPara("content");
            String sketch = getPara("sketch");

            //进行非空判断
            if(StringUtils.isEmpty(type)){
                jhm.putCode(0).putMessage("分类不能为空！");
                renderJson(jhm);
                return;
            }

            if(StringUtils.isEmpty(name)){
                jhm.putCode(0).putMessage("商品名称不能为空！");
                renderJson(jhm);
                return;
            }

            if(StringUtils.isEmpty(price)){
                jhm.putCode(0).putMessage("价格不能为空！");
                renderJson(jhm);
                return;
            }

            if(StringUtils.isEmpty(keyword)){
                jhm.putCode(0).putMessage("关键字不能为空！");
                renderJson(jhm);
                return;
            }

            if(StringUtils.isEmpty(pictureUrl)){
                jhm.putCode(0).putMessage("图片不能为空！");
                renderJson(jhm);
                return;
            }

            if(StringUtils.isEmpty(content)){
                jhm.putCode(0).putMessage("内容不能为空！");
                renderJson(jhm);
                return;
            }

            if(StringUtils.isEmpty(sketch)){
                jhm.putCode(0).putMessage("简述不能为空！");
                renderJson(jhm);
                return;
            }

            //装商品信息
            Record record = new Record();
            record.set("pid", UUIDTool.getUUID());
            record.set("pcid", type);
            record.set("pname", name);
            record.set("price", price);
            record.set("pkeyword", keyword);
            record.set("picture", pictureUrl);
            record.set("pintroduction", sketch);
            record.set("pdetail", content);
            record.set("pcreate_time", DateTool.GetDateTime());
            record.set("pmodify_time", DateTool.GetDateTime());
            record.set("pcreator_id", usu.getUserId());
            record.set("pmodifier_id", usu.getUserId());

            try {
                boolean flag = Db.save("w_product", record);
                if(flag){
                    jhm.putCode(1).putMessage("添加成功！");
                } else {
                    jhm.putCode(0).putMessage("插入数据库失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                jhm.putCode(-1).putMessage("服务器发生异常！");
            }
            renderJson(jhm);

        }
        //renderJson("{\"code\":\"1\",\"message\":\"添加成功！\"}");

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改商品
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/updateProductById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  id      string                  不允许   商品id
     * 	type	string		            不允许	 所属分类
     *  name    string                  不允许   商品名称
     *  price  string                   不允许   商品价格
     *  keyword     string              不允许   关键字
     *  pictureUrl  string              不允许   商品图片
     *  content     string              不允许   详细描述
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

    public void updateProductById(){
        JsonHashMap jhm = new JsonHashMap();
        UserSessionUtil usu = new UserSessionUtil(getRequest());

        String id = getPara("id");
        String type = getPara("type");
        String name = getPara("name");
        String price = getPara("price");
        String keyword = getPara("keyword");
        String picture = getPara("pictureUrl");
        String content = getPara("content");
        String sketch = getPara("sketch");

        //进行非空判断
        if(StringUtils.isEmpty(type)){
            jhm.putCode(0).putMessage("分类不能为空！");
            renderJson(jhm);
            return;
        }

        if(StringUtils.isEmpty(name)){
            jhm.putCode(0).putMessage("商品名称不能为空！");
            renderJson(jhm);
            return;
        }

        if(StringUtils.isEmpty(price)){
            jhm.putCode(0).putMessage("价格不能为空！");
            renderJson(jhm);
            return;
        }

        if(StringUtils.isEmpty(keyword)){
            jhm.putCode(0).putMessage("关键字不能为空！");
            renderJson(jhm);
            return;
        }

        if(StringUtils.isEmpty(picture)){
            jhm.putCode(0).putMessage("图片不能为空！");
            renderJson(jhm);
            return;
        }

        if(StringUtils.isEmpty(content)){
            jhm.putCode(0).putMessage("内容不能为空！");
            renderJson(jhm);
            return;
        }

        if(StringUtils.isEmpty(sketch)){
            jhm.putCode(0).putMessage("简述不能为空！");
            renderJson(jhm);
            return;
        }

        try {
            Record record = Db.findById("w_product", "pid", id);
            if(record == null){
                jhm.putCode(0).putMessage("id有误！");
                renderJson(jhm);
                return;
            }
            record.set("pcid", type);
            record.set("pname", name);
            record.set("price", price);
            record.set("pkeyword", keyword);
            record.set("picture", picture);
            record.set("pintroduction", sketch);
            record.set("pdetail", content);
            record.set("pmodify_time", DateTool.GetDateTime());
            record.set("pmodifier_id", usu.getUserId());

            boolean flag = Db.update("w_product","pid", record);
            if(flag){
                jhm.putCode(1).putMessage("修改成功！");
            } else {
                jhm.putCode(0).putMessage("数据库修改失败！");
            }
        } catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);

        //renderJson("{\"code\":\"1\",\"message\":\"修改成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	删除商品
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/deleteProductById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  id      string                 不允许   商品id
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

    public void deleteProductById(){
        JsonHashMap jhm = new JsonHashMap();

        String id = getPara("id");
        String status = getPara("status");

        //进行非空判断
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("id不能为空！");
            renderJson(jhm);
            return;
        }

        if(StringUtils.isEmpty(status)) {
            jhm.putCode(0).putMessage("状态类型不能为空！");
            renderJson(jhm);
            return;
        }

        try {
            Record record = Db.findById("w_product","pid", id);
            if(record == null){
                jhm.putCode(0).putMessage("id有误！");
                renderJson(jhm);
                return;
            }
            if(StringUtils.equals("notOn_sale", status)){
                record.set("pstatus", "notOn_sale");
            } else {
                record.set("pstatus", "on_sale");
            }

            boolean flag = Db.update("w_product", "pid", record);
            if(flag){
                jhm.putCode(1).putMessage("操作成功！");
            } else {
                jhm.putCode(0).putMessage("数据库操作失败！");
            }
        } catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"message\":\"删除成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	预览商品信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/previewProductById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  id      string                 不允许   商品id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":"1",
        "data":{
        "id":"商品id",
        "name":"商品名称",
        "price":"商品价格",
        "keyword":"关键字1，关键字2，关键字3",
        "pictureUrl":"商品图片",
        "type":"商品分类"
        }
    }
     * 失败：
     * {
    "code":"0",
    "message":"预览失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void previewProductById(){
        JsonHashMap jhm = new JsonHashMap();

        String id = getPara("id");

        //进行非空验证
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("id不能为空！");
            renderJson(jhm);
            return;
        }

        /**
         *根据商品信息表查询 : "id":"商品id" , "name":"商品名称" , "price":"商品价格",
         *"keyword":"关键字1，关键字2，关键字3" , "pictureUrl":"商品图片" , "type":"商品分类"
         */

        String sql = "SELECT pid as id, pname as name, price as price, pkeyword as keyword, picture as pictureUrl, pcid as type FROM w_product wp WHERE wp.pid = ? ";

        try {
            Record record = Db.findFirst(sql, id);
            if(record == null){
                jhm.putCode(0).putMessage("id有误！");
                renderJson(jhm);
                return;
            }
            jhm.putCode(1).put("data", record);
        } catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"data\":{\"id\":\"商品id\",\"name\":\"商品名称\",\"price\":\"商品价格\",\"keyword\":\"关键字1，关键字2，关键字3\",\"pictureUrl\":\"商品图片\",\"type\":\"商品分类\"}}");
    }


}
