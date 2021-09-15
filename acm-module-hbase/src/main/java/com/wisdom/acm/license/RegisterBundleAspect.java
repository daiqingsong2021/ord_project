package com.wisdom.acm.license;

import com.wisdom.base.common.constant.CommonConstants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 注册Bundle
 * 若许可包含注册Bundle，正常执行方法或者类
 * 若不包含注册Bundle，方法体不执行，返回值空
 *
 * @author
 * @since 1.7
 */
@Aspect
@Component
public class RegisterBundleAspect {

    private static Logger logger = LoggerFactory.getLogger(RegisterBundleAspect.class);

    @Around(value = "@annotation(registerBundle)")
    public Object doVerifyBundle(ProceedingJoinPoint joinPoint, RegisterBundle registerBundle) throws Throwable {
        String tragetClassName = joinPoint.getSignature().getDeclaringTypeName();
        String MethodName = joinPoint.getSignature().getName();
        String bundle = registerBundle.value();
        logger.debug(tragetClassName + "." + MethodName + " doVerifyBundle: " + bundle);
        if (CommonConstants.BUNDLESPERMISSION.contains(bundle)) {
            return joinPoint.proceed();
        }
        return null;
    }

}

