package com.wisdom.base.common.vo.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "活动实例")
public class WfActivityInstanceVo {

	@ApiModelProperty(value = "主键")
	private String Id;

	@ApiModelProperty(value = "活动定义ID")
	private String activityId;

	@ApiModelProperty(value = "活动定义名称")
	private String activityName;

	@ApiModelProperty(value = "活动类型")
	private String activityType;

	@ApiModelProperty(value = "流程定义ID")
	private String processDefinitionId;

	@ApiModelProperty(value = "流程实列ID")
	private String processInstanceId;

	@ApiModelProperty(value = "执行人")
	private String assignee;

	@ApiModelProperty(value = "开始时间")
	private Date startTime;

	@ApiModelProperty(value = "完成时间")
	private Date endTime;

	@ApiModelProperty(value = "获取持续时间")
	private Long getDurationInMillis;

	public WfActivityInstanceVo() {

	}

}
