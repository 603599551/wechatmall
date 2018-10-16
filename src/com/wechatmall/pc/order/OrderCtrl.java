package com.wechatmall.pc.order;

import com.common.controllers.BaseCtrl;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.utils.UnitConversion;
import easy.util.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import utils.bean.JsonHashMap;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderCtrl class
 * @author liushiwen
 * @date   2018-9-25
 */
public class OrderCtrl extends BaseCtrl{

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查询订单列表页面
     * 描述
     * 验证
     * 权限	    无
     * URL	  http://localhost:8080/weChatMallMgr/wm/pc/order/listOrder
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  number  string                  不允许   查询添加，按照订单编号完全匹配查询
     * 	type	string		            不允许	 查询添加，按照客户类型完全匹配查询
     *  customerName    string          不允许   查询添加，按照客户姓名完全匹配查询
     *  status  string                  不允许   查询添加，按照订单状态完全匹配查询
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code": 1,
        "list":[{
        "orderId":"11da",
        "time":"2018/9/9",
        "customerName":"客户姓名",
        "receiverName":"收货人姓名",
        "phone":1313005589,
        "type":0,
        "address":"收货地址",
        "originalPrice":20.0,
        "presentPrice":18.0,
        "status":0
        },{
        "orderId":"11da",
        "time":"2018/9/9",
        "customerName":"客户姓名",
        "receiverName":"收货人姓名",
        "phone":1313005589,
        "type":0,
        "address":"收货地址",
        "originalPrice":20.0,
        "presentPrice":18.0,
        "status":0
        }]
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
    public void listOrder(){
        JsonHashMap jhm = new JsonHashMap();

        String number = getPara("number");
        String type = getPara("type");
        String status = getPara("status");
        String customerName = getPara("customerName");
        String pageNumStr = getPara("pageNumber");
        String pageSizeStr = getPara("pageSize");

        //为空时赋予默认值
        int pageNum = NumberUtils.parseInt(pageNumStr, 1);
        int pageSize = NumberUtils.parseInt(pageSizeStr, 10);

        try {
            /**
             * 根据订单表w_orderform和客户表w_customer关联查询：客户id：orderId, 客户姓名:customerName, 创建时间：time, 客户类型:type
             * 收货人姓名:receiverName, 收货人电话:phone, 收货地址:address, 应付价格:originalPrice, 实付价格:presentPrice, 订单状态:status
             * 订单状态对应中文 : statusName ,客户类型对应中文 : typeName
             */
            String select = "SELECT wo.oid AS orderId,wo.onum AS orderNum, wo.cname as customerName, wo.oname AS receiverName, wo.ophone AS phone, wo.ocreate_time AS time, wo.oaddress AS address, FORMAT(wo.ooriginal_sum, 2)AS originalPrice, FORMAT(wo.ocurrent_sum, 2)AS presentPrice, wo.ostatus AS status, wd.`name` AS statusName, wo.ctype AS type, wdd.name AS typeName ";

            StringBuilder sql = new StringBuilder("  FROM (SELECT o.oid ,o.onum, o.oname, wc.cname, o.cid, o.ophone , o.ocreate_time , o.oaddress , o.ooriginal_sum , o.ocurrent_sum , o.ostatus, wc.ctype from w_orderform o, w_customer wc WHERE o.cid = wc.cid");


            List<Object> params = new ArrayList<>();

            //对检索条件进行判断
            if(!StringUtils.isEmpty(number)){
                number = "%" + number + "%";
                sql.append(" and o.onum like ? ");
                params.add(number);
            }

            if(!StringUtils.isEmpty(type)){
                sql.append(" and wc.ctype = ? ");
                params.add(type);
            }

            if(!StringUtils.isEmpty(status)){
                sql.append(" and o.ostatus = ? ");
                params.add(status);
            }

            if(!StringUtils.isEmpty(customerName)) {
                sql.append(" and wc.cname LIKE CONCAT('%',?,'%') ");
                params.add(customerName);
            }
                sql.append(" )wo LEFT JOIN w_dictionary wd ON wo.ostatus = wd.`value` LEFT JOIN w_dictionary wdd ON wo.ctype = wdd.`value` ORDER BY wo.ocreate_time desc");


                Page<Record> page = Db.paginate(pageNum, pageSize, select, sql.toString(), params.toArray());
                jhm.put("data", page);
                jhm.putCode(1);
            } catch (Exception e){
                e.printStackTrace();
                jhm.putCode(-1).putMessage("服务器发生异常！");
            }
            renderJson(jhm);
        }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	查看订单详情
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/order/listOrderDetail
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  orderId  string                  不允许   订单id/编号
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
        "code": 1,
        "customerType":0,
        "orderId":"A5454845648",
        "transportType":"物流类型",
        "payType":"支付方式",
        "products":[{
        "name":"大米",
        "originalPrice":20.0,
        "presentPrice":18.0,
        "number":1,
        "singleSum":20.0
        },{
        "name":"大米",
        "originalPrice":20.0,
        "presentPrice":18.0,
        "number":1,
        "singleSum":20.0
        }],
        "orderOriginalSum":400,
        "orderPresentSum":380
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
    public void listOrderDetail(){
        JsonHashMap jhm = new JsonHashMap();

        String orderId = getPara("orderId");

        if(StringUtils.isEmpty(orderId)){
            jhm.putCode(0).putMessage("订单号不能为空！");
            renderJson(jhm);
            return;
        }

        try {
            /**
             * 根据w_orderform表查询是否存在该订单
             */
            String sql  = "SELECT count(*) as c from w_orderform where oid = ? ";
            Record r = Db.findFirst(sql, orderId);
            if(!StringUtils.equals("0", r.getStr("c"))){
                /**
                 * 根据订单表w_orderform和客户信息表w_customer双表关联查询: 订单id : orderId , 客户类型 : customerType , 物流类型 : transportType
                 * 支付类型 : payType , 订单原总价 : orderOriginalSum , 订单应付金额 : orderPresentSum , 支付类型名称 : payTypeName 还有个类型相应的中文
                 */
                String orderSearch  = "SELECT wo.oid as orderId,wo.onum AS orderNum, wc.ctype as customerType, wo.otransport_type transportType, wo.opay_type as payType,FORMAT(wo.ooriginal_sum,2)as orderOriginalSum,FORMAT(wo.ocurrent_sum,2)as orderPresentSum,wo.ocreate_time AS createTime, wd. NAME AS transportTypeName, wdd. NAME AS payTypeName, wddd. NAME AS customerTypeName FROM w_orderform wo LEFT JOIN w_dictionary wd ON wd. VALUE = wo.otransport_type LEFT JOIN w_dictionary wdd ON wdd. VALUE = wo.opay_type, w_customer wc LEFT JOIN w_dictionary wddd ON wc.ctype = wddd.`value` WHERE wo.oid = ? AND wc.cid = ( SELECT cid FROM w_orderform WHERE oid = ? )";
                Record record = Db.findFirst(orderSearch, orderId, orderId);

                /**
                 * 根据订单表w_orderform和订单详情表w_orderform_detail双表关联查询: 订单商品名称 : name , 订单商品原价 : originalPrice
                 * 订单商品现价 : presentPrice , 订单商品数量 : number , 订单商品小结 : singleSum
                 */
                String orderDetailSearch = "select wod.odname as name,FORMAT(wod.odoriginal_price,2)AS originalPrice,FORMAT(wod.odcurrent_price,2)as presentPrice, wod.odquantity as number,FORMAT((wod.odoriginal_price * wod.odquantity),2)as singleSum from w_orderform wo, w_orderform_detail wod where wo.oid = ? and wod.oid = wo.oid ";
                List<Record> recordList = Db.find(orderDetailSearch, orderId);
                jhm.put("data", record);
                jhm.put("products", recordList);
                jhm.putCode(1);
            } else {
                jhm.putCode(0).putMessage("该订单不存在！");
            }
        } catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\"customerType\":0,\"orderId\":\"A5454845648\",\"transportType\":\"物流类型\",\"payType\":\"支付方式\",\"products\":[{\"name\":\"大米\",\"originalPrice\":20,\"presentPrice\":18,\"number\":1,\"singleSum\":20},{\"name\":\"大米\",\"originalPrice\":20,\"presentPrice\":18,\"number\":1,\"singleSum\":20}],\"orderOriginalSum\":400,\"orderPresentSum\":380}");
    }

    /**
     * @author liushiwen
     * @date 2018-9-22
     * 名称  	打印订单详情
     * 描述
     * 验证
     * 权限	    无
     * URL	    http://localhost:8080/weChatMallMgr/wm/pc/order/print/printSingleOrder
     * 请求方式     post
     *
     * 请求参数：
     * 参数名	类型	       最大长度	允许空	 描述
     *  orderIdList  array                  不允许   订单id/编号
     *
     * 返回数据：
     * 返回格式：JSON
     * 成功：
     * {
    "code": 1,
    "list": [{
    "orderId": "A6464546548945",
    "customerName": "小明",
    "customerPhone": 13130005589,
    "address": "用户地址",
    "createTime": "2018-09-16 23:00:00",
    "transportType":"物流类型",
    "payType":"支付方式",
    "orderOriginalSum": 20.00,
    "orderPresentSum": 19.00,
    "productsList": [{
    "productId": "51564157854",
    "productName": "大米",
    "productNum": 1,
    "productPrice": 18.00
    }]
    },{
    "orderId": "A6464546548945",
    "customerName": "小明",
    "customerPhone": 13130005589,
    "address": "用户地址",
    "createTime": "2018-09-16 23:00:00",
    "transportType":"物流类型",
    "payType":"支付方式",
    "orderOriginalSum": 20.00,
    "orderPresentSum": 19.00,
    "productsList": [{
    "productId": "15615648748794",
    "productName": "大米",
    "productNum": 1,
    "productPrice": 18.00
    }]
    }]
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
//    public void printOrders(){
//        JsonHashMap jhm = new JsonHashMap();
//
//        String [] orderIdList = getParaValues("orderIdList");
//
//        //进行非空验证
//        if(orderIdList.length == 0 && orderIdList == null){
//            jhm.putCode(0).putMessage("尚未选择订单！");
//            renderJson(jhm);
//            return;
//        }
//
//        StringBuilder order = new StringBuilder(orderIdList[0]);
//        for(int t = 1; t < orderIdList.length; t++){
//            order.append(","+ orderIdList[t]);
//        }
//
//        //检测数据库中是否存在的sql语句
//        String sql = "SELECT count(*) as c from w_orderform where oid IN("+ order.toString() +") ";
//
//        //订单id
//        String orderId = "";
//
//        List<Record> result = new ArrayList<>();
//
//        /**
//         *根据订单表w_orderform和客户信息表w_customer查询 : 订单id : orderId , 客户姓名 : customerName , 客户电话 : customerPhone
//         * 收货地址 : address , 订单创建时间 : crateTime , 物流类型 : transportType , 支付方式 : payType
//         * 订单原总价 : orderOriginalSum , 订单总现价 : orderPresentSum , 订单支付状态 : orderPayType
//         */
//        String orderSearch = "SELECT wo.oid as orderId, wc.cname as customerName, wo.ostatus as orderPayStatus, wc.cphone as customerPhone, wo.oaddress as address, wo.ocreate_time as crateTime, wo.otransport_type as transportType, wo.opay_type as payType, wo.ooriginal_sum as orderOriginalSum, wo.ocurrent_sum as orderPresentSum FROM w_orderform wo, w_customer wc where wo.oid IN("+ order.toString() +") and wc.cid = wo.cid";
//
//        /**
//         *根据订单表w_orderform和订单详情表w_orderfrom_detail查询 : 商品名称 : name , 商品所属订单号 ： oid
//         * 订单商品原价 : originalPrice , 订单商品现价 : presentPrice , 订单商品数量 : number
//         */
//        String goodsSearch = "select wod.odname as name,wo.oid as orderId, wod.pid as productId, wod.odcurrent_price as presentPrice, wod.odquantity as number from w_orderform wo, w_orderform_detail wod, w_customer wc where wo.oid IN ("+ order.toString() +") and wod.oid = wo.oid ";
//
//        try {
//            Record r = Db.findFirst(sql);
//            if(StringUtils.equals(r.getStr("c"),String.valueOf(orderIdList.length))){
//                List<Record> recordList = Db.find(orderSearch);
//                List<Record> records = Db.find(goodsSearch);
//
//                //查出来之后对数据进行分类
//                for(int i = 0; i < orderIdList.length; i++){
//                    orderId = orderIdList[i];
//                    for(int j = 0; j < records.size() ; j++){
//                        if(StringUtils.equals(records.get(j).getStr("orderId"), orderId)){
//                            result.add(records.get(j));
//                        }
//                    }
//                    recordList.get(i).set("productsList", result);
//                    result = new ArrayList<>();
//                }
//                jhm.putCode(1).put("list", recordList);
//            } else {
//                jhm.putCode(0).putMessage("订单记录缺失！");
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//            jhm.putCode(-1).putMessage("服务器出错！");
//        }
//        renderJson(jhm);
//        //renderJson("{\"code\":1,\"list\":[{\"orderId\":\"A6464546548945\",\"customerName\":\"小明\",\"customerPhone\":13130005589,\"address\":\"用户地址\",\"createTime\":\"2018-09-16 23:00:00\",\"transportType\":\"物流类型\",\"payType\":\"支付方式\",\"orderOriginalSum\":20,\"orderPresentSum\":19,\"productsList\":[{\"productId\":\"51564157854\",\"productName\":\"大米\",\"productNum\":1,\"productPrice\":18}]},{\"orderId\":\"A6464546548945\",\"customerName\":\"小明\",\"customerPhone\":13130005589,\"address\":\"用户地址\",\"createTime\":\"2018-09-16 23:00:00\",\"transportType\":\"物流类型\",\"payType\":\"支付方式\",\"orderOriginalSum\":20,\"orderPresentSum\":19,\"productsList\":[{\"productId\":\"15615648748794\",\"productName\":\"大米\",\"productNum\":1,\"productPrice\":18}]}]}");
//    }
    /***
     *
     * URL  http://localhost:8080/weChatMallMgr/wm/pc/order/setOrderStatusById
     */
    public void setOrderStatusById(){
        JsonHashMap jhm = new JsonHashMap();

        //获取参数
        String id = getPara("id");
        String status = getPara("status");

        //进行非空判断
        if(StringUtils.isEmpty(id)){
            jhm.putCode(0).putMessage("id不能为空！");
            renderJson(jhm);
            return;
        }

        if(StringUtils.isEmpty(status)){
            jhm.putCode(0).putMessage("未选择修改内容！");
            renderJson(jhm);
            return;
        }

        try {
            Record record = Db.findFirst("SELECT * FROM w_orderform WHERE oid = ?", id);
            if(record == null){
                jhm.putCode(-1).putMessage("数据库无此数据！");
                renderJson(jhm);
                return;
            }
            switch (status){
                case "finished" : record.set("ostatus", "finished"); break;
                case "canceled" : record.set("ostatus", "canceled"); break;
                case "shipped" : record.set("ostatus", "shipped"); break;
            }

            boolean flag = Db.update("w_orderform","oid", record);

            if(flag){
                jhm.putCode(1).putMessage("修改成功！");
            } else {
                jhm.putCode(0).putMessage("修改数据库失败！");
            }
        } catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage("服务器发生异常！");
        }
        renderJson(jhm);
    }

    /**
     * 上传PDF
     * URL	http://localhost:8080/weChatMallMgr/wm/pc/order/uploadJPG
     */
    static class Preference {
        public static String _PATH = "upload\\";
    }

    public void uploadJPG(){
        JsonHashMap jhm=new JsonHashMap();
        HttpServletRequest request = getRequest();
        String basePath = request.getContextPath();
        //存储路径
        String path = getSession().getServletContext().getRealPath(Preference._PATH);
        UploadFile file = getFile("file");
        System.out.println(path);
        String fileName = "";
        if(file.getFile().length() > 200*1024*1024) {
            System.err.println("文件长度超过限制，必须小于200M");
        }else{
            //上传文件
            String type = file.getFileName().substring(file.getFileName().lastIndexOf(".")); // 获取文件的后缀
            fileName = System.currentTimeMillis() + type; // 对文件重命名取得的文件名+后缀
            String dest = path + "\\" + fileName;
            file.getFile().renameTo(new File(dest));
            String realFile = basePath + "/" + Preference._PATH +  fileName;
            String fName="\\"+fileName;
            setAttr("fName", fName);
            setAttr("url", realFile);
            jhm.putCode(1);
            jhm.put("filePath", realFile);
        }
        renderJson(jhm);
        //renderJson("{\"code\":1,\n" + "\"message\":\"上传成功\",\n" + "\"data\":\n" + "    {\"id\":\"cjlkh8gye000aj8g7cgvvwliv\",\"filePath\":\"pdf路径\"}\n" + "}\n");
    }

    /***
     *
     * URL  http://localhost:8080/weChatMallMgr/wm/pc/order/exportXls
     */
    public void exportXls(){
        //订单id
        String orderId = getPara("id");
        JsonHashMap jhm = new JsonHashMap();

        try{
            List<Record> list=Db.find("SELECT * FROM w_orderform_detail WHERE oid=? ORDER BY odcreate_time ASC ",orderId);
            if(list != null && list.size() > 0){
                List<List<String>> content = new ArrayList<>();

                Record order=Db.findFirst("SELECT o.*,d.name AS 'status',dd.name AS 'transport_type',ddd.name AS 'pay_type' FROM w_orderform o LEFT JOIN w_dictionary d ON d.value=o.ostatus LEFT JOIN w_dictionary dd ON dd.value=o.otransport_type LEFT JOIN w_dictionary ddd ON ddd.value=o.opay_type WHERE  oid=?",orderId);
                List<String> sumList = new ArrayList<>();
                sumList.add("订单编号");
                sumList.add(order.getStr("onum"));
                sumList.add("  ");
                sumList.add("订单状态");
                sumList.add(order.getStr("status"));
                sumList.add("  ");
                sumList.add("客户姓名");
                sumList.add(order.getStr("oname"));
                content.add(sumList);
                List<String> sumList2 = new ArrayList<>();
                sumList2.add("创建时间");
                sumList2.add(order.getStr("ocreate_time"));
                sumList2.add("  ");
                sumList2.add("物流类型");
                sumList2.add(order.getStr("transport_type"));
                sumList2.add("  ");
                sumList2.add("联系电话");
                sumList2.add(order.getStr("ophone"));
                content.add(sumList2);
                List<String> sumList3 = new ArrayList<>();
                sumList3.add("订单应付");
                sumList3.add(order.getStr("ooriginal_sum"));
                sumList3.add("  ");
                sumList3.add("支付方式");
                sumList3.add(order.getStr("pay_type"));
                sumList3.add("  ");
                sumList3.add("收货地址");
                sumList3.add(order.getStr("oaddress"));
                content.add(sumList3);
                List<String> sumList4 = new ArrayList<>();
                sumList4.add("实付款");
                sumList4.add(order.getStr("ocurrent_sum"));
                content.add(sumList4);
                List<String> sumList5 = new ArrayList<>();
                sumList5.add(" ");
                sumList5.add(" ");
                sumList5.add(" ");
                content.add(sumList5);

                List<String> titleList = new ArrayList<>();
                titleList.add("商品序号");
                titleList.add("商品名称");
                titleList.add("商品原价");
                titleList.add("商品现价");
                titleList.add("商品数量");
                titleList.add("单品总价");
                content.add(titleList);
                int len=list.size();
                for(int i = 0; i <len ; i++){
                    Record record = list.get(i);
                    List<String> contentList = new ArrayList<>();
                    contentList.add(i + 1 + "");
                    contentList.add(record.getStr("odname"));
                    contentList.add(record.getStr("odoriginal_price"));
                    contentList.add(record.getStr("odcurrent_price"));
                    contentList.add(record.getStr("odquantity"));
                    contentList.add(record.getStr("odsingle_sum"));
                    content.add(contentList);
                }


                String fileName = exportXlsFile(content, getSession().getServletContext().getRealPath("") + "/exportOrderXls/" + order.getStr("onum") + ".xls");
                jhm.put("pageUrl", getRequest().getScheme()+"://"+getRequest().getServerName()+":"+getRequest() .getServerPort()  +getRequest().getContextPath()+  "/exportOrderXls/" + order.getStr("onum") + ".xls");
            }else{
                jhm.putCode(0);
                jhm.putMessage("订单无数据，不能导出！");
            }
        }catch (Exception e){
            e.printStackTrace();
            jhm.putCode(-1).putMessage(e.toString());
        }
        renderJson(jhm);
    }

    private String exportXlsFile(List<List<String>> content, String name) throws Exception{
        if(content != null && content.size() > 0){
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("1");

            sheet.setColumnWidth(1, 8000);
            sheet.setColumnWidth(7, 8000);
            for(int i = 0; i < content.size(); i++){
                List<String> list = content.get(i);
                HSSFRow row = sheet.createRow(i);
                if(list != null && list.size() > 0){
                    for(int j = 0; j < list.size(); j++){
                        HSSFCell cell = row.createCell(j);
                        cell.setCellValue(list.get(j));
                    }
                }
            }
            FileOutputStream fos = new FileOutputStream(new File(name));
            workbook.write(fos);
            fos.close();
            return name;
        }else{
            throw new Exception("订单编号有误！");
        }
    }

}
