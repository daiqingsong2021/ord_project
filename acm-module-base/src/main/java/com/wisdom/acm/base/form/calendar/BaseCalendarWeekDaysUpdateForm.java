package com.wisdom.acm.base.form.calendar;

import com.wisdom.acm.base.vo.calendar.BaseWeekDayVo;
import com.wisdom.base.common.aspect.LogParam;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BaseCalendarWeekDaysUpdateForm {

    @NotNull(message = "日历主键不能为空")
    private Integer id;

    @NotNull(message = "日历名称不能为空")
    @LogParam(title = "日历名称")
    private String calName;

    private List<BaseWeekDayVo> weekDays;

    private Double dayHrCnt;

    private Double weekHrCnt;

    private Double monthHrCnt;

    private Double yearHrCnt;

}