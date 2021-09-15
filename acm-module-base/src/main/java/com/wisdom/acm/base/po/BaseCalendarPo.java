package com.wisdom.acm.base.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "wsd_base_calendar")
@Data
public class BaseCalendarPo extends BasePo {

    @Column(name = "PROJECT_ID")
    private Integer projectId;

    @Column(name = "CAL_NAME")
    private String calName;

    @Column(name = "CAL_TYPE")
    private String calType;

    @Column(name = "is_default")
    private Integer isDefault;

    // 日期数据
    @Column(name = "week_days")
    private String weekDays;

    // 例外日期数据
    @Column(name = "exceptions")
    private String exceptions;

    @Column(name = "DAY_HR_CNT")
    private Double dayHrCnt;

    @Column(name = "WEEK_HR_CNT")
    private Double weekHrCnt;

    @Column(name = "MONTH_HR_CNT")
    private Double monthHrCnt;

    @Column(name = "YEAR_HR_CNT")
    private Double yearHrCnt;

    @Column(name = "BUILT_IN")
    private Integer builtIn;

}