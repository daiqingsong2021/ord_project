package com.wisdom.base.common.vo.plan.define;

import lombok.Data;

/**
 * @Author: szc
 * @Date: 2019/4/27 11:48
 * @Version 1.0
 */
@Data
public class PlanDefineSearchVo {

    //计划定义id
    private Integer id;
    //计划定义类型
    private String planType;
    //计划定义名称
    private String planName;
}
