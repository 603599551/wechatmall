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
     * URL	    http://localhost:8080/wm/pc/customer/listCustomer
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
        renderJson("{\"code\":\"1\",\"data\":{\"totalRow\":1,\"pageNumber\":1,\"firstPage\":\"true\",\"lastPage\":\"true\",\"totalPage\":1,\"pageSize\":10,\"list\":[{\"name\":\"客户姓名\",\"gender\":\"客户性别\",\"phone\":\"客户电话\",\"type\":\"客户类型\",\"group\":\"客户分组\",\"createTime\":\"创建时间\"}]}}");
    }


    /**
     * @author liushiwen
     * @date 2018-9-25
     * 名称  	查看客户信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/showCustomerById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     * id           string                不允许     客户id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code":"1",
        "data":{
        "id":"用户id",
        "customerName":"用户姓名",
        "gender":"用户性别",
        "customerPhone":"用户电话",
        "wechatNumber":"用户微信号",
        "list":[{
        "receiverName":"收货人姓名",
        "receiverPhone":"收货人电话",
        "receiverAddress":"收货人地址"
        }],
        "type":"用户类型",
        "group":"用户分组",
        "createTime":"创建时间",
        "desc":"备注"
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
    public void showCustomerById(){
        renderJson("{\"code\":\"1\",\"data\":{\"id\":\"用户id\",\"customerName\":\"用户姓名\",\"gender\":\"用户性别\",\"customerPhone\":\"用户电话\",\"wechatNumber\":\"用户微信号\",\"list\":[{\"receiverName\":\"收货人姓名\",\"receiverPhone\":\"收货人电话\",\"receiverAddress\":\"收货人地址\"}],\"type\":\"用户类型\",\"group\":\"用户分组\",\"createTime\":\"创建时间\",\"desc\":\"备注\"}}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-25
     * 名称  	修改客户信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/modifyCustomerById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	    类型	       最大长度	允许空	 描述
     * id           string                不允许     客户id
     * group        string                不允许     客户分组
     * desc         string                允许       客户备注
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
    public void modifyCustomerById(){
        renderJson("{\"code\":\"1\",\"message\":\"修改成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-25
     * 名称  	设置客户类型
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/setTypeById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	    类型	       最大长度	允许空	 描述
     * id           string                不允许     客户id
     * type         string                不允许     客户类型
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":"1",
    "message":"设置成功！"
    }
     * 失败：
     * {
    "code":"0",
    "message":"设置失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void setTypeById(){
        renderJson("{\"code\":\"1\",\"message\":\"设置成功！\"}");
    }
}
