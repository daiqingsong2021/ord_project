package com.wisdom.acm.dc1.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zll
 * @date 2020/7/20/020 14:06
 * Description：<月累计线路客运量，按日汇总>
 */
@Data
public class TrafficLineMonthlyVo {
    private Integer id;
    private Integer sortNum;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creatTime;

    private Integer creator;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;

    private String line;

    //线路分期
    private String linePeriod;

    //日均客运量
    private BigDecimal transferVolume;

    //月统计客运量
    private BigDecimal trafficVolume;

    //本月单日最大客流量
    private BigDecimal maxTrafficVolume;

    //本月单日最大客流量发生日期
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date maxTrafficDate;
}
