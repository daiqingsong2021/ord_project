package com.wisdom.base.common.vo;

import lombok.Data;

/**
 * 类型
 */
@Data
public class TypeVo {

    private String id;

    private String name;

    public TypeVo(){

    }

    public TypeVo(String id, String name){
        this.id = id;
        this.name = name;
    }
}
