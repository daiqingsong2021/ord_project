package com.wisdom.acm.sys.vo.excel;

import lombok.Data;

@Data
public class ExcelErrorListVo {

    private Integer row;

    private Integer sheetnum;

    private String error;

    public String getArr (){
        return "第"+(this.sheetnum+1)+"工作薄,第"+(row+1)+"行";
    }

}
