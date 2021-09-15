package com.wisdom.cache.rest;

import java.util.List;

import com.wisdom.cache.service.TestService;
import com.wisdom.cache.utils.TreeUtils;
import com.wisdom.cache.vo.CacheTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.wisdom.cache.service.ICacheManager;

@RestController
@RequestMapping("cache")
public class CacheRest {
    @Autowired
    private ICacheManager cacheManager;

    @Autowired
    private TestService testService;


    @RequestMapping("/list")
    @ResponseBody
    public List<CacheTree> listAll() {
        return TreeUtils.buildTree(cacheManager.getAll());
    }

    @RequestMapping(path = "/pre/{pre:.*}", method = RequestMethod.GET)
    @ResponseBody
    public List<CacheTree> listPre(@PathVariable("pre") String pre) {
        return TreeUtils.buildTree(cacheManager.getByPre(pre));
    }

    @RequestMapping(path = "/{key:.*}", method = RequestMethod.GET)
    @ResponseBody
    public String get(@PathVariable("key") String key) {
        return cacheManager.get(key);
    }

    @RequestMapping(path = "/remove", method = {RequestMethod.DELETE})
    @ResponseBody
    public void removeAll() {
        cacheManager.removeAll();
    }

    @RequestMapping(path = "/pre/{pre:.*}", method = {RequestMethod.DELETE})
    @ResponseBody
    public void removePre(@PathVariable("pre") String pre) {
        cacheManager.removeByPre(pre);
    }

    @RequestMapping(path = "/{key:.*}", method = RequestMethod.DELETE)
    @ResponseBody
    public void removeKey(@PathVariable("key") String key) {
        cacheManager.remove(key);
    }

    @RequestMapping(path = "/{key:.*}", method = RequestMethod.PUT)
    @ResponseBody
    public void updateTime(@PathVariable("key") String key, int hour) {
        cacheManager.update(key, hour);
    }

    @RequestMapping("")
    public String index() {
        return "/static/cache/index.html";
    }


    @RequestMapping(value = "/test/get", method = RequestMethod.GET)
    public @ResponseBody
    void getTest() throws Exception {
        testService.getTestCache();
    }

    @RequestMapping(value = "/test/{user_id}/get", method = RequestMethod.GET)
    public @ResponseBody
    void getTest(@PathVariable("user_id")String user_id) throws Exception {
        testService.getTestCache(user_id);
    }

    @RequestMapping(value = "/test/delete", method = RequestMethod.GET)
    public @ResponseBody
    void deleteTestCache() throws Exception {
        testService.deleteTestCache();
    }

    @RequestMapping(value = "/test/{key}", method = RequestMethod.GET)
    public @ResponseBody
    void getTestCacheByKey(@PathVariable("key")String key) throws Exception {
        testService.getTestCacheByKey(key);
    }

    @RequestMapping(value = "/test/add/{key}/{value}", method = RequestMethod.GET)
    public @ResponseBody
    void setTestCacheByKey(@PathVariable("key")String key, @PathVariable("value")Object value) throws Exception {
        //
        testService.setTestCacheByKey(key, value);
    }

}
