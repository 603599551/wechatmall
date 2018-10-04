package com.wechatmall.mobile.my;

import com.common.service.BaseService;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import easy.util.DateTool;
import easy.util.UUIDTool;
import org.apache.commons.lang.StringUtils;
import utils.bean.JsonHashMap;

import java.util.List;
import java.util.Map;

public class MyService extends BaseService {


    /**
     * 事物管理
     * 新增收货地址
     *
     * */
    @Before(Tx.class)
    public JsonHashMap addHarvestAddress(Map paraMap){
        JsonHashMap jhm=new JsonHashMap();
        //接收MyCtrl的参数
        String caid = (String) paraMap.get("caid");
        String cid = (String) paraMap.get("cid");
        String caname = (String) paraMap.get("caname");
        String caphone = (String) paraMap.get("caphone");
        String castatus = (String) paraMap.get("castatus");
        String caprovince = (String) paraMap.get("caprovince");
        String cadistrict = (String) paraMap.get("cadistrict");
        String cacity = (String) paraMap.get("cacity");
        String castreet = (String) paraMap.get("castreet");
        String caaddress = (String) paraMap.get("caaddress");

        String time=DateTool.GetDateTime();
        /**
         * 增加的收货地址记录
         */
        //按客户id查询收货地址表，如果记录为空，则新增一条收货地址并为默认，否则看前台传的参数
        //前台传参为默认地址，则将这个客户的所有地址都先设置为非默认，再新增一条记录
        String sql = "select count(1) from w_customer_address where cid = ? ";
        int count = Db.queryInt(sql,cid);
        Record record=new Record();
        if(count == 0 ){
            record.set("castatus", "1");
            modifyCustomerInformation(cid,caname,caphone,time);
        }else{
            if(castatus.equals("1")){
               Db.update("UPDATE w_customer_address SET castatus='0' WHERE cid =?",cid);
               modifyCustomerInformation(cid,caname,caphone,time);
            }
                record.set("castatus",castatus);
        }
        //新增地址记录
        record.set("caid", caid);
        record.set("cid", cid);
        record.set("sid", "");
        record.set("caname", caname);
        record.set("caphone", caphone);
        record.set("caprovince", caprovince);
        record.set("cacity", cacity);
        record.set("cadistrict", cadistrict);
        record.set("castreet", castreet);
        record.set("caaddress", caaddress);
        record.set("cacreate_time",time );
        record.set("camodify_time", time);
        record.set("cacreator_id", cid);
        record.set("camodifier_id", cid);
        record.set("cadesc", "");

        boolean flag=Db.save("w_customer_address",record);
            if(flag){
                jhm.putCode(1).putMessage("新增成功！");
            }else{
                jhm.putCode(0).putMessage("新增失败！");
            }
        return jhm;
    }

    /**
     * 事务管理
     * 删除收货地址
     * */
    @Before(Tx.class)
    public JsonHashMap deleteHarvestAddress(Map paraMap){
        JsonHashMap jhm=new JsonHashMap();
        //接收MyCtrl的参数
        String caid = (String) paraMap.get("caid");
        String cid = (String) paraMap.get("cid");
        //按收货地址id查询是否为默认状态，如果是，删除之后将客户的第一条地址设置为默认地址
        Record r = Db.findFirst("select castatus from w_customer_address where caid = ?",caid);
        /**
         * 删除收货地址记录
         */
        String sql = "DELETE from w_customer_address where (caid = ? and cid = ?)";
        int num = Db.delete(sql,caid,cid);
        if(num > 0){
            if("1".equals(r.get("castatus"))){
                List<Record> rr = Db.find("select caid,caname,caphone from w_customer_address where cid = ?",cid);
                Db.update("UPDATE w_customer_address SET castatus='1' WHERE caid =?",rr.get(0).getStr("caid"));
                modifyCustomerInformation(cid,rr.get(0).getStr("caname"), rr.get(0).getStr("caphone"),DateTool.GetDateTime());
            }
            jhm.putCode(1).putMessage("删除成功！");
        }else{
            jhm.putCode(0).putMessage("删除失败！");
        }
        return jhm;
    }

    /**
     * 事务管理
     * 修改收货地址
     * */
    @Before(Tx.class)
    public JsonHashMap modifyHarvestInformation(Map paraMap){
        JsonHashMap jhm=new JsonHashMap();
        //接收MyCtrl的参数
        String caid = (String) paraMap.get("caid");
        String cid = (String) paraMap.get("cid");
        String caname = (String) paraMap.get("caname");
        String caphone = (String) paraMap.get("caphone");
        String castatus = (String) paraMap.get("castatus");
        String caprovince = (String) paraMap.get("caprovince");
        String cadistrict = (String) paraMap.get("cadistrict");
        String cacity = (String) paraMap.get("cacity");
        String castreet = (String) paraMap.get("castreet");
        String caaddress = (String) paraMap.get("caaddress");

        String time=DateTool.GetDateTime();

        /**
         * 修改的收货地址记录
         */
        //判断默认收货地址是否改变，按收货地址id查询状态与前台返回状态是否一致
        //一致正常修改，不一致判断状态是0还是1
        //0：在其他收货地址中默认第一个为默认地址
        //1：将其他地址都改成非默认地址
        Record r = Db.findFirst("select castatus from w_customer_address where caid = ?",caid);
        if(!castatus.equals(r.getStr("castatus"))){
            if("1".equals(castatus)){
                Db.update("UPDATE w_customer_address SET castatus='0' WHERE cid =?",cid);
                modifyCustomerInformation(cid,caname,caphone,time);
            }else{
                List<Record> rr = Db.find("select caid,caname,caphone from w_customer_address where castatus='0'and  cid = ?",cid);
                Db.update("UPDATE w_customer_address SET castatus='1' WHERE caid =?",rr.get(0).getStr("caid"));
                modifyCustomerInformation(cid,rr.get(0).getStr("caname"), rr.get(0).getStr("caphone"),time);
            }
        }
        Record modifyHarvestAddress = new Record();
        modifyHarvestAddress.set("caid",caid);
        modifyHarvestAddress.set("cid",cid);
        modifyHarvestAddress.set("caname",caname);
        modifyHarvestAddress.set("caphone",caphone);
        modifyHarvestAddress.set("caprovince",caprovince);
        modifyHarvestAddress.set("cacity",cacity);
        modifyHarvestAddress.set("cadistrict",cadistrict);
        modifyHarvestAddress.set("castreet",castreet);
        modifyHarvestAddress.set("caaddress",caaddress);
        modifyHarvestAddress.set("camodify_time",DateTool.GetDateTime());
        modifyHarvestAddress.set("camodifier_id", UUIDTool.getUUID());
        modifyHarvestAddress.set("castatus",castatus);
        boolean flag = Db.update("w_customer_address","caid" ,modifyHarvestAddress);
        if(flag){
               // modifyCustomerInformation(cid,caname,caphone);
            jhm.putMessage("修改成功！");
        }else {
            jhm.putCode(0).putMessage("修改失败！");
        }
        return jhm;
    }

    //修改默认地址时自动修改客户表里的信息：姓名，电话，修改时间
    private void modifyCustomerInformation(String cid,String name,String phone,String time){
        Record r = new Record();
        r.set("cid",cid);
        r.set("cname",name);
        r.set("cphone",phone);
        r.set("cmodify_time",time);
        Db.update("w_customer","cid",r);
    }
}
