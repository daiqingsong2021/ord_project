package com.wisdom.fescar.annotation;



import com.wisdom.fescar.config.CustomMethodIntercepter;
import com.wisdom.fescar.config.CustomMybatisConfig;
import com.wisdom.fescar.config.FeignConfig;
import com.wisdom.fescar.config.MvcIntercepterConfig;
import com.wisdom.fescar.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import({FeignConfig.class, MvcIntercepterConfig.class,
        CustomMethodIntercepter.class, GlobalExceptionHandler.class
        , CustomMybatisConfig.class})
public @interface EnableTxManager {
}
