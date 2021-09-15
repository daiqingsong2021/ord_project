package com.wisdom.base.common.vo.plan.task;

import com.wisdom.base.common.vo.DictionaryVo;
import lombok.Data;

import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/9/18 10:05
 * @Version 1.0
 */
@Data
public class PlanTaskStepVo {

    //id
    private Integer id;

    //名称
    private String name;

    //代码
    private String code;

    //设计总量
    private String totalDesign;

    //计量单位
    private DictionaryVo unit;

    //权重
    private double estwt;

    //计划完成量
    private Integer planComplete;

    //实际完成量
    private Integer actComplete;

    //实际开始时间
    private Date actStartTime;

    //实际完成时间
    private Date actEndTime;

    //完成比例
    private String completePct;

    //创建者id
    private Integer creator;
}
