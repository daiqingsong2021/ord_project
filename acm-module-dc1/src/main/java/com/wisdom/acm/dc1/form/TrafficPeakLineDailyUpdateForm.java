package com.wisdom.acm.dc1.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zll
 * 2020/8/4/004 9:22
 * Description:<描述>
 */
@Data
public class TrafficPeakLineDailyUpdateForm {
    @NotNull(message = "主键ID不能为空")
    private Integer id;
    @NotBlank(message = "线路不能为空")
    private String line;
    @NotNull(message = "交易日期不能为空")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;
    //线路分期
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
}
