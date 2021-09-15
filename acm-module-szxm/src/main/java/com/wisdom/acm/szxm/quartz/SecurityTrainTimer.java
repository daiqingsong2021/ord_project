package com.wisdom.acm.szxm.quartz;

import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.base.common.dc.feign.DcBaseService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import java.util.Date;
import java.util.Map;

/**
 * @author zll
 * 2020/8/17/017 14:22
 * Description:<计算安全运营天数，从开通那天算起至今天，定时设置每天晚上凌晨1点开始加1天>
 */
public class SecurityTrainTimer extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(SecurityTrainTimer.class);
    @Autowired
    private DcBaseService dcBaseService;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("运营天数开始执行，开始时间：{}", DateUtil.getDateTimeFormat(new Date()));
        Map<String,String>map= Maps.newHashMap();
        dcBaseService.updateTrainTime(map);
    }
}
