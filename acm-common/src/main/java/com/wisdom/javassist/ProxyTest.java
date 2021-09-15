package com.wisdom.javassist;

import org.springframework.cglib.proxy.Enhancer;

public class ProxyTest {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MyTarget.class);
        enhancer.setCallback(new MyInterceptor());
        MyTarget target = (MyTarget) enhancer.create();
        target.printName();
        System.out.println("proxy class name:" + target.getClass().getName());

    }
}
