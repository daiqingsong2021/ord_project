package com.wisdom.base.common.vo.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@Data
@ApiModel(value = "流程业务ID")
public class WfFormDataVo {

    @ApiModelProperty(value = "业务数据类型")
    private String bizType;

    @ApiModelProperty(value = "业务数据ID")
    private String bizId;

    @ApiModelProperty(value = "业务表单ID")
    private String formId;

    @ApiModelProperty(value = "通过")
    private String passed;

}
