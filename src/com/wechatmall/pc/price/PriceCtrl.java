package com.wechatmall.pc.price;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * PriceCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class PriceCtrl extends BaseCtrl{
    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询定价信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/listPricesByGroupId
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  groupIdList    array                  不允许      选中的分组
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "productsList":[
    {
    "productName":"海飞丝",
    "originalPrice":"200.00",
    "presentPrice":"180.00"
    },
    {
    "productName":"海飞丝",
    "originalPrice":"200.00",
    "presentPrice":"180.00"
    }
    ]
    }
     * 失败：
     * {
    "code":"0",
    "message":"查看定价信息失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void listPricesByGroupId(){
        JsonHashMap jhm = new JsonHashMap();

        //选中的分组
        String groupId = getPara("groupId");

        //进行非空判断
        if(StringUtils.isEmpty(groupId)){
            jhm.putCode(0).putMessage("没有选中分组！");
            renderJson(jhm);
            return;
        }

        /**
         * 根据商品表w_product和商品现价表w_product_currentPrice关联查询 : 商品名称 : productName
         * 商品原价 : originalPrice , 商品现价 : presentPrice
         * */
        String sql = "SELECT wp.pname as productName, wp.price as originalPrice, wpc.pcpcurrent_price as presentPrice,wpc.pcpid AS goodsId FROM w_product_currentprice wpc, w_product wp WHERE wpc.pid = wp.pid and wpc.cgid = ?";

        try {
            List<Record> recordList = Db.find(sql, groupId);
            if(recordList == null && recordList.size() == 0){
                jhm.putCode(0).putMessage("分组无记录！");
                renderJson(jhm);
                return;
            }
            jhm.putCode(1).put("productsList", recordList);
        } catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器出错！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"productsList\":[{\"productName\":\"海飞丝\",\"originalPrice\":\"200.00\",\"presentPrice\":\"180.00\"},{\"productName\":\"海飞丝\",\"originalPrice\":\"200.00\",\"presentPrice\":\"180.00\"}]}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	提交定价信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/submitPrices
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  groupIdList    array                  不允许      选中的分组列表，内为选中分组ID
     *  pricesList     json                 不允许       商品列表带有商品现价
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "message":"更新定价成功"
    }
     * 失败：
     * {
    "code":"0",
    "message":"更新定价失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void submitPrices(){
        JsonHashMap jhm = new JsonHashMap();
        String groupId = getPara("groupId");
        String price = getPara("pricesList");


        //进行非空判断
        if(StringUtils.isEmpty(groupId)){
            jhm.putCode(0).putMessage("分组不能为空！");
            renderJson(jhm);
            return;
        }
        if(StringUtils.isEmpty(price)){
            jhm.putCode(0).putMessage("请填入现价！");
            renderJson(jhm);
            return;
        }
        JSONArray pricesList = JSONArray.fromObject(price);

        //将传回来的json数组里的商品id处理然后添加到sql语句里
        StringBuilder stringBuilder = new StringBuilder(pricesList.getJSONObject(0).getString("goodsId"));
        for(int i = 1; i < pricesList.size(); i++){
            stringBuilder.append("," + pricesList.getJSONObject(i).getString("goodsId"));
        }

        String priceSearch = "SELECT pcpid, pid, cgid, pcpcurrent_price, pcpcreate_time, pcpmodify_time, pcpcreator_id, pcpmodifier_id, pcpdesc from w_product_currentprice wpc where wpc.cgid = '1' and wpc.pid IN("+stringBuilder+")";

        try {
            List<Record> recordList = Db.find(priceSearch);

            if(recordList.size() != pricesList.size()){
                jhm.putCode(0).putMessage("传入商品id有误！");
                renderJson(jhm);
                return;
            }

            //将商品新改的价格放入recordList里面
            for(int i = 0; i < pricesList.size(); i++){
                for(int j = 0; j < recordList.size(); j++){
                    if(StringUtils.equals(pricesList.getJSONObject(i).getString("goodsId"), recordList.get(j).getStr("pid"))){
                        recordList.get(j).set("pcpcurrent_price", pricesList.getJSONObject(i).getString("presentPrice"));
                        continue;
                    }
                }
            }
            Db.batchUpdate("w_product_currentPrice","pcpid", recordList, recordList.size());
            jhm.putCode(1).putMessage("更新定价成功！");
        } catch (Exception e) {
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"message\":\"更新定价成功\"}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询分组信息
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/wm/pc/customer/listGroupsAndCustomers
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	       类型	       最大长度	允许空	  描述
     *  groupName      String                  不允许      分组名称
     *  groupNumber     String                 不允许      分组编号
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code":1,
    "groupList":[
    {
    "groupId":"gx00001",
    "groupName":"分组1",
    "list":[
    {
    "merchantNumber":"mc00001",
    "merchantName":"商户1"
    },
    {
    "merchantNumber":"mc00002",
    "merchantName":"商户2"
    }
    ]
    },
    {
    "groupId":"gx00001",
    "groupName":"分组1",
    "list":[
    {
    "merchantNumber":"mc00001",
    "merchantName":"商户1"
    },
    {
    "merchantNumber":"mc00002",
    "merchantName":"商户2"
    }
    ]
    }
    ]
    }
     * 失败：
     * {
    "code":"0",
    "message":"查询分组失败！"
    }
     * 报错：
     * {
    "code": -1,
    "message": "服务器发生异常！"
     * }
     */
    public void listGroupsAndCustomers(){
        JsonHashMap jhm = new JsonHashMap();

        List <Record> result = new ArrayList<>();

        List<Object> params = new ArrayList<>();

        String groupName = getPara("groupName");
        String groupNumber = getPara("groupNumber");

        /**
         * 根据客户信息表查询 : 客户id : groupId , 客户姓名 : groupName
         */
        StringBuilder groupSql = new StringBuilder("SELECT cgid as id, cgname as label FROM  w_customer_group where 1 = 1 ");

        /**
         * 根据客户信息表和客户分组表双表关联查询 : "merchantNumber":"商户id" , "merchantName":"商户姓名"
         */
        StringBuilder customerSql = new StringBuilder("SELECT wc.cgid, wc.cid as id, wc.cname as label FROM w_customer wc, w_customer_group wcg WHERE wc.cgid = wcg.cgid ");

        if(!StringUtils.isEmpty(groupName)){
            groupName = "%"+ groupName +"%";
            groupSql.append(" and cgname like ? ");
            customerSql.append(" and wcg.cgname like ?");
            params.add(groupName);
        }

        if(!StringUtils.isEmpty(groupNumber)){
            groupSql.append(" and cgid = ? ");
            customerSql.append(" and wc.cgid = ? ");
            params.add(groupNumber);
        }

        try {
            List <Record> recordList = Db.find(groupSql.toString(), params.toArray());
            List <Record> records = Db.find(customerSql.toString(), params.toArray());

            for(int i = 0; i < recordList.size(); i++){
                for(int j = 0; j < records.size(); j++){
                    if(StringUtils.equals(records.get(j).getStr("cgid"), recordList.get(i).getStr("id"))){
                        records.get(j).set("parent_id", recordList.get(i).getStr("id"));
                        records.get(j).remove("cgid");
                        result.add(records.get(j));
                    }
                }
                recordList.get(i).set("children", result);
                result = new ArrayList<>();
            }
            jhm.putCode(1).put("groupList", recordList);
        } catch (Exception e) {
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);

        //renderJson("{\"code\":1,\"groupList\":[{\"groupId\":\"gx00001\",\"groupName\":\"分组1\",\"list\":[{\"merchantNumber\":\"mc00001\",\"merchantName\":\"商户1\"},{\"merchantNumber\":\"mc00002\",\"merchantName\":\"商户2\"}]},{\"groupId\":\"gx00001\",\"groupName\":\"分组1\",\"list\":[{\"merchantNumber\":\"mc00001\",\"merchantName\":\"商户1\"},{\"merchantNumber\":\"mc00002\",\"merchantName\":\"商户2\"}]}]}");
    }
}
