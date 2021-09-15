package com.wisdom.acm.activiti;

import com.spring4all.swagger.EnableSwagger2Doc;
import com.wisdom.auth.client.EnableAcmAuthClient;
import com.wisdom.cache.EnableAcmCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableFeignClients({"com.wisdom.auth.client.feign","com.wisdom.base.common.feign","com.wisdom.acm.activiti.feign"})
@EnableScheduling
@EnableAcmAuthClient
@EnableAcmCache
@EnableTransactionManagement
@MapperScan("com.wisdom.acm.activiti.mapper")
@EnableSwagger2Doc
@Configuration
public class ACMActivitiApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ACMActivitiApplication.class, args);
    }
}
