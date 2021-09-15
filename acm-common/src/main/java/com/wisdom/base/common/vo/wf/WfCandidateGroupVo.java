package com.wisdom.base.common.vo.wf;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "流程活动候选组")
public class WfCandidateGroupVo {

    @ApiModelProperty(value="ID")
    private String id;

    @ApiModelProperty(value="代码")
    private String code;

    @ApiModelProperty(value="名称")
    private String name;

    @ApiModelProperty(value="是否默认选中")
    private boolean defaultPart;

    @ApiModelProperty(value="候选人")
    private List<WfCandidateUserVo> candidateUsers;

    public WfCandidateGroupVo(){

    }

    public WfCandidateGroupVo(String id){
        this.id = id;
    }

    public WfCandidateGroupVo(String id, String code, String name){
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
