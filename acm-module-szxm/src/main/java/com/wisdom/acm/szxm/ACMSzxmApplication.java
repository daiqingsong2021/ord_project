package com.wisdom.acm.szxm;

import com.spring4all.swagger.EnableSwagger2Doc;
import com.wisdom.auth.client.EnableAcmAuthClient;
import com.wisdom.cache.EnableAcmCache;
import com.wisdom.encrypt.springboot.annotation.EnableEncrypt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

//import org.mybatis.spring.annotation.MapperScan;

/**
 */
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableFeignClients({"com.wisdom.auth.client.feign","com.wisdom.base.common.feign","com.wisdom.base.common.dc.feign"})
@EnableScheduling
@EnableAcmAuthClient
@EnableAcmCache
@EnableTransactionManagement
@MapperScan({"com.wisdom.acm.szxm.mapper"})
@EnableSwagger2Doc
@EnableEncrypt
public class ACMSzxmApplication {
    public static void main(String[] args) {
        SpringApplication.run(ACMSzxmApplication.class, args);
    }
}
