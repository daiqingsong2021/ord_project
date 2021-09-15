package com.wisdom.cache;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class DefaultView extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将访问/static/** 的路由映射到classpath:/static/ 目录下
        registry.addResourceHandler("/static/cache/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/static/cache/css/**").addResourceLocations("classpath:/static/css/");

    }
}