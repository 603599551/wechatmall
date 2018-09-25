package com.wechatmall.pc.system;

import com.common.controllers.BaseCtrl;
/**
 * StoreCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class StoreCtrl extends BaseCtrl {

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	自提点列表
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/listStore
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	    类型	       最大长度	允许空	 描述
     *  storeName      string                不允许  查询添加，根据自提点名称模糊查询
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code": 1,
    "data": {
    "totalRow": 1,
    "pageNumber": 1,
    "firstPage": true,
    "lastPage": true,
    "totalPage": 1,
    "pageSize": 10,
    "list": [{
    "id": "自提点id",
    "storeName": "自提点名称",
    "managerName": "管理员姓名",
    "cityName": "城市名称",
    "address": "地址",
    "workTime": "工作时间",
    "managerPhone": "联系电话",
    "status": "启用状态"
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
    public void listStore(){
        renderJson("{\"code\":1,\"data\":{\"totalRow\":1,\"pageNumber\":1,\"firstPage\":true,\"lastPage\":true,\"totalPage\":1,\"pageSize\":10,\"list\":[{\"id\":\"自提点id\",\"storeName\":\"自提点名称\",\"managerName\":\"管理员姓名\",\"cityName\":\"城市名称\",\"address\":\"地址\",\"workTime\":\"工作时间\",\"managerPhone\":\"联系电话\",\"status\":\"启用状态\"}]}}");
    }


    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	添加自提点
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/addStore
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  storeName       string                不允许   自提点名称
     *  cityName        string                不允许   城市名称
     *  managerName     string                不允许   管理员姓名
     *  address         string                不允许   自提点地址
     *  workTime        string                不允许   营业时间
     *  phone           string                不允许   联系电话
     *  longitude       string                不允许   经度
     *  latitude        string                不允许   纬度
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
    public void addStore(){
        renderJson("{\"code\":\"1\",\"message\":\"添加成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	修改自提点
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/updateStoreById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  storeName       string                不允许   自提点名称
     *  cityName        string                不允许   城市名称
     *  managerName     string                不允许   管理员姓名
     *  address         string                不允许   自提点地址
     *  workTime        string                不允许   营业时间
     *  phone           string                不允许   联系电话
     *  longitude       string                不允许   经度
     *  latitude        string                不允许   纬度
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
    public void updateStoreById(){
        renderJson("{\"code\":\"1\",\"message\":\"修改成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	删除自提点
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/deleteStoreById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  id       string                     不允许      自提点id
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
    public void deleteStoreById(){
        renderJson("{\"code\":\"1\",\"message\":\"删除成功！\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	根据id查询自提点信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/showStoreById
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  id       string                     不允许      自提点id
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code": "1",
    "message":"查询成功！",
    "data": {
    "id": "自提点id",
    "storeName": "自提点名称",
    "cityName": "城市名称",
    "managerName":"管理者姓名",
    "address": "地址",
    "phone": "联系电话",
    "longitude": "经度",
    "latitude":"纬度"
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
    public void showStoreById(){
        renderJson("{\"code\":\"1\",\"message\":\"查询成功！\",\"data\":{\"id\":\"自提点id\",\"storeName\":\"自提点名称\",\"cityName\":\"城市名称\",\"managerName\":\"管理者姓名\",\"address\":\"地址\",\"phone\":\"联系电话\",\"longitude\":\"经度\",\"latitude\":\"纬度\"}}");
    }

}
