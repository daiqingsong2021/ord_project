package com.wisdom.acm.sys.vo;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sys.menu.list")
@Data
public class SysMenuLevelVo {

    //菜单权限（1：开启，0：关闭）
    private Integer enable;
}
