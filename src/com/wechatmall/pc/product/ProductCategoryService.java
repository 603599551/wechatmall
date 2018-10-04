package com.wechatmall.pc.product;
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


public class ProductCategoryService  extends BaseService{
    /*
    增加事务
     */
    @Before(Tx.class)
    public JsonHashMap deleteProductTypeById(Map paraMap){
        JsonHashMap jhm=new JsonHashMap();
        String id = (String) paraMap.get("id");

        Db.update("UPDATE w_product SET pcid=(SELECT pcid FROM w_product_category WHERE pcname='默认类') WHERE pcid=?",id);
        Db.delete("DELETE FROM w_product_category WHERE pcid=?",id);
        jhm.putCode(1).putMessage("删除成功");

        return jhm;
    }
}
