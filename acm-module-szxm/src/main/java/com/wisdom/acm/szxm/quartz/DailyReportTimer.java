package com.wisdom.acm.szxm.quartz;

import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.base.common.dc.feign.DcProcessingService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zll
 * 2020/8/19/019 14:23
 * Description:<日报管理>
 */
public class DailyReportTimer extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(SecurityTrainTimer.class);
    @Autowired
    private DcProcessingService dcProcessingService;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("日报开始执行，开始时间：{}", DateUtil.getDateTimeFormat(new Date()));
        Map<String,Object> maps= Maps.newHashMap();
        List<String> keyAll= Arrays.asList("1","2","3","4","5");
        Calendar cal  =   Calendar.getInstance();
        cal.add(Calendar.DATE,  -1);
        String yesterday =new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
        String line1="1";
        String line2="2";
        String line3="3";
        maps.put("keys",keyAll);
        maps.put("date",yesterday);
        maps.put("line1",line1);
        maps.put("line2",line2);
        maps.put("line3",line3);
        maps.put("description","定时器生成");
        try{
            dcProcessingService.generateCCDailyWorkReportFile(maps);
        }finally {
            dcProcessingService.generateDailyWorkReportFile(maps);
        }
    }
}
