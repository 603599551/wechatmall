package com.wechatmall.pc.product;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProductCategoryCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class ProductCategoryCtrl extends BaseCtrl {

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查看商品分类列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/product/category/listProductType
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * 	keyword	string		            不允许	 查询添加，按照分类名称模糊查询
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code": 1,
        "data": {
        "totalRow": "1",
        "pageNumber": "1",
        "firstPage": "true",
        "lastPage": "true",
        "totalPage": "1",
        "pageSize": "10",
        "list": [{
        "id": "分类id",
        "name": "分类名称",
        "type": "上级分类",
        "sort": "分类排序"
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
    public void listProductType(){
        JsonHashMap jhm=new JsonHashMap();
        /**
         * 接收前端参数
         */
        //商品分类名称
        String keyword=getPara("keyword");
        //页码
        String pageNumStr=getPara("pageNumber");
        //每页限制的记录数
        String pageSizeStr=getPara("pageSize");

        //为空时赋予默认值
        int pageNum = NumberUtils.parseInt(pageNumStr, 1);
        int pageSize = NumberUtils.parseInt(pageSizeStr, 10);

        //查询product_category表得到 商品分类id，名称，排序
        String select="SELECT pcid AS id,pcname AS name ,pcsort AS sort";

        StringBuilder sql=new StringBuilder(" FROM w_product_category");

        List<Object> params = new ArrayList<>();

        //查询添加，按照商品分类名称模糊查询
        if (StringUtils.isNotEmpty(keyword)){
            sql=sql.append(" WHERE pcname  LIKE CONCAT('%',?,'%')");
            params.add(keyword);
        }

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
        //renderJson("{\"code\":1,\"data\":{\"totalRow\":\"1\",\"pageNumber\":\"1\",\"firstPage\":\"true\",\"lastPage\":\"true\",\"totalPage\":\"1\",\"pageSize\":\"10\",\"list\":[{\"id\":\"分类id\",\"name\":\"分类名称\",\"type\":\"上级分类\",\"sort\":\"分类排序\"}]}}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	添加商品分类
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/product/category/addProductType
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * 	name	string		            不允许	 分类名称
     *  type    string                  不允许   上级分类
     *  sort    string                  不允许   分类排序
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
    public void addProductType(){
        JsonHashMap jhm=new JsonHashMap();
        UserSessionUtil usu=new UserSessionUtil(getRequest());

        /**
         * 接收前端参数
         */
        //商品分类名称
        String name=getPara("name");
        //商品分类排序
        String sortStr=getPara("sort");
        //非空验证
        if (StringUtils.isEmpty(name)){
            jhm.putCode(0).putMessage("商品分类名称为空");
            renderJson(jhm);
            return;
        }
//        if (StringUtils.isEmpty(sort)){
//            jhm.putCode(0).putMessage("商品分类排序为空");
//            renderJson(jhm);
//            return;
//        }

        int sort = NumberUtils.parseInt(sortStr, 1);

        //商品分类id
        String id= UUIDTool.getUUID();

        try{
            //往customer_group表存储新的分组信息
            Record r=new Record();
            r.set("pcid",id);
            r.set("pcname", name);
            r.set("pcsort", sort);
            r.set("pccreate_time", DateTool.GetDateTime());
            r.set("pcmodify_time", DateTool.GetDateTime());
            r.set("pccreator_id", usu.getUserId());
            r.set("pcmodifier_id", usu.getUserId());
            r.set("pcdesc", "");
            boolean flag=Db.save("w_product_category",r);
            if (flag){
                jhm.putCode(1).putMessage("添加成功");
            }else{
                jhm.putCode(0).putMessage("添加失败");
            }

        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record异常");
        }

        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"message\":\"添加成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改商品分类
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/product/category/updateProductTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  id      string                  不允许   分类id
     * 	name	string		            不允许	 分类名称
     *  type    string                  不允许   上级分类
     *  sort    string                  不允许   分类排序
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
    public void updateProductTypeById(){
        JsonHashMap jhm=new JsonHashMap();
        /**
         * 接收前端参数
         */
        //商品分类id
        String id=getPara("id");
        //商品分类名称
        String name=getPara("name");
        //商品分类排序
        String sort=getPara("sort");

        //非空验证
        if (StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("商品分类id为空 ");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(name)){
            jhm.putCode(0).putMessage("商品分类名称为空");
            renderJson(jhm);
            return;
        }
//        if (StringUtils.isEmpty(sort)){
//            jhm.putCode(0).putMessage("商品分类排序为空");
//            renderJson(jhm);
//            return;
//        }

        try{
            Db.update("UPDATE w_product_category SET pcname=?,pcsort=? WHERE pcid=?",name,sort,id);
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
     * @date 2018-9-22
     * 名称  	删除商品分类
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/product/category/deleteProductTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  id      string                  不允许   分类id
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
    public void deleteProductTypeById(){
        JsonHashMap jhm=new JsonHashMap();
        /**
         * 接收前端参数
         */
        //商品分类id
        String id=getPara("id");

        //非空验证
        if (StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("商品分类id为空");
            renderJson(jhm);
            return;
        }

        try{
            Map<String,String> paraMap=new HashMap<>();
            paraMap.put("id", id);
            ProductCategoryService srv = enhance(ProductCategoryService.class);
            jhm = srv.deleteProductTypeById(paraMap);

        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record异常");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"message\":\"删除成功！\"}");
    }

    /**
     * URL  http://localhost:8080/weChatMallMgr/wm/pc/product/category/showCategoryById
     * */

    public void showCategoryById(){
        JsonHashMap jhm=new JsonHashMap();
        //商品分类id
        String id=getPara("id");

        //非空验证
        if (StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("商品分类id为空");
            renderJson(jhm);
            return;
        }

        String sql1="SELECT pcname AS name,pcid AS id,pcsort AS sort FROM w_product_category WHERE pcid=?";

        try{

            Record r=Db.findFirst(sql1,id);
            jhm.putCode(1);
            jhm.put("data",r);

        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record异常");
        }
        renderJson(jhm);
    }

}
