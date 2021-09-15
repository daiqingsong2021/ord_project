package com.wisdom.acm.base.form.calendar;

import com.wisdom.acm.base.vo.calendar.BaseExceptionDayVo;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BaseCalendarExceptionsUpdateForm extends BaseForm {

    @NotNull(message = "日历主键不能为空")
    private Integer id;
    // 例外
    private List<BaseExceptionDayVo> exceptions;
}