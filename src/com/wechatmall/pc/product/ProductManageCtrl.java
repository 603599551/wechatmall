package com.wechatmall.pc.product;

import com.common.controllers.BaseCtrl;

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
     * 	type	string		            不允许	 查询添加，按照分组名称模糊查询
     *  name    string                  不允许   查询添加，按照商品名称模糊查询
     *  status  string                  不允许   查询添加，按照上架状态完全匹配查询
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
        renderJson("{\"code\":1,\"data\":{\"totalRow\":1,\"pageNumber\":1,\"firstPage\":true,\"lastPage\":true,\"totalPage\":1,\"pageSize\":10,\"list\":[{\"id\":\"商品id\",\"type\":\"所属分类\",\"name\":\"商品名称\",\"pictureUrl\":\"商品图片\",\"price\":\"商品价格\",\"status\":\"上架状态\",\"creator\":\"发布人\",\"createTime\":\"创建时间\"}]}}");
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
        renderJson("{\"code\":\"1\",\"message\":\"添加成功！\"}");
    }

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
        renderJson("{\"code\":\"1\",\"message\":\"修改成功！\"}");
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
        renderJson("{\"code\":\"1\",\"message\":\"删除成功！\"}");
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
        renderJson("{\"code\":\"1\",\"data\":{\"id\":\"商品id\",\"name\":\"商品名称\",\"price\":\"商品价格\",\"keyword\":\"关键字1，关键字2，关键字3\",\"pictureUrl\":\"商品图片\",\"type\":\"商品分类\"}}");
    }


}
