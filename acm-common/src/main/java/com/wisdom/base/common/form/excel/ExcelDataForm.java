package com.wisdom.base.common.form.excel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ExcelDataForm {

    // 参数
    private Map<String,Object> params;
    // 类型
    private Integer errorType;
    // 列集合
    private List<ExcelSheetDataForm> sheets = new ArrayList<>();


    public ExcelSheetDataForm getSheetDataByIndex(Integer index){
        if(sheets != null){
            for(ExcelSheetDataForm sheetDataForm : sheets){
                if(sheetDataForm.getIndex().equals(index)){
                    return sheetDataForm;
                }
            }
        }
        return null;
    }

}
