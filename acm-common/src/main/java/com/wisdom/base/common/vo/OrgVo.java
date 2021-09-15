package com.wisdom.base.common.vo;

import lombok.Data;

/**
 * 组织
 */
@Data
public class OrgVo {

    private Integer id;

    private String name;

    public OrgVo(){

    }

    public OrgVo(Integer id,String name){
        this.id = id;
        this.name = name;
    }

}
