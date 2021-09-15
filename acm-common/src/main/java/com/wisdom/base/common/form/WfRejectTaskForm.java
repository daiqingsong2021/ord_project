package com.wisdom.base.common.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 驳回流程表单
 */
@Data
@ApiModel(value = "驳回流程表单")
public class WfRejectTaskForm {

    @ApiModelProperty(value = "流程实例id")
    private String procInstId;

    @ApiModelProperty(value = "工作项id")
    private String taskId;

    @ApiModelProperty(value = "返回节点名称")
    private String activityId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "意见")
    private String comment;

}
