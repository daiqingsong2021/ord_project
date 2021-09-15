package com.wisdom.acm.base.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.*;

/**
 * BaseTmplTaskController Tester.
 *
 * @author hejian
 * @version 1.0
 * @since <pre>2019-03-18</pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BaseTmplTaskControllerTest {
    private String url = "http://127.0.0.1:8780/tmplTask";
    private Integer id = 9999;
    private RestTemplate restTemplate = null;

    @Before
    public void before() throws Exception {
        //Can test all controllers
        restTemplate = new RestTemplate();
    }

    @After
    public void after() throws Exception {
    }

    /**
     *
     */
    @Test
    public void test2QueryTmplTaskTreeList() {
        String api = "/treeList";
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(headers);
        String contentAsString = restTemplate.getForObject(url + api, String.class, formEntity);
        
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        System.out.println("==>contentAsString: " + contentAsString);
        Assert.assertNotNull(contentAsString);
    }

    /**
     *
     */
    @Test
    public void test1AddTmplTask() {
        String api = "/add";
        
        Map map = new HashMap<String, Object>();
        map.put("tmplId", 9999);
        map.put("parentId", 9999);
        map.put("code", "code100");
        map.put("name", "name100");
        map.put("planDrtn", 9999);
        map.put("planHours", 9999);
        map.put("planType", "planType100");
        map.put("planLevel", "planLevel100");
        map.put("isWbsFb", 9999);
        map.put("isCtrlUser", 9999);
        map.put("taskType", "taskType100");
        map.put("drtnType", "drtnType100");
        map.put("remark", "remark100");
        map.put("status", "status100");
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(map, headers);
        String contentAsString = restTemplate.postForObject(url + api, formEntity, String.class);
        
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        System.out.println("==>contentAsString: " + contentAsString);
        id = Integer.valueOf(String.valueOf(JSON.parseObject(String.valueOf(jsonObject.get("data"))).get("id")));
        System.out.println("==>id=" + id);
        Assert.assertNotNull(id);
    }

    /**
     *
     */
    @Test
    public void test3UpdateTmplTask() {
        String api = "/update";
        Integer id = 1010;
        
        Map map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("code", "code100");
        map.put("name", "name100");
        map.put("planDrtn", 9999);
        map.put("planHours", 9999);
        map.put("planType", "planType100");
        map.put("planLevel", "planLevel100");
        map.put("isWbsFb", 9999);
        map.put("isCtrlUser", 9999);
        map.put("taskType", "taskType100");
        map.put("drtnType", "drtnType100");
        map.put("remark", "remark100");
        map.put("status", "status100");
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(map, headers);
        // 发送请求
        ResponseEntity<String> resultEntity = restTemplate.exchange(url + api, HttpMethod.PUT, formEntity, String.class);
        String contentAsString = resultEntity.getBody();
        
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        System.out.println("==>contentAsString: " + contentAsString);
        Assert.assertNotNull(contentAsString);
    }

    /**
     *
     */
    @Test
    public void test2QueryTmplTaskInfo() {
        //String api = "/{id}/info";
        String api = "/9999/info";

        int p0 = 9999;
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(headers);
        String contentAsString = restTemplate.getForObject(url + api, String.class, formEntity);
        
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        System.out.println("==>contentAsString: " + contentAsString);
        Assert.assertNotNull(contentAsString);
    }

    /**
     *
     */
    @Test
    public void test1AddTmplPlan() {
        String api = "/addPlan";
        
        Map map = new HashMap<String, Object>();
        map.put("tmplCode", "tmplCode100");
        map.put("tmplName", "tmplName100");
        map.put("isGlobal", 9999);
        map.put("status", "status100");
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(map, headers);
        String contentAsString = restTemplate.postForObject(url + api, formEntity, String.class);
        
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        System.out.println("==>contentAsString: " + contentAsString);
        id = Integer.valueOf(String.valueOf(JSON.parseObject(String.valueOf(jsonObject.get("data"))).get("id")));
        System.out.println("==>id=" + id);
        Assert.assertNotNull(id);
    }

    /**
     *
     */
    @Test
    public void test3UpdateTmplPlan() {
        String api = "/updatePlan";
        Integer id = 1010;
        
        Map map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("tmplCode", "tmplCode100");
        map.put("tmplName", "tmplName100");
        map.put("isGlobal", 9999);
        map.put("status", "status100");
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(map, headers);
        // 发送请求
        ResponseEntity<String> resultEntity = restTemplate.exchange(url + api, HttpMethod.PUT, formEntity, String.class);
        String contentAsString = resultEntity.getBody();
        
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        System.out.println("==>contentAsString: " + contentAsString);
        Assert.assertNotNull(contentAsString);
    }

    /**
     *
     */
    @Test
    public void test2QueryTmplPlanInfo() {
        //String api = "/{id}/tmplPlan/info";
        String api = "/9999/tmplPlan/info";

        int p0 = 9999;
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(headers);
        String contentAsString = restTemplate.getForObject(url + api, String.class, formEntity);
        
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        System.out.println("==>contentAsString: " + contentAsString);
        Assert.assertNotNull(contentAsString);
    }

    /**
     *
     */
    @Test
    public void test4DeleteTmplTask() {
        String api = "/delete";
        int[] ids = {9999, 9999};
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(ids, headers);
        // 发送请求
        ResponseEntity<String> resultEntity = restTemplate.exchange(url + api, HttpMethod.DELETE, formEntity, String.class);
        String contentAsString = resultEntity.getBody();
        
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        System.out.println("==>contentAsString: " + contentAsString);
        Assert.assertNotNull(contentAsString);
    }


}
