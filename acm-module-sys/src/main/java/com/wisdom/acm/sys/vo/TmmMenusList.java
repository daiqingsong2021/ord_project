package com.wisdom.acm.sys.vo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 读取三员权限配置类
 */
@Component
@ConfigurationProperties(prefix = "menus")
@Data
public class TmmMenusList {

    private List<String> system;

    private List<String> built_in;

}
