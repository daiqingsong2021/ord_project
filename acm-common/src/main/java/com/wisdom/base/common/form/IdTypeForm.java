package com.wisdom.base.common.form;

import lombok.Data;

@Data
public class IdTypeForm {

    // id
    private Integer id;

    // 类型
    private String type;

    public IdTypeForm(){

    }

    public IdTypeForm(Integer id, String type){
        this.id = id;
        this.type = type;
    }
}
