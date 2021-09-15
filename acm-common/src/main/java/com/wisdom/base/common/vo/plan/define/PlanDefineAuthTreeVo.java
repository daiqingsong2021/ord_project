package com.wisdom.base.common.vo.plan.define;


import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

@Data
public class PlanDefineAuthTreeVo extends TreeVo<PlanDefineAuthTreeVo> {
    //代码
    private String code;
    //名称
    private String name;
    // 行对象类型（eps,project,define）
    private String type;
    //计划定义类型（1前期计划，2专项计划，3实施计划）
    private String planType;
}
