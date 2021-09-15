package com.wisdom.base.common.vo.wf;

import com.wisdom.base.common.vo.UserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "流程日志")
@Data
public class WfLogDetailVo {

    @ApiModelProperty(value = "流程FormID")
    private Integer id;

    @ApiModelProperty(value = "流程名称")
    private String processInstName;

    @ApiModelProperty(value = "发起人")
    private UserVo creator;

    @ApiModelProperty(value = "流程日志详情")
    private List<WfProcLogDetailVo> wfLog;

}



