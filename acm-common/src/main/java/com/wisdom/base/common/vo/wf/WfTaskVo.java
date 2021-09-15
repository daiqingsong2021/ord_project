package com.wisdom.base.common.vo.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "流程任务")
public class WfTaskVo {

	@ApiModelProperty(value = "ID")
	private String id;

	@ApiModelProperty(value = "流程定义ID")
	private String procDefId;

	@ApiModelProperty(value = "流程实列ID")
	private String procInstId;

	@ApiModelProperty(value = "流程活动ID")
	private String activityId;

	@ApiModelProperty(value = "名称")
	private String name;

	@ApiModelProperty(value = "拥有者（一般情况下为空，只有在委托时才有值）")
	private String owner;

	@ApiModelProperty(value = "签收人或委托人")
	private String assignee;

	@ApiModelProperty(value = "委托类型，DelegationState分为两种：PENDING，RESOLVED。如无委托则为空v")
	private String delegation;

	@ApiModelProperty(value = "创建时间v")
	private Date createTime;

	@ApiModelProperty(value = "完成时间")
	private Date endTime;

	public WfTaskVo() {

	}

	public WfTaskVo(String id, String name) {
		this.id = id;
		this.name = name;
	}

}
