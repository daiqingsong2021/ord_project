package com.wisdom.acm.dc5.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FaultDailyProblemDealVo {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 问题ID odr_fault_daily_problem表的主键
     */
    private Integer problemId;

    /**
     *记录人
     */
    private Integer recorder;

    /**
     *记录人
     */
    private String recorderName;

    private String dealDetail;
    /**
     * 记录日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

}
