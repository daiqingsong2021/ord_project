package com.wisdom.base.common.vo.wf;

import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.sys.OrgVo;
import com.wisdom.base.common.vo.sys.UserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "流程任务详情")
public class WfTaskDetailVo extends WfTaskVo{

	@ApiModelProperty(value="流程业务类型")
	private GeneralVo bizType;

	@ApiModelProperty(value = "流程实例名称")
	private String procInstName;

	@ApiModelProperty(value = "流程状态")
	private String status;

	@ApiModelProperty(value = "流程发起人")
	private UserVo startUser;

	@ApiModelProperty(value = "流程发起人部门")
	private OrgVo startUserOrg;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value="是否能同意审批")
	private boolean agree;

	@ApiModelProperty(value="是否能撤销审批")
	private boolean cancel;

	@ApiModelProperty(value="是否能驳回审批")
	private boolean reject;

	@ApiModelProperty(value="是否能终止审批")
	private boolean terminate;

	@ApiModelProperty(value = "业务数据")
	private List<WfFormDataVo> datas;

	@ApiModelProperty(value = "流程日志详情")
	private List<WfProcLogDetailVo> wfLog;
}
