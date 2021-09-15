package com.wisdom.base.common.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "模型")
@Data
public class ActModelAddForm {

    @ApiModelProperty(value = "标题")
    private String modelTitle;

    @ApiModelProperty(value = "描述")
    private String modelDesc;

    @ApiModelProperty(value = "分类Id")
    private Integer parentId;

    @ApiModelProperty(value = "分类")
    private String category;
}
