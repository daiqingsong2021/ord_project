package com.wisdom.javassist;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] params, MethodProxy proxy) throws Throwable {
        System.err.println("=======before======");
        Object res = proxy.invokeSuper(obj, params);
        System.err.println("=======and======");
        return res;
    }
}