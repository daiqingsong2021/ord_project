package com.wisdom.acm.sys.task;

import com.wisdom.acm.sys.service.UUVService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Author：wqd
 * Date：2019-09-23 16:49
 * Description：<描述>
 */

/**
 * 定时同步uuv数据 组织、人员
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class UuvSyncTask implements SchedulingConfigurer {
    private static Logger logger = LoggerFactory.getLogger(UuvSyncTask.class);

    /**
     * 每天22点执行同步任务  cron表达式不支持年 仅支持6位
     */
    @Value("${uuv.task.corn}")
    private String cronExpression;

    @Autowired
    private UUVService uuvService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    uuvService.tbUuvTask();
                } catch (Exception e) {
                    logger.error("定时同步uuv数据异常信息：{}", e);
                }
            }
        };
        Trigger trigger = new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                //任务触发，获取下一次的执行时间
                CronTrigger trigger = new CronTrigger(cronExpression);
                Date nextExec = trigger.nextExecutionTime(triggerContext);
                logger.info("定时同步uuv数据，下次执行时间：{}", nextExec);
                return nextExec;
            }
        };
        taskRegistrar.addTriggerTask(task, trigger);
    }
}
