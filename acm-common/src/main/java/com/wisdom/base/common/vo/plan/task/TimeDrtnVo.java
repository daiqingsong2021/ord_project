package com.wisdom.base.common.vo.plan.task;

import com.wisdom.base.common.util.calc.calendar.PmCalendar;
import lombok.Data;

import java.util.Date;

@Data
public class TimeDrtnVo {

    /**
     * 日历
     */
    private PmCalendar calendar;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 完成时间
     */
    private Date endTime;

    /**
     * 计划工期
     */
    private Double drtn;

}
