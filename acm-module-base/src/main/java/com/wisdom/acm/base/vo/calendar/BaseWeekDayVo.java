package com.wisdom.acm.base.vo.calendar;

import lombok.Data;

import java.util.List;

@Data
public class BaseWeekDayVo {

    // 是否工作日0/1
    private Integer dayWorking;

    // 周几，1周日/2周一/3周二/4周三/5周四/6周五/7周六
    private Integer dayType;

    // 工作时间
    private List<BaseWorkingTimeVo> workingTimes;
}
