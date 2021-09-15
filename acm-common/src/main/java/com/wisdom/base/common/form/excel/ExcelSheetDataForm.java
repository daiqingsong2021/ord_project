package com.wisdom.base.common.form.excel;

import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.util.ListUtil;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Data
public class ExcelSheetDataForm {
    // 坐标
    private Integer index;
    // 数据集合
    private List<Map<String,Object>> datas;
    // 列集合
    private List<Map<String,Object>> columns;

    public Map<String,String> findFieldKeyMap () {
        Map<String,String> ret = new HashMap<>();
        if(!ObjectUtils.isEmpty(this.columns)){
            for(Map<String,Object> map: this.columns){
                String field = FormatUtil.toString(map.get("field"));
                String col = FormatUtil.toString(map.get("col"));
                ret.put(field,col);
            }
        }
        return ret;
    }


    public Map<String,String> findColKeyMap () {

        Map<String,String> ret = new HashMap<>();
        if(!ObjectUtils.isEmpty(this.columns)){
            for(Map<String,Object> map: this.columns){
                String field = FormatUtil.toString(map.get("field"));
                String col = FormatUtil.toString(map.get("col"));
                ret.put(col,field);
            }
        }
        return ret;
    }

    public List<Map<String,Object>> findDatasByFieldKey(){
        List<Map<String,Object>> retlist = new ArrayList<>();
        if(!ObjectUtils.isEmpty(this.datas)){
            Map<String,String> colKeyMap = this.findColKeyMap();
            for(Map<String,Object>  map : this.datas){
                Map<String,Object> retmap = new HashMap<>();
                Iterator<Map.Entry<String,Object>> iterator =  map.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry<String,Object> entry = iterator.next();
                    if("rowIndex".equals(entry.getKey())){
                        retmap.put(entry.getKey(),entry.getValue());
                    }else{
                        String field = colKeyMap.get(entry.getKey()).replace("*","").trim();
                        retmap.put(field,entry.getValue());
                    }
                }
                retlist.add(retmap);
            }
        }
        return retlist;
    }

    public List<String> getFieldKeys() {
        return ListUtil.toValueListByListMap(this.columns,"field",String.class);
    }
}
