package com.wisdom.acm.license;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RegisterBundle {
    /**
     * 注册Bundle
     *
     * @return
     */
    String value() default "";
}

