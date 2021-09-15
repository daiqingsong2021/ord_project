package com.wisdom.cache.service;

import com.wisdom.cache.annotation.Cache;
import com.wisdom.cache.annotation.CacheClear;
import com.wisdom.cache.api.CacheAPI;
import com.wisdom.cache.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TestService {

    @Autowired
    private CacheAPI cacheAPI;

    @Value("${redis.expire}")
    private int expire;

    @Cache(key = "test:cache")
    public UserInfo getTestCache() {
        UserInfo user = new UserInfo();
        user.setName("u1");
        user.setAge(8);
        System.out.println("Cache User: " + user);
        return user;
    }

    @CacheClear(keys = {"test:cache", "test.cache:userId"})   //也可以写成@CacheClear(keys = {"test:cache"}) 会带着子节点缓存一起删除
    public void deleteTestCache() {
        System.out.println("delete User Cache Success");
    }


    @Cache(key = "test:cache:userId{1}")
    public UserInfo getTestCache(String user_id) {
        UserInfo user = new UserInfo();
        user.setUserId(user_id);
        user.setName("u1");
        user.setAge(8);
        System.out.println("Cache User(" + user_id + "): " + user);
        return user;
    }

    /**
     *
     * @param key
     * @param value
     */
    public void setTestCacheByKey(String key, Object value) {
        cacheAPI.set(key, value, expire);
    }

    /**
     *
     * @param key
     */
    public void getTestCacheByKey(String key) {
        String value = cacheAPI.get(key);
        System.out.println("Cache Get Key: " + value);
    }



}

