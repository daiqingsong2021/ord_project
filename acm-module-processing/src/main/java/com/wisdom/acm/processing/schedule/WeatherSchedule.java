package com.wisdom.acm.processing.schedule;

import com.wisdom.acm.processing.common.WeatherUtil;
import com.wisdom.acm.processing.service.weather.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
@Configuration
public class WeatherSchedule {

    private Logger log = LoggerFactory.getLogger(WeatherSchedule.class);

    @Autowired
    private WeatherService weatherService;

    /**
     * 每天中午12点获取天气
     */
    @Scheduled(cron = "0 01 12 * * ?")
    public void deal() {
        log.info("WeatherSchedule thread is start,currenttime is " + new Date());
        dealTask();
        log.info("WeatherSchedule thread is end,currenttime is " + new Date());
    }

    /**
     * 处理天气落库任务
     */
    public void dealTask(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);//时
        calendar.set(Calendar.MINUTE, 0);//分
        calendar.set(Calendar.SECOND, 0);//秒
        calendar.set(Calendar.MILLISECOND, 0);//毫秒
        Date currentTime = calendar.getTime();//当前时间

        String drtq="--";
        String weatherUrl = "https://free-api.heweather.net/s6/weather/?location=haerbin&key=3c3fa198cacc4152b94b20def11b2455";
        try {
            if(!ObjectUtils.isEmpty(weatherUrl))
            {
                Map<String, Object> info= WeatherUtil.getTodayWeather(weatherUrl);
                if(!ObjectUtils.isEmpty(info)){
                    drtq=info.get("condTxt").toString()+", 温度 "+info.get("tmpMin").toString()+"℃"+" - "+info.get("tmpMax").toString()+"℃";
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        log.info("weather is：" + drtq);
        weatherService.add(currentTime, drtq);
    }
}
