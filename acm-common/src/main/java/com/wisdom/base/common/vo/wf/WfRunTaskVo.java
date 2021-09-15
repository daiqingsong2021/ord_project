package com.wisdom.base.common.vo.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "流程运行任务")
public class WfRunTaskVo extends WfTaskVo{

    @ApiModelProperty(value="是否开始活动")
    private boolean start;

    @ApiModelProperty(value="是否结束活动")
    private boolean end;

}
