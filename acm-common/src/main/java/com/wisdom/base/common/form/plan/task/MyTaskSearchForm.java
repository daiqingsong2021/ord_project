package com.wisdom.base.common.form.plan.task;


import lombok.Data;

@Data
public class MyTaskSearchForm {

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

}
