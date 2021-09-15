package com.wisdom.base.common.vo.wf;

import com.wisdom.base.common.vo.sys.RoleVo;
import com.wisdom.base.common.vo.sys.UserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "流程监听任务")
public class WfTaskListenerVo {

	@ApiModelProperty(value = "ID")
	private String id;

	@ApiModelProperty(value = "流程定义ID")
	private String procDefId;

	@ApiModelProperty(value = "流程定义Key")
	private String procDefKey;

	@ApiModelProperty(value = "流程定义名称")
	private String procDefName;

	@ApiModelProperty(value = "流程实列ID")
	private String procInstId;

	@ApiModelProperty(value = "流程实列名称")
	private String procInstName;

	@ApiModelProperty(value = "流程活动ID")
	private String activityId;

	@ApiModelProperty(value = "任务名称")
	private String taskName;

	@ApiModelProperty(value = "发送人")
	private UserVo sender;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "完成时间")
	private Date endTime;

	@ApiModelProperty(value = "候选人")
	private List<WfCandidateUserVo> candidateUsers;

	@ApiModelProperty(value = "候选组")
	private List<WfCandidateGroupVo> candidateGroups;

	@ApiModelProperty(value="是否开始活动")
	private boolean start;

	@ApiModelProperty(value="是否结束活动")
	private boolean end;

	public WfTaskListenerVo() {

	}

}
