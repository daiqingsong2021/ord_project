package com.wisdom.base.common.vo;

import lombok.Data;

/**
 * 用户
 */
@Data
public class RoleVo {

    private Integer id;

    private String code;

    private String name;

    public RoleVo(){

    }

    public RoleVo(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public RoleVo(Integer id, String code, String name){
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
