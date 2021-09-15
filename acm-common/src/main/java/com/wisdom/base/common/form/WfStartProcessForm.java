package com.wisdom.base.common.form;

import com.wisdom.base.common.vo.wf.WfActivityVo;
import com.wisdom.base.common.vo.wf.WfFormDataVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "发起流程表单")
public class WfStartProcessForm {

	@ApiModelProperty(value = "流程定义名称", required = true)
	private String procDefKey;

	@ApiModelProperty(value = "业务流程代码", required = true)
	private String bizTypeCode;

	@ApiModelProperty(value = "流程名称", required = true)
	private String title;

	@ApiModelProperty(value = "发起者")
	private String userId;

	@ApiModelProperty(value = "意见")
	private String comment;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "业务数据", required = true)
	private List<WfFormDataVo> formData;

	@ApiModelProperty(value = "后续参与者")
	private List<WfActivityVo> nextActPart;

	@ApiModelProperty(value = "流程变量")
	private Map<String, Object> vars;
}
