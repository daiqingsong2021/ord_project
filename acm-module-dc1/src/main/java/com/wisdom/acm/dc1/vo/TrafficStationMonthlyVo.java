package com.wisdom.acm.dc1.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zll
 * 2020/7/29/029
 * Description:<>
 */
@Data
public class TrafficStationMonthlyVo {
    private Integer id;
    private Integer sortNum;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creatTime;
    private Integer creator;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;
    //'几号线'
    private String line;
    //'几期'
    private String linePeriod;
    //'站点'
    private String station;

    //'月累计进站量（万人次）'
    private BigDecimal arrivalVolume;

    //'月累计日均进站客流（万人次）'
    private BigDecimal arrivalVolumeAverage;

    //'月累计出站量（万人次）'
    private BigDecimal outboundVolume;

    //'月累计日均出站客流（万人次）'
    private BigDecimal outboundVolumeAverage;

    //'月累计总进出站量（万人次）'
    private BigDecimal totalVolume;
    //站点编号
    private String stationNum;
}
