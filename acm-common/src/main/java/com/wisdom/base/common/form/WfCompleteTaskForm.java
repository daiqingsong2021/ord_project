package com.wisdom.base.common.form;

import com.wisdom.base.common.vo.wf.WfActivityVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 流程统一提交表单
 */
@Data
@ApiModel(value = "完成任务表单")
public class WfCompleteTaskForm {

    @ApiModelProperty(value = "流程实例id")
    private String procInstId;

    @ApiModelProperty(value = "工作项id")
    private String taskId;

    @ApiModelProperty(value = "工作项id")
    private String userId;

    @ApiModelProperty(value = "意见")
    private String comment;

    @ApiModelProperty(value = "后续参与者")
    private List<WfActivityVo> nextActPart;

    @ApiModelProperty(value = "流程变量")
    private Map<String, Object> vars;
}
