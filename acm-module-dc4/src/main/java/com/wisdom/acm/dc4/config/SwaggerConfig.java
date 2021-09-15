package com.wisdom.acm.dc4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	//是否开启swagger，正式环境一般是需要关闭的，可根据springboot的多环境配置进行设置
    boolean swaggerEnabled = true;
 
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                // 是否开启
                .enable(swaggerEnabled).select()
                // 扫描的路径包
                .apis(RequestHandlerSelectors.basePackage("com.wisdom.acm.szxm"))
                // 指定路径处理PathSelectors.any()代表所有的路径
                .paths(PathSelectors.any()).build().pathMapping("/");
    }
 
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //.title("wf api")
                //.description("My wf api")
                //.contact(new Contact("", "https://blog.lqdev.cn/", "499452441@qq.com"))
                //.version("1.0.0")
                //.termsOfServiceUrl("http://192.168.3.151:8080/super")
                .build();
    }
}
