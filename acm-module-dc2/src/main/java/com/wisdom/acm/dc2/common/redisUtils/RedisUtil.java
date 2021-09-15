package com.wisdom.acm.dc2.common.redisUtils;

import com.wisdom.acm.dc2.common.ProtostuffUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RedisUtil
{
    public static final String getRedisCountScript =
            "local times = redis.call('incr',KEYS[1]);\nif times == 1 then\n   redis.call('expire',KEYS[1], tonumber(ARGV[1]));\nend;\nif times > tonumber(ARGV[2]) then\n    return tostring(1)..'@'..tostring(times);\nend;\nreturn tostring(0)..'@'..tostring(times);\n";

    private JedisPool jedisPool;

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private String port;

    @Value("${redis.timeout}")
    private String timeout;

    @Value("${redis.password}")
    private String requirepass;

    @Value("${redis.database}")
    private String database;

    @Value("${redis.pool.maxActive}")
    private String maxTotal;

    @Value("${redis.pool.maxIdle}")
    private String maxIdle;

    @Value("${redis.pool.maxWait}")
    private String maxWaitMillis;

    @PostConstruct
    public void  init()
    {
        String address=host+":"+port;

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        if (!isBlank(maxTotal))
        {
            poolConfig.setMaxTotal(Integer.parseInt(maxTotal));
        }

        if (!isBlank(maxIdle))
        {
            poolConfig.setMaxIdle(Integer.parseInt(maxIdle));
        }

        if (!isBlank(maxWaitMillis))
        {
            poolConfig.setMaxWaitMillis(Long.parseLong(maxWaitMillis));
        }
        String uri = "";
        if (isBlank(requirepass))
        {
            uri = "redis://"+address+ "/" + database;
        }
        else
            uri = "redis://user:" + requirepass + "@" + address + "/" + database;

        try
        {
            jedisPool = new JedisPool(poolConfig, new URI(uri), Integer.parseInt(timeout));
        }
        catch (URISyntaxException e)
        {
            throw new IllegalArgumentException("没有可用的连接，请确认缓存地址是否配置正确、缓存是否开启！");
        }
    }

    public static boolean isBlank(String str)
    {
        if (null != str && !"".equals(str) && !"null".equals(str))
        {
            return str.trim().length() == 0;
        }
        else
        {
            return true;
        }
    }

    /**
     * 删除键
     * @param key
     * @return
     */
    public boolean del(String key)
    {
        return RedisAPI.del(this.jedisPool, key).longValue() == 0L ? true : false;
    }

    /**
     * key值是否还存在着
     * @param key
     * @return
     */
    public boolean isCacheExists(String key)
    {
        return RedisAPI.exists(this.jedisPool, key).booleanValue();
    }

    /**
     * key值是否超时
     * @param key
     * @return
     */
    public boolean isCacheTimeOut(String key)
    {
        Long a = RedisAPI.ttl(this.jedisPool, key);

        if (a.longValue() == -2L)
        {
            return true;
        }
        return false;
    }

    /**
     * 获取缓存key的值
     * @param key
     * @return
     */
    public String getStringValue(String key)
    {
        String value = RedisAPI.get(this.jedisPool, key);
        return value;
    }

    /**
     * 根据key获取对象
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getObjectValue(String key,Class<T> clazz)
    {
        Jedis jedis = null;
        T val = null;
        try
        {
            jedis = this.jedisPool.getResource();
            byte [] clasVal=jedis.get(key.getBytes());
            val= ProtostuffUtils.deserialize(clasVal, clazz);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jedis.close();
        }
        return val;
    }

    /**
     * key值自增
     * @param key
     * @return 自增后的值
     */
    public Long incr(String key)
    {
        Long val=0L;
        Jedis jedis = null;
        try
        {
            jedis = this.jedisPool.getResource();
            val=jedis.incr(key);
            return val;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jedis.close();
        }
        return val;
    }

    /**
     * key值自减
     * @param key
     * @return 自减后的值
     */
    public Long decr(String key)
    {
        Long val=0L;
        Jedis jedis = null;
        try
        {
            jedis = this.jedisPool.getResource();
            val=jedis.decr(key);
            return val;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jedis.close();
        }
        return val;
    }

    /**
     * 根据key存value
     * @param key
     * @param value
     * @return
     */
    public boolean setStringValue(String key, String value)
    {
        String v = RedisAPI.set(this.jedisPool, key, value);
        if("OK".equals(v))
            return true;
        return false;
    }

    /**
     * 存Object
     * @param key
     * @param value
     * @return
     */
    public boolean setObjectValue(String key, Object value)
    {
        Jedis jedis = null;
        String val = null;
        try
        {
            jedis = this.jedisPool.getResource();
            byte[] data = ProtostuffUtils.serialize(value);
            val=jedis.set(key.getBytes(), data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jedis.close();
        }
        if("OK".equals(val))
            return true;
        return false;
    }

    /**
     * 存Object 设定存亡时间，单位秒
     * @param key
     * @param value
     * @param second
     * @return
     */
    public boolean setxObjectValue(String key, Object value,int second)
    {
        byte[] data = ProtostuffUtils.serialize(value);
        String val=RedisAPI.setex(this.jedisPool,key.getBytes(),second,data);
        if("OK".equals(val))
            return true;
        return false;
    }
    /**
     * //将值value关联到key，并将key的生存时间设为seconds(秒)。 
     * setStringValue:(这里用一句话描述这个方法的作用). <br/>
     * TODO(这里描述这个方法适用条件 - 可选).<br/>
     * TODO(这里描述这个方法的执行流程 - 可选).<br/>
     * TODO(这里描述这个方法的使用方法 - 可选).<br/>
     * TODO(这里描述这个方法的注意事项 - 可选).<br/>
     *
     * @author wyf
     * @param key
     * @param value
     * @param seconds
     * @return
     * @since JDK 1.6
     */
    public boolean setStringValue(String key, String value,int seconds)
    {
        String v = RedisAPI.setex(this.jedisPool, key, seconds, value);
        if("OK".equals(v))
            return true;
        return false;
    }

    /**
     * 获取map值
     * @param key map变量名称
     * @param field field 为map的键
     * @return
     */
    public String getMapValue(String key, String field)
    {
        String value = RedisAPI.hget(this.jedisPool, key, field);
        return value;
    }

    /**
     * 设置Map的值
     * @param key map变量名称
     * @param field field 为map的键
     * @param value 值
     * @return
     */
    public boolean setMapValue(String key, String field, String value)
    {
        long v = RedisAPI.hset(this.jedisPool, key, field, value).longValue();
        if(v==1L || v==0L)
            return true;
        else return false;
    }


    public String[] isLimitMsgCounts(String key, String seconds, String limitNums)
    {
        List keys = new ArrayList();
        keys.add(key);

        List params = new ArrayList();
        params.add(seconds);
        params.add(limitNums);
        Object v = eval(getRedisCountScript, keys, params);
        String[] value = v.toString().split("@");
        return value;
    }

    private Object eval(String script, List<String> keys, List<String> params)
    {
        String scriptSha = RedisAPI.scriptLoad(this.jedisPool, script);
        return RedisAPI.evalsha(this.jedisPool, scriptSha, keys, params);
    }
}
