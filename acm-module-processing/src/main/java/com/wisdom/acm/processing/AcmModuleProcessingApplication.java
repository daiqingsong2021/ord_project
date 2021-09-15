package com.wisdom.acm.processing;

import com.wisdom.auth.client.EnableAcmAuthClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableAcmAuthClient
@EnableEurekaClient
@EnableFeignClients({"com.wisdom.auth.client.feign","com.wisdom.base.common.feign","com.wisdom.base.common.dc.feign"})
@MapperScan({"com.wisdom.acm.processing.mapper"})
@EnableAsync
@EnableScheduling
public class AcmModuleProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcmModuleProcessingApplication.class, args);
	}

}
