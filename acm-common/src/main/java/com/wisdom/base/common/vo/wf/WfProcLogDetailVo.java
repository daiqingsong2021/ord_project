package com.wisdom.base.common.vo.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "流程日志详情")
@Data
public class WfProcLogDetailVo {

    @ApiModelProperty(value = "意见")
    private String opinion;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "完成时间")
    private Date endTime;

    @ApiModelProperty(value = "停留时间")
    private String stayTime;

    @ApiModelProperty(value = "节点名称")
    private String workItemName;

    @ApiModelProperty(value = "操作人")
    private String operateUser;

    @ApiModelProperty(value = "操作类型")
    private String operateType;

    @ApiModelProperty(value = "送审至")
    private String nextUserName;
}
