package com.wisdom.base.common.vo.excel;

import lombok.Data;

@Data
public class ExcelColumnVo {

    private Integer id;
    // 字段名称
    private String name;
    // 标题
    private String title;
    // 字段类型
    private String type;

    private Integer check;
    // 是否必填
    private Integer required;

}
