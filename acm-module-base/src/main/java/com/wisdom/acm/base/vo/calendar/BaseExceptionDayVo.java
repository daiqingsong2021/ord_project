package com.wisdom.acm.base.vo.calendar;

import lombok.Data;

import java.util.List;

@Data
public class BaseExceptionDayVo {

    // 例外日期的名称
    private String name;

    // 是否工作日0/1
    private Integer dayWorking;

    // 周几，1周日/2周一/3周二/4周三/5周四/6周五/7周六
    private Integer dayType;

    // 例外开始时间
    private String fromDate;

    // 例外完成时间
    private String toDate;

    // 工作时间
    private List<BaseWorkingTimeVo> workingTimes;
}
