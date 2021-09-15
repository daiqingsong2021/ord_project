package com.wisdom.acm.processing.vo.weather;

import lombok.Data;

import java.util.Date;

@Data
public class WeatherVo {

    //主键
    private Integer id;
    //日期
    private Date crt_time;
    //天气描述
    private String weatherdes;
}
