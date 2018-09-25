package com.wechatmall.pc.product;

import com.common.controllers.BaseCtrl;

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
     * URL	    http://localhost:8080/wm/pc/customer/listProductType
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
        renderJson("{\"code\":1,\"data\":{\"totalRow\":\"1\",\"pageNumber\":\"1\",\"firstPage\":\"true\",\"lastPage\":\"true\",\"totalPage\":\"1\",\"pageSize\":\"10\",\"list\":[{\"id\":\"分类id\",\"name\":\"分类名称\",\"type\":\"上级分类\",\"sort\":\"分类排序\"}]}}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	添加商品分类
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/addProductType
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
        renderJson("{\"code\":\"1\",\"message\":\"添加成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改商品分类
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/updateProductTypeById
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
        renderJson("{\"code\":\"1\",\"message\":\"修改成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	删除商品分类
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/deleteProductTypeById
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
        renderJson("{\"code\":\"1\",\"message\":\"删除成功！\"}");
    }

}
