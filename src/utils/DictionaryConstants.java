package utils;

import com.jfinal.plugin.activerecord.Record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王泽
 * @date 2018-09-21
 * 字典值缓存常量
 * 字典值tomcat启动时加载，如果直接修改数据库中字典值数据要重启tomcat才能生效
 */
public class DictionaryConstants {

    //客户类型
    public static final String CUSTOMERType = "customer_type";
    //收货地址状态
    public static final String ADDRESSSTATUS = "address_status";
    //在职状态
    public static final String WORKSTATUS = "work_status";
    //订单状态
    public static final String ORDERSTATUS = "order_status";
    //商品状态
    public static final String PRODUCTSTATUS = "product_status";
    //自提点状态
    public static final String STORESTATUS = "store_status";
    //通知类型
    public static final String NOTICETYPE = "notice_type";
    //物流类型
    public static final String TRANSPORTTYPE = "transport_type";
    //支付类型
    public static final String PAYTYPE = "pay_type";
    //客户性别
    public static final String GENDER = "gender";



    /**
     * dictionary表中的常量，tomcat启动时将字典值读取到内存中
     * 格式：
     *      大分类dictionary表的value--》设为a
     *      具体字典值dictionary表的value--》设为b
     *      具体字典值dictionary表的name--》设为c
     *      Map<a, Map<b, c>>
     * 例如：
     *      性别
     *      id   parent_id  name   value     sort
     *      1	    0	    性别	   gender	 100
     *      2	    1	    男	     1	     120
     *      3	    1	    女	     0	     130
     *      Map<gender, Map<1, 男>>
     *      Map<gender, Map<0, 女>>
     *
     */
    public static final Map<String, Map<String, String>> DICT_STRING_MAP = new HashMap<>();

    /**
     * dictionary表中的常量，tomcat启动时将字典值读取到内存中
     * 格式：
     *      大分类dictionary表的value--》设为a
     *      具体字典值dictionary表的value--》设为b
     *      具体字典值dictionary表的Record--》设为c
     *      Map<a, Map<b, c>>
     * 例如：
     *      性别
     *      id   parent_id  name   value     sort
     *      1	    0	    性别	   gender	 100
     *      2	    1	    男	     1	     120
     *      3	    1	    女	     0	     130
     *      Map<gender, Map<1, Record>>
     *      Map<gender, Map<0, Record>>
     *
     */
    public static final Map<String, Map<String, Record>> DICT_RECORD_MAP = new HashMap<>();

    /**
     * dictionary表中的常量，tomcat启动时将字典值读取到内存中
     * 格式：
     *      大分类dictionary表的value--》设为a
     *      具体字典值dictionary表的Record--》设为b
     *      Map<a, List<b>>
     * 例如：
     *      性别
     *      id   parent_id  name   value     sort
     *      1	    0	    性别	   gender	 100
     *      2	    1	    男	     1	     120
     *      3	    1	    女	     0	     130
     *      Map<gender, List<Record>>
     *      List<Record> ：Record（男字典值的）、Record（女字典值的）
     */
    public static final Map<String, List<Record>> DICT_RECORD_LIST = new HashMap<>();

}
