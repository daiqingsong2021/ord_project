package com.wisdom.base.common.form;

import com.wisdom.base.common.vo.wf.WfCandidateVo;
import com.wisdom.base.common.vo.wf.WfFormDataVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@ApiModel(value = "流程运行表单")
public class WfRuningProcessForm {

    @ApiModelProperty(value="流程Key")
    private String procDefKey;

    @ApiModelProperty(value="流程定义ID")
    private String procDefId;

    @ApiModelProperty(value="流程实列ID")
    private String procInstId;

    @ApiModelProperty(value="流程活动ID")
    private String activityId;

    @ApiModelProperty(value="流程活动名称")
    private String activityName;

    @ApiModelProperty(value="流程任务id")
    private String taskId;

    @ApiModelProperty(value="流程任务处理人")
    private String userId;

    @ApiModelProperty(value="流程业务类型代码")
    private String bizTypeCode;

    @ApiModelProperty(value="流程定义参与者")
    private WfCandidateVo candidate;

    @ApiModelProperty(value="业务数据")
    private List<WfFormDataVo> formData;

    @ApiModelProperty(value = "流程变量")
    private Map<String, Object> vars;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "意见")
    private String comment;


}
