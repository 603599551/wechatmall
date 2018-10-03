package com.wechatmall.pc.customer;

import com.common.service.BaseService;
import com.jfinal.aop.Before;
import com.jfinal.json.Json;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.utils.UserSessionUtil;
import easy.util.DateTool;
import easy.util.UUIDTool;
import utils.bean.JsonHashMap;

import java.util.List;
import java.util.Map;

public class CustomerGroupService extends BaseService {

    /*
    增加事务
     */
    @Before(Tx.class)
    public JsonHashMap addCustomerGroup(Map paraMap){
        JsonHashMap jhm=new JsonHashMap();

        String groupName = (String) paraMap.get("groupName");
        int sort = (int) paraMap.get("sort");
        String userId = (String) paraMap.get("userId");

        //分组id
        String id= UUIDTool.getUUID();
        //查询product表得到所有商品id
        String sql="SELECT pid,price AS pcpcurrent_price FROM w_product ";

        //往customer_group表存储新的分组信息
        Record r=new Record();
        r.set("cgid",id);
        r.set("cgname", groupName);
        r.set("cgsort", sort);
        r.set("cgcreate_time", DateTool.GetDateTime());
        r.set("cgmodify_time", DateTool.GetDateTime());
        r.set("cgcreator_id", userId);
        r.set("cgmodifier_id", userId);
        r.set("cgdesc", "");
        boolean flag1= Db.save("w_customer_group",r);
        if (flag1){
            jhm.putCode(1).putMessage("添加成功");
        }else{
            jhm.putCode(0).putMessage("添加失败");
        }

        List<Record> productList=Db.find(sql);
        if (productList!=null||productList.size()>0){
            for (Record pr:productList){
                pr.set("pcpid",UUIDTool.getUUID());
                pr.set("cgid",id);
                pr.set("pcpcreate_time",DateTool.GetDateTime());
                pr.set("pcpmodify_time",DateTool.GetDateTime());
                pr.set("pcpcreator_id",userId);
                pr.set("pcpmodifier_id",userId);
                pr.set("pcpdesc","");
                boolean flag2=Db.save("w_product_currentprice",pr);
                if (!flag2){
                    jhm.putCode(0).putMessage("添加商品失败");
                }
            }
        }

        return jhm;
    }

    @Before(Tx.class)
    public JsonHashMap deleteCustomerGroupById(Map paraMap){
        JsonHashMap jhm=new JsonHashMap();

        String id = (String) paraMap.get("id");

        Db.update("UPDATE w_customer SET cgid=(SELECT cgid FROM w_customer_group WHERE cgname='个人组') WHERE cgid=?",id);
        Db.delete("DELETE FROM w_customer_group WHERE cgid=?",id);
        Db.delete("DELETE FROM w_product_currentprice WHERE cgid=?",id);

        jhm.putCode(1).putMessage("删除成功");
        return jhm;
    }
}
