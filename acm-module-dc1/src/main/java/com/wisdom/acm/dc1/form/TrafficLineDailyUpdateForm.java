package com.wisdom.acm.dc1.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zll
 * 2020/8/4/004 8:40
 * Description:<线网客运日表>
 */
@Data
public class TrafficLineDailyUpdateForm {
    @NotNull(message = "主键ID不能为空")
    private Integer id;
    @NotBlank(message = "线路不能为空")
    private String line;

    @NotNull(message = "交易日期不能为空")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;
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


    //月累计客运量（万人次）
    private BigDecimal trafficVolumeMonth;

    //本月日均客运量（万人次）
    private BigDecimal trafficVolumeMonthAverage;

    //年累计客运量（万人次）
    private BigDecimal trafficVolumeYear;

    //年累计日均客运量（万人次）
    private BigDecimal trafficVolumeYearAverage;
}
