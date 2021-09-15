package com.wisdom.gateway;

import com.wisdom.auth.client.EnableAcmAuthClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 网关
 location /jwt/ {
 # 转发请求到后端服务网关
 proxy_pass http://192.168.3.145:8765/jwt/;
 }
 location /api/ {
 proxy_pass http://192.168.3.145:8765/api/;
 }
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAcmAuthClient
@EnableFeignClients({"com.wisdom.auth.client.feign","com.wisdom.gateway.feign"})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
