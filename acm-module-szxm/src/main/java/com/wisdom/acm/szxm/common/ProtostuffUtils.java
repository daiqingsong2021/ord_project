package com.wisdom.acm.szxm.common;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * protostuff 只能序列化普通bean，不支持bean的嵌套序列化
 */
public class ProtostuffUtils
{
    /**
     * 避免每次序列化都重新申请Buffer空间
     */
     private static final ThreadLocal<LinkedBuffer> linkedBufferThreadLocal = new ThreadLocal<LinkedBuffer>(){
        /**
         * ThreadLocal没有被当前线程赋值时或当前线程刚调用remove方法后调用get方法，返回此方法值
         */
        @Override
        protected LinkedBuffer initialValue()
        {
            LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
            return buffer;
        }
    };

    /**
     * 序列化方法，把指定对象序列化成字节数组
     *
     * @param obj
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj)
    {
        Class<T> clazz = (Class<T>) obj.getClass();
        Schema<T> schema = getSchema(clazz);
        byte[] data;
        try
        {
            data = ProtostuffIOUtil.toByteArray(obj, schema, linkedBufferThreadLocal.get());
        }
        finally
        {
            linkedBufferThreadLocal.get().clear();
        }

        return data;
    }

    /**
     * 反序列化方法，将字节数组反序列化成指定Class类型
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz)
    {
        Schema<T> schema = getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, obj, schema);
        return obj;
    }

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> clazz)
    {
        //RuntimeSchema.getSchema(),这个方法是线程安全的
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        return schema;
    }

}
