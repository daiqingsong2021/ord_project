package com.wisdom.base.common.vo;

import lombok.Data;

import javax.persistence.Column;

@Data
public class SelectVo extends TreeVo<SelectVo> {
    // 值
    private String value;
    // 显示内容
    private String title;
    // 类型
    private String type;

    private Integer orgId;

    /**
     * 扩展字段1
     */
    @Column(name="extended_column_1")
    private String extendedColumn1;


    public void setValue(Integer value){
        if(value != null){
            this.setValue(value+"");
        }else{
            this.setValue(value);
        }

    }

    public void setValue(String value){
        this.value = value;
    }

}
