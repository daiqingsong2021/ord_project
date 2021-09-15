package com.wisdom.acm.dc1.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zll
 * @date 2020/7/21/021 18:38
 * Description：<线网数据>
 */
@Data
public class TrafficMainVo {
    private Integer id;
    private Integer sortNum;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creatTime;
    private Integer creator;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;

    //'本日客运量（人次）'
    private BigDecimal trafficVolumeToday;


    //'月累计客运量（万人次）'
    private BigDecimal trafficVolumeMonth;

    //'本月日均客运量（万人次）'
    private BigDecimal trafficVolumeMonthAverage;

    //'年累计客运量（万人次）'
    private BigDecimal trafficVolumeYear;

    //'年累计日均客运量（万人次）'
    private BigDecimal trafficVolumeYearAverage;

    //'开通累计客运量（万人次）'
    private BigDecimal trafficVolumeOpen;

    //'开通累计日均客运量（万人次）'
    private BigDecimal trafficVolumeOpenAverage;

    //'审批状态'
    private GeneralVo statusVo=new GeneralVo();

    //星期几
    private String week;

    //上报人
    private String reportMan;
}
