package com.wisdom.base.common.vo.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "认领VO")
public class WfClaimVo {

    @ApiModelProperty(value="流程实列ID")
    private String procInstId;

    @ApiModelProperty(value="流程活动ID")
    private String activityId;

    @ApiModelProperty(value="活动名称")
    private String activityName;

    @ApiModelProperty(value="工作项ID")
    private String taskId;

    @ApiModelProperty(value="处理人")
    private String userId;

    @ApiModelProperty(value="表单ID")
    private Integer formId;

    @ApiModelProperty(value="流程业务类型代码")
    private String bizTypeCode;

    @ApiModelProperty(value="业务表单")
    private String url;

    @ApiModelProperty(value="是否能同意审批")
    private boolean agree;

    @ApiModelProperty(value="是否能撤销审批")
    private boolean cancel;

    @ApiModelProperty(value="是否能驳回审批")
    private boolean reject;

    @ApiModelProperty(value="是否能终止审批")
    private boolean terminate;

    @ApiModelProperty(value="是否开始活动")
    private boolean start;

    @ApiModelProperty(value="是否结束活动")
    private boolean end;

}
