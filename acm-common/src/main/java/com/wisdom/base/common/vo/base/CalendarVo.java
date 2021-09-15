package com.wisdom.base.common.vo.base;

import lombok.Data;

@Data
public class CalendarVo {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 日历名称
     */
    private String calName;

    /**
     * 日历类型
     */
    private String calType;

    /**
     * 默认日历
     */
    private Integer isDefault;

    /**
     * 标准周工作时间
     */
    private String weekDays;

    /**
     * 例外工作时间
     */
    private String exceptions;

    /**
     * 天转小时
     */
    private Double dayHrCnt;

    /**
     * 周转小时
     */
    private Double weekHrCnt;

    /**
     * 月转小时
     */
    private Double monthHrCnt;

    /**
     * 年转小时
     */
    private Double yearHrCnt;

}