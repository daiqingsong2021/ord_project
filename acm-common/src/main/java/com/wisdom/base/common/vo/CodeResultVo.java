package com.wisdom.base.common.vo;

import lombok.Data;

@Data
public class CodeResultVo {

    private String code;

    private boolean over = true;

    /**
     * 剩余几位
     */
    private int oddLength;

    /**
     * 是否可以被修改
     */
    private boolean modified;

    /**
     * 没有设定规则
     */
    private boolean noneRule;

    public CodeResultVo(){}

    public CodeResultVo(String code){
        this.code = code;
    }
}
