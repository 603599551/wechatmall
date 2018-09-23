package com.wechatmall.pc.customer;

import com.common.controllers.BaseCtrl;

/**
 * customerCtrl class
 * @author zhanjinqi
 * @date   2018-9-21
 */
public class CustomerInfoCtrl extends BaseCtrl{
    /**
     * @author zhanjinqi
     * @date 2018-9-21
     * 名称  	查询客户列表页面
     * 描述	    默认显示所有客户信息
     *          查询添加，按照客户姓名模糊查询
     *          查询添加，按照客户类型完全匹配查询
     *          查询添加，按照客户所在组完全匹配查询
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wcos/customer/listCustomer
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	描述
     * name	   string		         允许	查询添加，按照客户姓名模糊查询
     * type	   string		         允许	查询添加，按照客户类型完全匹配查询
     * group   string                允许   查询添加，按照客户所在组完全匹配查询
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":"1",
    "data":{
    "totalRow": "1",
    "pageNumber": "1",
    "firstPage": "true",
    "lastPage": "true",
    "totalPage": "1",
    "pageSize": "10",
    "list":[{
    "name":"客户姓名",
    "gender":"客户性别",
    "phone":"客户电话",
    "type":"客户类型",
    "group":"客户分组",
    "createTime":"创建时间"
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

    public void listCustomer(){
        renderJson("{\"code\":\"1\",\"data\":{\"totalRow\":\"1\",\"pageNumber\":\"1\",\"firstPage\":\"true\",\"lastPage\":\"true\",\"totalPage\":\"1\",\"pageSize\":\"10\",\"list\":[{\"name\":\"客户姓名\",\"gender\":\"客户性别\",\"phone\":\"客户电话\",\"type\":\"客户类型\",\"group\":\"客户分组\",\"createTime\":\"创建时间\"}]}}");
    }


    public void showCustomerById(){

    }

    public void modifyCustomerById(){

    }

    public void setTypeById(){

    }
}
