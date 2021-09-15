package com.wisdom.acm.processing.service.weather;

import com.wisdom.acm.processing.po.weather.WeatherPo;
import com.wisdom.base.common.service.CommService;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface WeatherService  extends CommService<WeatherPo> {
    /**
     * 新增天气
     * @param crt_time
     * @param des
     */
    void add(Date crt_time, String des);
    /**
     * 根据时间查询天气
     * @param crt_time
     * @return
     */
    String query(Date crt_time);
}
