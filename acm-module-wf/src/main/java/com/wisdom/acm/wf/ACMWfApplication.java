package com.wisdom.acm.wf;

import com.wisdom.cache.EnableAcmCache;
import com.wisdom.auth.client.EnableAcmAuthClient;
import com.spring4all.swagger.EnableSwagger2Doc;
//import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;
//import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.util.Properties;

/**
 */
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
@MapperScan("com.wisdom.acm.wf.mapper")
@EnableSwagger2Doc
public class ACMWfApplication {
    @Autowired
    DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(ACMWfApplication.class, args);
    }
}
