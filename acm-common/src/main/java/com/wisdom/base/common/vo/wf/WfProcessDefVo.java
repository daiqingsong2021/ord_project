package com.wisdom.base.common.vo.wf;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel(value = "流程定义")
public class WfProcessDefVo {

    @ApiModelProperty(value="流程定义ID")
    private String id;

    @ApiModelProperty(value="流程定义KEY，不同版本的流程，KEY相同，ID不同, 根据key发起流程会自动发起最高版本的流程定义！")
    private String key;

    @ApiModelProperty(value="流程定义名称")
    private String name;

    @ApiModelProperty(value="流程版本")
    private String version;

}
