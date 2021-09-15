package com.wisdom.acm.base.vo.calendar;

import com.alibaba.druid.support.json.JSONUtils;
import com.wisdom.base.common.vo.UserVo;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Data
public class BaseCalendarInfoVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 日历名称
     */
    private String calName;

    /**
     * 日历类型
     */
    private String calType;

    /**
     * 全局默认
     */
    private Integer isDefault;

    /**
     * 每天工时
     */
    private Double dayHrCnt;

    /**
     * 每周工时
     */
    private Double weekHrCnt;

    /**
     * 每月工时
     */
    private Double monthHrCnt;

    /**
     * 每年工时
     */
    private Double yearHrCnt;

    /**
     * 创建人
     */
    private UserVo creator;

    /**
     * 创建时间
     */
    private Date creatTime;

    // 标准周期（一周每天的工作时间）
    private List<BaseWeekDayVo> weekDays;

    // 例外时间
    private List<BaseExceptionDayVo> exceptions;

    //
    public void setWeekDaysString(String weekDaysString){

        List<BaseWeekDayVo> list = null;
        if(!ObjectUtils.isEmpty(weekDaysString)){
            list = (List<BaseWeekDayVo>) JSONUtils.parse(weekDaysString);
        }
        this.setWeekDays(list);
    }

    public void setExceptionsString(String weekDaysString){

        List<BaseExceptionDayVo> list = null;
        if(!ObjectUtils.isEmpty(weekDaysString)){
            list = (List<BaseExceptionDayVo>) JSONUtils.parse(weekDaysString);
        }
        this.setExceptions(list);
    }

}