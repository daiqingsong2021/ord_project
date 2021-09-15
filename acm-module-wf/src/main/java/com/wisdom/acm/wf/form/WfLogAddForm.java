package com.wisdom.acm.wf.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Api(tags = "流程日志增加表单")
@Data
public class WfLogAddForm {

    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "操作类型")
    private String optType;

    @ApiModelProperty(value = "待处理人")
    private String handleUser;

    @ApiModelProperty(value = "处理节点名称")
    private String activity;

    @ApiModelProperty(value = "流程实例ID")
    private String procInstId;

    @ApiModelProperty(value = "节点创建时间")
    private Date startTime;

    @ApiModelProperty(value = "处理人名称")
    private String user;

    @ApiModelProperty(value = "完成时间")
    private Date stayTime;

    @ApiModelProperty(value = "节点ID")
    private String activityId;
}
