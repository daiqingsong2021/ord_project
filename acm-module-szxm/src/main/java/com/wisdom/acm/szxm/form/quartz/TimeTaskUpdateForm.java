package com.wisdom.acm.szxm.form.quartz;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 */
@Data
public class TimeTaskUpdateForm
{
    @NotNull(message = "ID不能为空")
    private Integer id;

    private String description;

    @NotBlank(message = "cron表达式不能为空")
    private String cronExpression;

}
