package com.wisdom.acm.dc5.config;


import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author: ZGC
 * @date Created in 2018/8/31 下午 7:39
 */
@Configuration
public class DozerBeanMapperConfigure {
    @Bean
    public DozerBeanMapper mapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
       // mapper.setMappingFiles(ListUtil.toArrayList("dozer/StationDozerMapping.xml","dozer/PlanDefineMapping.xml","dozer/PlanTaskStepMapping.xml"));
        return mapper;
    }
}