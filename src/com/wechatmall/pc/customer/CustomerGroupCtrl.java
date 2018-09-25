package com.wechatmall.pc.customer;

import com.common.controllers.BaseCtrl;
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
     * URL	    http://localhost:8080/wm/pc/customer/listCustomerGroup
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
        renderJson("{\"code\":\"1\",\"data\":{\"totalRow\":\"1\",\"pageNumber\":\"1\",\"firstPage\":\"true\",\"lastPage\":\"true\",\"totalPage\":\"1\",\"pageSize\":\"10\",\"list\":[{\"id\":\"分组id\",\"groupName\":\"分组名称\",\"sort\":\"分组排序\"}]}}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-25
     * 名称  	添加分组
     * 描述
     * 验证
     * 权限	    无
     * URL	   http://localhost:8080/wm/pc/customer/addCustomerGroup
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
        renderJson("{\"caozuo\":\"记得往商品定价表中增加记录！！\",\"code\":\"1\",\"message\":\"添加成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-25
     * 名称  	修改分组
     * 描述
     * 验证
     * 权限	    无
     * URL	   http://localhost:8080/wm/pc/customer/updateCustomerGroupById
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
        renderJson("{\"code\":\"1\",\"message\":\"修改成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-25
     * 名称  	删除分组
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/deleteCustomerGroupById
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
        renderJson("{\"code\":\"1\",\"message\":\"删除成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-25
     * 名称  	查看分组
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/showCustomerGroupById
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
        renderJson("{\"code\":\"1\",\"data\":{\"id\":\"分组id\",\"groupName\":\"分组名称\",\"sort\":\"分组排序\"}}");
    }

}
