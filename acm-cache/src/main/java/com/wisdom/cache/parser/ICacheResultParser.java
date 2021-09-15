package com.wisdom.cache.parser;

import java.lang.reflect.Type;

/**
 * cache结果解析
 *
 * 解决问题：
 *
 *
 * @version 1.0
 * @since 1.7
 */
public interface ICacheResultParser {
    /**
     * 解析结果
     *
     * @param value
     * @param returnType
     * @param origins
     * @return
     */
    public Object parse(String value, Type returnType, Class<?>... origins);
}
