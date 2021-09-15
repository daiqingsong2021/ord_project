package com.wisdom.base.common.vo.wf;

import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.base.DictionaryVo;
import com.wisdom.base.common.vo.sys.OrgVo;
import com.wisdom.base.common.vo.sys.UserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel(value = "我的流程待办")
@Data
public class MyUnFinishTaskVo {

    @ApiModelProperty(value = "任务ID")
    private String id;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "流程实例ID")
    private String procInstId;

    @ApiModelProperty(value = "流程实例名称")
    private String procInstName;

    @ApiModelProperty(value = "流程发起人")
    private UserVo startUser;

    @ApiModelProperty(value = "流程发起人部门")
    private OrgVo startUserOrg;

    @ApiModelProperty(value = "送审人")
    private UserVo sender;

    @ApiModelProperty(value = "开始时间")
    private Date createTime;

    @ApiModelProperty(value = "业务类型")
    private GeneralVo bizType;

    @ApiModelProperty(value = "停留时间")
    private String stayTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
