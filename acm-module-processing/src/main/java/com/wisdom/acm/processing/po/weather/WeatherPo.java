package com.wisdom.acm.processing.po.weather;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "weather_history")
@Data
public class WeatherPo extends BasePo {
    //日期
    @Column(name = "crt_time")
    private Date crt_time;
    //天气描述
    @Column(name = "weatherdes")
    private String weatherdes;
}
