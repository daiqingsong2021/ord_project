package com.wisdom.base.common.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 认领流程表单
 */
@Data
@ApiModel(value = "认领流程表单")
public class WfClaimTaskForm {

    @ApiModelProperty(value="流程实例id")
    private String procInstId;

    @ApiModelProperty(value="工作项id")
    private String taskId;

    @ApiModelProperty(value="用户id")
    private String userId;

}
