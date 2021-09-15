package com.wisdom.base.common.vo;

import lombok.Data;

/**
 * 业务字典
 */
@Data
public class DictionaryVo {

    private String id;

    private String name;

    public DictionaryVo(){

    }

    public DictionaryVo(String id,String name){
        this.id = id;
        this.name = name;
    }
}
