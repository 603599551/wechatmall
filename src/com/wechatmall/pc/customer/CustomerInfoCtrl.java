package com.wechatmall.pc.customer;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import easy.util.DateTool;
import easy.util.NumberUtils;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/customer/info/listCustomer
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
        JsonHashMap jhm=new JsonHashMap();
        /**
         * 接收前端参数
         */
        //客户姓名
        String name=getPara("name");
        //客户类型
        String type=getPara("type");
        //客户所在组
        String groupId=getPara("group");
        //页码
        String pageNumStr=getPara("pageNumber");
        //每页限制的记录数
        String pageSizeStr=getPara("pageSize");

        //为空时赋予默认值
        int pageNum = NumberUtils.parseInt(pageNumStr, 1);
        int pageSize = NumberUtils.parseInt(pageSizeStr, 10);

        //关联查询customer,customer_group,dictionary表得到 客户id，性别，类型，联系电话，创建时间，所在组
        String select="SELECT c.cid AS id,c.cname AS name,c.cwechat AS openId,c.ctype AS value,dd.name AS 'type',c.cphone AS phone,c.ccreate_time AS createTime,cg.cgname AS 'groupName',cg.cgid AS 'groupId',c.cremark AS 'desc' ";

        StringBuilder sql=new StringBuilder("FROM w_customer c LEFT JOIN w_dictionary dd ON c.ctype=dd.value,w_customer_group cg WHERE c.cgid=cg.cgid ");

        List<Object> params = new ArrayList<>();

        //查询添加，按照客户姓名模糊查询
        if (StringUtils.isNotEmpty(name)){
            sql=sql.append(" AND c.cname LIKE CONCAT('%',?,'%')");
            params.add(name);
        }
        //查询添加，按照客户类型完全匹配查询
        if (StringUtils.isNotEmpty(type)){
            sql=sql.append(" AND c.ctype=?");
            params.add(type);
        }
        //查询添加，按照客户所在组完全匹配查询
        if (StringUtils.isNotEmpty(groupId)){
            sql=sql.append(" AND c.cgid=?");
            params.add(groupId);
        }

        sql=sql.append(" ORDER BY c.ccreate_time ASC ,c.ctype DESC");

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
//        renderJson("{\"code\":\"1\",\"data\":{\"totalRow\":1,\"pageNumber\":1,\"firstPage\":\"true\",\"lastPage\":\"true\",\"totalPage\":1,\"pageSize\":10,\"list\":[{\"name\":\"客户姓名\",\"gender\":\"客户性别\",\"phone\":\"客户电话\",\"type\":\"客户类型\",\"group\":\"客户分组\",\"createTime\":\"创建时间\"}]}}");
    }


    /**
     * @author liushiwen
     * @date 2018-9-25
     * 名称  	查看客户信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/customer/info/showCustomerById
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
        JsonHashMap jhm=new JsonHashMap();
        //接收前端参数 客户id
        String id=getPara("id");

        //非空验证
        if (StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("客户id为空");
            renderJson(jhm);
            return;
        }

        //根据客户id关联查询customer,customer_group,dictionary表得到 客户id，姓名，性别，类型，联系电话，创建时间，所在组，微信号，备注
        String sql1="SELECT c.cid AS id,c.cname AS customerName,d.name AS gender,dd.name AS 'type',c.cphone AS customerPhone,c.ccreate_time AS createTime,cg.cgname AS 'group',cwechat AS wechatNumber ,cremark AS 'desc' FROM w_customer c LEFT JOIN w_dictionary d ON d.value=c.cgender LEFT JOIN w_dictionary dd ON dd.value=c.ctype,w_customer_group cg WHERE c.cgid=cg.cgid  AND cid=?";
        //根据客户id查customer_address表得到多个 收货地址信息
        String sql2="SELECT CONCAT(caprovince,cacity,cadistrict,castreet,caaddress) AS receiverAddress,caphone AS receiverPhone,caname AS receiverName FROM w_customer_address WHERE cid =?";

        try{

            Record r=Db.findFirst(sql1,id);
            List<Record> addressList=Db.find(sql2,id);
            if (r==null){
                jhm.putCode(0).putMessage("没有该客户的信息");
                renderJson(jhm);
                return;
            }
            r.set("list",addressList);
            jhm.putCode(1);
            jhm.put("data",r);

        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record异常");
        }
        renderJson(jhm);
        //renderJson("{\"code\":\"1\",\"data\":{\"id\":\"用户id\",\"customerName\":\"用户姓名\",\"gender\":\"用户性别\",\"customerPhone\":\"用户电话\",\"wechatNumber\":\"用户微信号\",\"list\":[{\"receiverName\":\"收货人姓名\",\"receiverPhone\":\"收货人电话\",\"receiverAddress\":\"收货人地址\"}],\"type\":\"用户类型\",\"group\":\"用户分组\",\"createTime\":\"创建时间\",\"desc\":\"备注\"}}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-25
     * 名称  	修改客户信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/customer/info/modifyCustomerById
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
        JsonHashMap jhm=new JsonHashMap();
        /**
         * 接收前端参数
         */
        //客户id
        String id=getPara("id");
        //客户分组id
        String group=getPara("groupId");
        //客户备注
        String desc=getPara("desc");

        //非空验证
        if (StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("客户id为空");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(group)){
            jhm.putCode(0).putMessage("客户分组id为空");
            renderJson(jhm);
            return;
        }
//        if (StringUtils.isEmpty(desc)){
//            jhm.putCode(0).putMessage("客户备注为空");
//            renderJson(jhm);
//            return;
//        }

        try{

            //如果转到个人组，客户类型自动转为个人
            if (StringUtils.equals(group,"0fa26d8989954540855013d9659b0ba6")){
                Db.update("UPDATE w_customer SET cgid=?,cremark=?,cmodify_time=?,ctype='individual' WHERE cid=?",group,desc, DateTool.GetDateTime(),id);
            }else {
                Db.update("UPDATE w_customer SET cgid=?,cremark=?,cmodify_time=? WHERE cid=?",group,desc, DateTool.GetDateTime(),id);
            }

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
     * 名称  	设置客户类型
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/customer/info/setTypeById
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
        JsonHashMap jhm=new JsonHashMap();
        /**
         * 接收前端参数
         */
        //客户id
        String id=getPara("id");
        //客户类型
        String type=getPara("type");

        //非空验证
        if (StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("客户id为空");
            renderJson(jhm);
            return;
        }
        if (StringUtils.isEmpty(type)){
            jhm.putCode(0).putMessage("客户类型为空");
            renderJson(jhm);
            return;
        }


        try{

            if (StringUtils.equals(type,"individual")){
                Db.update("UPDATE w_customer SET ctype=?,cgid=(SELECT cgid FROM w_customer_group WHERE cgname='个人组') WHERE cid=?",type,id);
            }else {
                Db.update("UPDATE w_customer SET ctype=? WHERE cid=?",type,id);
            }
            jhm.putCode(1).putMessage("设置成功");
        }catch (ActiveRecordException e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("Record异常");
        }
        renderJson(jhm);


        //renderJson("{\"code\":\"1\",\"message\":\"设置成功！\"}");
    }

    /**
     * 商品所属分类下拉列表
     *  URL	    http://localhost:8080/weChatMallMgr/wm/pc/customer/info/showCustomerGroup
     * */
    public void showCustomerGroup(){
        JsonHashMap jhm = new JsonHashMap();

        String sql="SELECT cgid AS value,cgname AS name FROM w_customer_group";

        try {
            List<Record> list=Db.find(sql);
            Record r=new Record();
            r.set("value",null);
            r.set("name","全部");
            list.add(0,r);
            jhm.putCode(1).put("data", list);
        } catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
    }
}
