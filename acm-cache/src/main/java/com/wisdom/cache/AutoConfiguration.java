package com.wisdom.cache;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 *
 *
 */
@ComponentScan({"com.wisdom.cache"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class AutoConfiguration {

}
