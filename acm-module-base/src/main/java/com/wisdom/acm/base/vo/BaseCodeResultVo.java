package com.wisdom.acm.base.vo;

import lombok.Data;

@Data
public class BaseCodeResultVo {

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

    public BaseCodeResultVo(){}

    public BaseCodeResultVo(String code){
        this.code = code;
    }
}
