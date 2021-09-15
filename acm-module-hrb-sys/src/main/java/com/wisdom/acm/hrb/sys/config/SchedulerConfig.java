package com.wisdom.acm.hrb.sys.config;

import com.wisdom.base.common.util.ResourceUtil;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.ObjectUtils;

import java.io.InputStream;
import java.util.Map;

@Configuration
public class SchedulerConfig implements SchedulerFactoryBeanCustomizer
{
    @Override public void customize(SchedulerFactoryBean schedulerFactoryBean)
    {
        InputStream in = SchedulerConfig.class.getClassLoader().getResourceAsStream("application.yml");//文件路径是相对类目录(src/main/java)的相对路径
        Map<String, Object> yml = ResourceUtil.getYmlPropertie(in);
        Boolean autoStartup = Boolean.valueOf(String.valueOf(yml.get("spring.quartz.properties.org.quartz.scheduler.autoStartup")));
        if(ObjectUtils.isEmpty(autoStartup))
        {
            schedulerFactoryBean.setAutoStartup(true);
        }
        else
          schedulerFactoryBean.setAutoStartup(autoStartup);//是否自动运行scheduler
    }
}
