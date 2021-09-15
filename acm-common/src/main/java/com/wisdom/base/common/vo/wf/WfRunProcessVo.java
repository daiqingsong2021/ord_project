package com.wisdom.base.common.vo.wf;

import com.wisdom.base.common.vo.wf.WfCandidateVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@ApiModel(value = "流程运行对象")
public class WfRunProcessVo implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="流程定义id")
    private String procDefId;

    @ApiModelProperty(value="流程实例id")
    private String procInstId;

    @ApiModelProperty(value="流程活动ID")
    private String activityId;

    @ApiModelProperty(value="当前流程活动名称")
    private String activityName;

    @ApiModelProperty(value="流程任务ID")
    private String taskId;

    @ApiModelProperty(value="处理用户")
    private String userId;

    @ApiModelProperty(value="流程是否完成")
    private boolean procComplete;

    @ApiModelProperty(value="流程变量")
    private Map<String, Object> vars;

    @ApiModelProperty(value="候选者")
    private WfCandidateVo candidate;

}
