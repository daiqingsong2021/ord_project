package com.wisdom.base.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.aop.framework.AopContext;

/**
 * spring aop的辅助工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AopUtil {
    /**
     * 为了解决循环aop调用,要使用这个方法。另：spring默认的代理必须为cglib，且exposeProxy=true <br>
     * 用法：
     *
     * <pre>
     * Aops.getSelf(this)
     * </pre>
     *
     * @param t t一般入参为this，而this只能是类对象，不可能是代理类，这一点要注意
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSelf(T t) {
        try {
            T curT = (T) AopContext.currentProxy();
            if (curT.getClass().getSuperclass().equals(t.getClass())) { // 有时出现currentProxy和t类型不一致，这里做一下判断
                return curT;
            }
        } catch (IllegalStateException e) {
            // 一般会报错：Cannot find current proxy: Set 'exposeProxy' property on
            // Advised to 'true' to make it available.
            // 此时表明这个类中没有aop方法，直接返回t即可
        }
        return t;
    }
}