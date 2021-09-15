package com.wisdom.acm.szxm.form.quartz;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 */
@Data
public class TimeTaskAddForm
{

    @NotBlank(message = "任务名称不能为空")
    private String jobName;

    @NotBlank(message = "任务分组不能为空")
    private String jobGroup;

    private String description;

    private String arguments;

    @NotBlank(message = "cron表达式不能为空")
    private String cronExpression;

    @NotBlank(message = "执行类不能为空")
    private String beanClass;

}
