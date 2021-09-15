package com.wisdom.acm.dc5.common.redisUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class RedisAPI
{
    public RedisAPI()
    {
    }

    public static String get(JedisPool jedisPool, String key)
    {
        String val = null;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.get(key);
            }
            catch (Exception var8)
            {
                var8.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static byte[] get(JedisPool jedisPool, byte[] key)
    {
        byte[] val = null;
        Jedis jedis = null;

        try
        {
            jedis = jedisPool.getResource();
            val = jedis.get(key);
        }
        catch (Exception var8)
        {
            var8.printStackTrace();
        }
        finally
        {
            jedis.close();
        }

        return val;
    }

    public static String set(JedisPool jedisPool, String key, String value)
    {
        String val = null;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.set(key, value);
            }
            catch (Exception var9)
            {
                var9.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static String setex(JedisPool jedisPool, String key, int seconds, String value)
    {
        String val = null;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.setex(key, seconds, value);
            }
            catch (Exception var10)
            {
                var10.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static String setex(JedisPool jedisPool, byte[] key, int seconds, byte[] value)
    {
        String val = null;
        Jedis jedis = null;

        try
        {
            jedis = jedisPool.getResource();
            val = jedis.setex(key, seconds, value);
        }
        catch (Exception var10)
        {
            var10.printStackTrace();
        }
        finally
        {
            jedis.close();
        }

        return val;
    }

    public static Long del(JedisPool jedisPool, String key)
    {
        long val = 0L;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.del(key);
            }
            catch (Exception var9)
            {
                var9.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static Long del(JedisPool jedisPool, byte[] key)
    {
        long val = 0L;
        Jedis jedis = null;

        try
        {
            jedis = jedisPool.getResource();
            val = jedis.del(key);
        }
        catch (Exception var9)
        {
            var9.printStackTrace();
        }
        finally
        {
            jedis.close();
        }

        return val;
    }

    public static Long del(JedisPool jedisPool, String[] keys)
    {
        long val = 0L;
        if (isBlank(keys))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.del(keys);
            }
            catch (Exception var9)
            {
                var9.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static Boolean exists(JedisPool jedisPool, String key)
    {
        Boolean val = false;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.exists(key);
            }
            catch (Exception var8)
            {
                var8.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static Boolean exists(JedisPool jedisPool, byte[] key)
    {
        Boolean val = false;
        Jedis jedis = null;

        try
        {
            jedis = jedisPool.getResource();
            val = jedis.exists(key);
        }
        catch (Exception var8)
        {
            var8.printStackTrace();
        }
        finally
        {
            jedis.close();
        }

        return val;
    }

    public static Long exists(JedisPool jedisPool, String[] keys)
    {
        long val = 0L;
        if (isBlank(keys))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.exists(keys);
            }
            catch (Exception var9)
            {
                var9.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static Long expire(JedisPool jedisPool, String key, int seconds)
    {
        long val = 0L;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.expire(key, seconds);
            }
            catch (Exception var10)
            {
                var10.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static Long expire(JedisPool jedisPool, byte[] key, int seconds)
    {
        long val = 0L;
        Jedis jedis = null;

        try
        {
            jedis = jedisPool.getResource();
            val = jedis.expire(key, seconds);
        }
        catch (Exception var10)
        {
            var10.printStackTrace();
        }
        finally
        {
            jedis.close();
        }

        return val;
    }

    public static Long ttl(JedisPool jedisPool, String key)
    {
        long val = 0L;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.ttl(key);
            }
            catch (Exception var9)
            {
                var9.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static String flushDB(JedisPool jedisPool)
    {
        String val = null;
        Jedis jedis = null;

        try
        {
            jedis = jedisPool.getResource();
            val = jedis.flushDB();
        }
        catch (Exception var7)
        {
            var7.printStackTrace();
        }
        finally
        {
            jedis.close();
        }

        return val;
    }

    public static Long lpush(JedisPool jedisPool, String key, String string)
    {
        long val = 0L;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            String[] strings = new String[]{string};
            val = lpush(jedisPool, key, strings);
            return val;
        }
    }

    public static Long lpush(JedisPool jedisPool, String key, String[] strings)
    {
        long val = 0L;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.lpush(key, strings);
            }
            catch (Exception var10)
            {
                var10.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static Long lrem(JedisPool jedisPool, String key, long count, String value)
    {
        long val = 0L;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.lrem(key, count, value);
            }
            catch (Exception var12)
            {
                var12.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static List<String> lrange(JedisPool jedisPool, String key, long start, long end)
    {
        List<String> val = null;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.lrange(key, start, end);
            }
            catch (Exception var12)
            {
                var12.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static Long llen(JedisPool jedisPool, String key)
    {
        long val = 0L;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.llen(key);
            }
            catch (Exception var9)
            {
                var9.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static String hget(JedisPool jedisPool, String key, String field)
    {
        String val = null;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.hget(key, field);
            }
            catch (Exception var9)
            {
                var9.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static Long hset(JedisPool jedisPool, String key, String field, String value)
    {
        long val = -1L;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.hset(key, field, value);
            }
            catch (Exception var11)
            {
                var11.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static Long hdel(JedisPool jedisPool, String key, String field)
    {
        long val = 0L;
        String[] fields = new String[]{field};
        val = hdel(jedisPool, key, fields);
        return val;
    }

    public static Long hdel(JedisPool jedisPool, String key, String[] fields)
    {
        long val = 0L;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.hdel(key, fields);
            }
            catch (Exception var10)
            {
                var10.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static Boolean hexists(JedisPool jedisPool, String key, String field)
    {
        Boolean val = false;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.hexists(key, field);
            }
            catch (Exception var9)
            {
                var9.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static Long hlen(JedisPool jedisPool, String key)
    {
        long val = 0L;
        if (isBlank(key))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.hlen(key);
            }
            catch (Exception var9)
            {
                var9.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
        }
    }

    public static Object evalsha(JedisPool jedisPool, String sha1, List<String> keys, List<String> args)
    {
        Object val = null;
        Jedis jedis = null;

        try
        {
            jedis = jedisPool.getResource();
            val = jedis.evalsha(sha1, keys, args);
        }
        catch (Exception var10)
        {
            var10.printStackTrace();
        }
        finally
        {
            jedis.close();
        }

        return val;
    }

    public static String scriptLoad(JedisPool jedisPool, String script)
    {
        String val = null;
        if (isBlank(script))
        {
            return val;
        }
        else
        {
            Jedis jedis = null;

            try
            {
                jedis = jedisPool.getResource();
                val = jedis.scriptLoad(script);
            }
            catch (Exception var8)
            {
                var8.printStackTrace();
            }
            finally
            {
                jedis.close();
            }

            return val;
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

    public static boolean isBlank(String[] str)
    {
        return str == null || str.length == 0;
    }
}
