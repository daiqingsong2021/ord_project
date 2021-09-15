package com.wisdom.base.common.aspect;

import com.wisdom.base.common.enums.ParamEnum;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(value= RetentionPolicy.RUNTIME)
@Documented
public @interface LogParam {
    boolean log() default true;
    String title() default "";
    ParamEnum type() default ParamEnum.NONE;
    String dictType() default "";
}

