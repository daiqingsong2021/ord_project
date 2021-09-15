package com.wisdom.acm.sys.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取系统配置类
 */
@Component
@ConfigurationProperties(prefix = "sys.tmm")
@Data
public class SystemVo {

    //三员管理状态（1：开启，0：关闭）
    private Integer enable;
}

