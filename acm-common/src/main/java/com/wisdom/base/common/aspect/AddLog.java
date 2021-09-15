package com.wisdom.base.common.aspect;

import com.wisdom.base.common.enums.LoggerModuleEnum;

import java.lang.annotation.*;

@Target(value={ElementType.PARAMETER, ElementType.METHOD,ElementType.LOCAL_VARIABLE})
@Retention(value= RetentionPolicy.RUNTIME)
@Documented
public @interface AddLog {
    LoggerModuleEnum module();
    String title() default "";
    String content() default "";
    boolean initContent() default false;
}

