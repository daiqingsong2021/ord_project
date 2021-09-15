package com.wisdom.base.common.vo.wf;

import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.sys.OrgVo;
import com.wisdom.base.common.vo.sys.UserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 我发起的
 */
@ApiModel(value = "我发起的流程")
@Data
public class WfMineTaskVo {

    @ApiModelProperty(value = "流程实例ID")
    private String id;

    @ApiModelProperty(value = "当前流程激活的任务名称")
    private String taskName;

    @ApiModelProperty(value = "流程实例ID")
    private String procInstId;

    @ApiModelProperty(value = "流程实例名称")
    private String procInstName;

    @ApiModelProperty(value = "流程发起人")
    private UserVo startUser;

    @ApiModelProperty(value = "流程发起人部门")
    private OrgVo startUserOrg;

    @ApiModelProperty(value = "开始时间")
    private Date createTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "流程状态")
    private String status;

    @ApiModelProperty(value = "业务类型")
    private GeneralVo bizType;

    @ApiModelProperty(value = "备注")
    private String remark;
}
