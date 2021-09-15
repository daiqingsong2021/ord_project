package com.wisdom.base.common.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务字典
 */
@ApiModel(value = "业务字典")
@Data
public class DictionaryVo {

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    public DictionaryVo(){

    }

    public DictionaryVo(String id, String name){
        this.id = id;
        this.name = name;
    }
}
