package com.wisdom.acm.base.config;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 拦截器
 *
 * @author Administrator
 */
@Aspect
@Component
public class LogAspect extends com.wisdom.base.common.aspect.LogAspect {
    /**
     * 日志勿删 ！！！！
     */
}
