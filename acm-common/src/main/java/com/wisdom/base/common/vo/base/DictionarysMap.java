package com.wisdom.base.common.vo.base;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务字典Map
 */
@Data
public class DictionarysMap {

    private Map<String,Map<String, DictionaryVo>> retmap = new HashMap<>();

    public DictionaryVo getDictionaryVo(String dictType, String dictCode){

        Map<String, DictionaryVo> dictionaryVoMap = retmap.get(dictType);
        if(dictionaryVoMap != null){
            return dictionaryVoMap.get(dictCode);
        }
        return  null;
    }

    public String getDictionaryName(String dictType,String dictCode){

        DictionaryVo dict = this.getDictionaryVo(dictType,dictCode);

        return  dict != null  ? dict.getName() : null;
    }

    public void putDictionaryVo(String dictType,String dictCode,String dictName){
        Map<String, DictionaryVo> dictionaryVoMap = retmap.get(dictType);
        if(dictionaryVoMap == null){
            dictionaryVoMap = new HashMap<>();
            retmap.put(dictType,dictionaryVoMap);
        }
        DictionaryVo dict = new DictionaryVo();
        dict.setName(dictName);
        dict.setId(dictCode);
        dictionaryVoMap.put(dictCode,dict);
    }

}
