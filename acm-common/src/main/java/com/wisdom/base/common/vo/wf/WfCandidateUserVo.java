package com.wisdom.base.common.vo.wf;

import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.UserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "流程候选用户")
public class WfCandidateUserVo {

    @ApiModelProperty(value="ID")
    private String id;

    @ApiModelProperty(value="代码")
    private String code;

    @ApiModelProperty(value="名称")
    private String name;

    @ApiModelProperty(value="是否默认选中")
    private boolean defaultPart;

    @ApiModelProperty(value="委托人")
    private UserVo assignor;

    public WfCandidateUserVo(){

    }

    public WfCandidateUserVo(String id){
        this.id = id;
    }

    public WfCandidateUserVo(UserVo user){
        this.id = Tools.toString(user.getId());
        this.code = user.getCode();
        this.name = user.getName();
    }

    public WfCandidateUserVo(String id, String code, String name){
        this.id = id;
        this.code = code;
        this.name = name;
    }

}
