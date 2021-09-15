package com.wisdom.acm.dc5.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FaultDailyProblemVo {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 主键ID
     */
    private Integer faultId;

    /**
     * 报告日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordDay;
    /**
     *线路
     */
    private Integer line;

    /**
     *线路
     */
    private String lineName;

    /**
     *记录人
     */
    private Integer recorder;

    /**
     *记录人
     */
    private String recorderName;
    private String problemDesc;
    /**
     * 记录日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

    /**
     *发布状态
     */
    private String problemStatus;
    /**
     *发布状态
     */
    private String problemStatusDesc;

    private String problemReason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 处理详情
     */
    private String dealDetail;

}
