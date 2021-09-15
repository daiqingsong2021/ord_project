package com.wisdom.base.common.form.plan.task;


import lombok.Data;

@Data
public class MyWarningSearchForm {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 计划开始时间
     */
    private String planStartTime;

    /**
     * 计划完成时间
     */
    private String planEndTime;

    /**
     * 预警时间
     */
    private Integer warningDays;
}
