package com.wisdom.acm.base;


import com.wisdom.cache.EnableAcmCache;
import com.wisdom.auth.client.EnableAcmAuthClient;
import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;


@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableFeignClients({"com.wisdom.auth.client.feign","com.wisdom.base.common.feign"})
@EnableScheduling
@EnableAcmAuthClient
@EnableAcmCache
@EnableAsync
@EnableTransactionManagement
@MapperScan({"com.wisdom.acm.base.mapper", "com.wisdom.base.domain.mapper"})
@EnableSwagger2Doc
public class ACMBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(ACMBaseApplication.class, args);
    }
}
