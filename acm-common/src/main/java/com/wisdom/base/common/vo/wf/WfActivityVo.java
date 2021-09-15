package com.wisdom.base.common.vo.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "流程活动定义")
public class WfActivityVo {

	@ApiModelProperty(value = "ID")
	private String id;

	@ApiModelProperty(value = "名称")
	private String name;

	@ApiModelProperty(value = "候选组(角色)")
	private List<WfCandidateGroupVo> candidateGroups;

	@ApiModelProperty(value = "候选人")
	private List<WfCandidateUserVo> candidateUsers;

	public WfActivityVo() {

	}

	public WfActivityVo(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
