package com.wisdom.acm.dc1;

import com.wisdom.auth.client.EnableAcmAuthClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

@EnableAcmAuthClient
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients({"com.wisdom.auth.client.feign","com.wisdom.base.common.feign"})
@MapperScan({"com.wisdom.acm.dc1.mapper"})
public class AcmModuleDc1Application {

	public static void main(String[] args) {
		SpringApplication.run(AcmModuleDc1Application.class, args);
	}

}
