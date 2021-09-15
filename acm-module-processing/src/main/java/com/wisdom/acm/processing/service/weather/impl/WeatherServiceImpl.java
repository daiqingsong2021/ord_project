package com.wisdom.acm.processing.service.weather.impl;

import com.wisdom.acm.processing.common.DateUtil;
import com.wisdom.acm.processing.mapper.weather.WeatherMapper;
import com.wisdom.acm.processing.po.weather.WeatherPo;
import com.wisdom.acm.processing.service.weather.WeatherService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import java.util.Date;

@Service
@Slf4j
public class WeatherServiceImpl  extends BaseService<WeatherMapper, WeatherPo> implements WeatherService {
    @Override
    public void add(Date crt_time, String des) {
        WeatherPo po=new WeatherPo();
        po.setCrt_time(crt_time);
        po.setWeatherdes(des);
        this.insert(po);
    }
    @Override
    public String query(Date crt_time) {
        Example example=new Example(WeatherPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("crt_time", DateUtil.getDateFormat(crt_time));
        if(CollectionUtils.isEmpty(selectByExample(example)))
            return "--";
        else
            return this.selectByExample(example).get(0).getWeatherdes();
    }
}
