package com.wisdom.base.common.vo.wf;

import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.vo.TreeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import sun.reflect.generics.tree.Tree;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "流程活动候选人树形列表")
public class WfCandidateTreeVo {

    @ApiModelProperty(value="UUID")
    private String uuid = FormatUtil.UUID();

    @ApiModelProperty(value="ID")
    private String id;

    @ApiModelProperty(value="代码")
    private String code;

    @ApiModelProperty(value="名称")
    private String name;

    @ApiModelProperty(value="类型(user=用户，group=用户组(角色), activity=活动)")
    private String type;

    @ApiModelProperty(value="是否默认选中")
    private boolean defaultPart;

    @ApiModelProperty(value="是否只能指定一个后续活动下的参与者（单一分支）")
    private boolean activityOnly;

    @ApiModelProperty(value="子节点集合")
    protected List<WfCandidateTreeVo> children;

    public WfCandidateTreeVo(){

    }

    public WfCandidateTreeVo(WfActivityVo activity){
        this.id = activity.getId();
        this.name = activity.getName();
        this.type = "activity";
    }

    public WfCandidateTreeVo(WfCandidateGroupVo group){
        this.id = group.getId();
        this.code = group.getCode();
        this.name = group.getName();
        this.defaultPart = group.isDefaultPart();
        this.type = "group";
    }

    public WfCandidateTreeVo(WfCandidateUserVo user){
        this.id = user.getId();
        this.code = user.getCode();
        this.name = user.getName();
        this.defaultPart = user.isDefaultPart();
        this.type = "user";
    }
}
