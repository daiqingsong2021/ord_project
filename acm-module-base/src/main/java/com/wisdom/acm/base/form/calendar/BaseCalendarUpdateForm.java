package com.wisdom.acm.base.form.calendar;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class BaseCalendarUpdateForm extends BaseForm {

    @NotNull(message = "日历主键不能为空")
    private Integer id;

    @NotNull(message = "日历名称不能为空")
    @LogParam(title = "日历名称")
    private String calName;
}