package com.wisdom.base.common.form.plan.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TimeDrtnCalcForm {

    //日历id
    private Integer calendarId;

    //开始日期
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    //完成日期
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    //计划工期
    private Double drtn;

    //操作类型
    private String opeType;
}


