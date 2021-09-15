package com.wisdom.acm.processing.vo.traffic;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zll
 * 2020/7/29/029 23:22
 * Description:<>
 */
@Data
public class TrafficPeakLineDailyVo {
    private Integer id;
    //日期
    private Date recordTime;
    private String line;
    //线路分期  逗号分隔：一期,二期,三期
    private String linePeriod;
    //早高峰进站量
    private BigDecimal morningPeakArrivalVolume;

    //早高峰出站量
    private BigDecimal morningPeakOutboundVolume;

    //早高峰客运量
    private BigDecimal morningPeakTrafficVolume;

    //晚高峰进站量
    private BigDecimal eveningPeakArrivalVolume;

    //晚高峰出站量
    private BigDecimal eveningPeakOutboundVolume;

    //晚高峰客运量
    private BigDecimal eveningPeakTrafficVolume;

    //高峰小时断面客流（上行）
    private BigDecimal transectUpVolume;

    //高峰小时断面客流（下行）
    private BigDecimal transectDownVolume;
    //'线路'
    private GeneralVo lineVo=new GeneralVo();
}
