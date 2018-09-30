package com.common.controllers;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang.StringUtils;
import sun.swing.StringUIClientPropertyKey;
import utils.DictionaryConstants;
import utils.bean.JsonHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示数据字典
 */
public class DictionaryCtrl extends BaseCtrl {

    /**
     * 第一项为“全部”
     * 传输参数返回list
     * 参数是字典值
     */
    public void getDictIncludeAll() {
        String dict = getPara("dict");
        JsonHashMap jhm = new JsonHashMap();
        try {
            List<Record> list = DictionaryConstants.DICT_RECORD_LIST.get(dict);
            List<Record> reList=new ArrayList<>(list);
            Record all = new Record();
            all.set("value", null);
            all.set("name", "全部");
            reList.add(0, all);
            jhm.putCode(1).put("data", reList);
        } catch (Exception e) {
            e.printStackTrace();
            jhm.putCode(-1).putMessage(e.toString());
        }
        renderJson(jhm);
    }

    /**
     * 第一项为“请选择”
     */
    public void getDictIncludeChoose() {
        String dict = getPara("dict");
        JsonHashMap jhm = new JsonHashMap();
        try {
            List<Record> list = DictionaryConstants.DICT_RECORD_LIST.get(dict);
            List<Record> reList=new ArrayList<>(list);
            Record all = new Record();
            all.set("value", null);
            all.set("name", "请选择");
            reList.add(0, all);
            jhm.putCode(1).put("data", reList);
        } catch (Exception e) {
            e.printStackTrace();
            jhm.putCode(-1).putMessage(e.toString());
        }
        renderJson(jhm);
    }

    /**
     * 只返回数据库中的字典值
     */
    public void getDict() {
        String dict = getPara("dict");
        JsonHashMap jhm = new JsonHashMap();
        try {
            List<Record> list = DictionaryConstants.DICT_RECORD_LIST.get(dict);
            jhm.putCode(1).put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            jhm.putCode(-1).putMessage(e.toString());
        }
        renderJson(jhm);
    }

}
