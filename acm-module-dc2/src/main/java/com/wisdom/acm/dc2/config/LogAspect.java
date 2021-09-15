package com.wisdom.acm.dc2.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * 日志拦截器
 */
@Aspect
@Component
public class LogAspect extends com.wisdom.base.common.aspect.LogAspect
{

}
