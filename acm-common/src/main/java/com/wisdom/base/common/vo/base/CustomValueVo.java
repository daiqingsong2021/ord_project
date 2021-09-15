package com.wisdom.base.common.vo.base;


import lombok.Data;


@Data
/**
 * 自定义VO
 */
public class CustomValueVo {

    private String value;

    private String displayName;

    public CustomValueVo(){

    }

    public CustomValueVo(String value, String displayName){
        this.value = value;
        this.displayName = displayName;
    }

}
