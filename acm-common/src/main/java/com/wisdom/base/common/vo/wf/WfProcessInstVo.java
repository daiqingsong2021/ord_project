package com.wisdom.base.common.vo.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@ApiModel(value = "流程实例")
public class WfProcessInstVo {

    @ApiModelProperty(value="流程定义名称")
    private String procDefKey;

    @ApiModelProperty(value="流程定义ID")
    private String procDefId;

    @ApiModelProperty(value="流程定义名称")
    private String procDefName;

    @ApiModelProperty(value="实例id")
    private String procInstId;

    @ApiModelProperty(value="流程实例名称")
    private String procInstName;

    @ApiModelProperty(value="发起用户ID")
    private String startUserId;

    @ApiModelProperty(value="开始时间")
    private Date startTime;

    @ApiModelProperty(value="结束时间")
    private Date endTime;

}
