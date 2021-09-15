package com.wisdom.base.common.vo;

import lombok.Data;

/**
 * 状态
 */
@Data
public class StatusVo {

    private String id;

    private String name;

    public StatusVo(){

    }

    public StatusVo(String id,String name){
        this.id = id;
        this.name = name;
    }
}
