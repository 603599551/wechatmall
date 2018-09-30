package com.common.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import utils.DictionaryConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictionaryService {

    public static void loadDictionary(){
		List<Record> dictList = Db.find("select * from w_dictionary order by sort");
		Map<String, List<Record>> dictMap = new HashMap<>();
		Map<String, String> dictIdValueMap = new HashMap<>();
		if(dictList != null && dictList.size() > 0){
			for(Record r : dictList){
				List<Record> list = dictMap.computeIfAbsent(r.getStr("parent_id"), k -> new ArrayList<>());
				list.add(r);
				dictIdValueMap.put(r.getStr("id"), r.getStr("value"));
			}
		}
		if(dictMap != null && dictMap.size() > 0){
		    for(String key : dictMap.keySet()){
		        if("0".equals(key)){
		            continue;
                }
                //根据key（parent_id），找到对应的list
		        List<Record> list = dictMap.get(key);
		        //根据parent_id找到一级的value
		        String dict_key = dictIdValueMap.get(key);
		        //一个key对应一个map，一个map可以存多个键值对
		        Map<String, String> stringMap = DictionaryConstants.DICT_STRING_MAP.computeIfAbsent(dict_key, k -> new HashMap<>());
		        Map<String, Record> recordMap = DictionaryConstants.DICT_RECORD_MAP.computeIfAbsent(dict_key, k -> new HashMap<>());
		        if(list != null && list.size() > 0){
		            for(Record r : list){
		                stringMap.put(r.getStr("value"), r.getStr("name"));
                        recordMap.put(r.getStr("value"), r);
                    }
                }
            }
        }
        //把record_Map中的所有value（record），放到recordList中。
        if(DictionaryConstants.DICT_RECORD_MAP != null && DictionaryConstants.DICT_RECORD_MAP.size() > 0){
			for(String key : DictionaryConstants.DICT_RECORD_MAP.keySet()){
				Map<String, Record> recordMap = DictionaryConstants.DICT_RECORD_MAP.get(key);
				List<Record> recordList = new ArrayList<>();
				for(String rKey : recordMap.keySet()){
					recordList.add(recordMap.get(rKey));
				}
				DictionaryConstants.DICT_RECORD_LIST.put(key, recordList);
			}
		}

	}
}
