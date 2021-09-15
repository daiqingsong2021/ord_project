package com.wisdom.base.common.vo.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "流程活动候选人")
public class WfCandidateVo{

    @ApiModelProperty(value="是否只能选择一个后续活动（单一分支）")
    private boolean activityOnly;

    @ApiModelProperty(value="后续活动")
    private List<WfActivityVo> activities;

}
