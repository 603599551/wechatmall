package com.wechatmall.pc.product;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.utils.UserSessionUtil;
import easy.util.DateTool;
import easy.util.UUIDTool;
import utils.bean.JsonHashMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ProductManageService {

    /*
    增加事务
     */
    @Before(Tx.class)
    public JsonHashMap add(Map paraMap){
        JsonHashMap jhm = new JsonHashMap();

        String num = (String)paraMap.get("num");
        String type = (String)paraMap.get("type");
        String name = (String)paraMap.get("name");
        String price = (String)paraMap.get("price");
        String keyword = (String)paraMap.get("keyword");
        String pictureUrl = (String)paraMap.get("picture");
        String content = (String)paraMap.get("content");
        String sketch = (String)paraMap.get("sketch");
        UserSessionUtil usu = (UserSessionUtil)paraMap.get("usu");


        try {
            String pid = UUIDTool.getUUID();
            //装商品信息
            Record record = new Record();
            record.set("pid", pid);
            record.set("pnum", num);
            record.set("pcid", type);
            record.set("pname", name);
            record.set("price", price);
            record.set("pkeyword", keyword);
            record.set("picture", pictureUrl);
            record.set("pintroduction", sketch);
            record.set("pdetail", content);
            record.set("pcreate_time", DateTool.GetDateTime());
            record.set("pmodify_time", DateTool.GetDateTime());
            record.set("pcreator_id", usu.getUserId());
            record.set("pmodifier_id", usu.getUserId());
            record.set("pstatus", "on_sale");

            boolean flag = Db.save("w_product", record);

            if(!flag){
                jhm.putCode(0).putMessage("插入w_product失败!");
                return jhm;
            }

            /**
             * 根据w_customer_group查询分组数量
             */
            List<Record> records = Db.find("SELECT cgid FROM w_customer_group");
            for (Record r :  records){
                r.set("pcpid", UUIDTool.getUUID());
                r.set("pid", pid);
                r.set("pcpcurrent_price", price);
                r.set("pcpcreate_time", DateTool.GetDateTime());
                r.set("pcpmodify_time", DateTool.GetDateTime());
                r.set("pcpcreator_id", usu.getUserId());
                r.set("pcpmodifier_id", usu.getUserId());
            }

            int[] i = Db.batchSave("w_product_currentprice",records,records.size());

            if(i.length != records.size()){
                jhm.putCode(0).putMessage("插入w_product_currentprice失败!");
                return jhm;
            }
            jhm.putCode(1).putMessage("添加成功！");
        } catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage(e.toString());
        }
        return jhm;
    }

}
