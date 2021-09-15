package com.wisdom.base.common.dc.vo.traffic;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 线路客运量vo
 */
@Data
public class TrafficLineDailyVo {
    private Integer id;
    private Integer sortNum;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date lastUpdTime;

    private Integer lastUpdUser;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creatTime;

    private BigDecimal creator;
    private String lastUpdIp;
    private BigDecimal wsdver;
    private BigDecimal projectId;
    private BigDecimal sectionId;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;

    private String line;

    //线路分期
    private String linePeriod;

    //进站量
    private BigDecimal arrivalVolume;

    //出站量
    private BigDecimal outboundVolume;

    //换入量
    private BigDecimal transferVolume;

    //进站量+换入量
    private BigDecimal trafficVolume;

    //本月累计客运量(万人次)
    private BigDecimal trafficVolumeMonth;

    //本月日均客运量（万人次）
    private BigDecimal trafficVolumeMonthAverage;

    //年累计客运量(万人次)
    private BigDecimal trafficVolumeYear;

    //年日均客运量(万人次)
    private BigDecimal trafficVolumeYearAverage;

    //开通至今累计客运量（万人次）
    private BigDecimal trafficVolumeOpen;

    //开通至今日均客运量(万人次)
    private BigDecimal trafficVolumeOpenAverage;
    //星期几
    private String week;

    //'线路'
    private GeneralVo lineVo=new GeneralVo();
}
