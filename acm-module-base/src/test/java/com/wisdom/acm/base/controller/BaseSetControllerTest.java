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
 * BaseSetController Tester.
 *
 * @author hejian
 * @version 1.0
 * @since <pre>2019-03-18</pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BaseSetControllerTest {
    private String url = "http://127.0.0.1:8780/set";
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
    public void test2QueryBoList() {
        String api = "/bo/list";
        
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
    public void test3UpdateProject() {
        String api = "/project/update";
        Integer id = 1010;
        
        Map map = new HashMap<String, Object>();
        map.put("taskDrtnType", "taskDrtnType100");
        map.put("cpmType", "cpmType100");
        map.put("cpmFloat", 9999);
        map.put("enableProjectTeam", 9999);
        map.put("shareWbs", 9999);
        map.put("message", 9999);
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(map, headers);
        // 发送请求
        ResponseEntity<String> resultEntity = restTemplate.exchange(url + api, HttpMethod.POST, formEntity, String.class);
        String contentAsString = resultEntity.getBody();
        
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        System.out.println("==>contentAsString: " + contentAsString);
        Assert.assertNotNull(contentAsString);
    }

    /**
     *
     */
    @Test
    public void test3UpdateDoc() {
        String api = "/doc/update";
        Integer id = 1010;
        List<String> banFileList = new ArrayList<String>();
        banFileList.add("banFileType2");
        banFileList.add("banFileType5");
        banFileList.add("banFileType8");

        Map map = new HashMap<String, Object>();
        map.put("uploadMax", "uploadMax100");
        // 参数 BaseSetDocUpdateForm 
        map.put("banFileType", banFileList);
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(map, headers);
        // 发送请求
        ResponseEntity<String> resultEntity = restTemplate.exchange(url + api, HttpMethod.POST, formEntity, String.class);
        String contentAsString = resultEntity.getBody();
        
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        System.out.println("==>contentAsString: " + contentAsString);
        Assert.assertNotNull(contentAsString);
    }

    /**
     *
     */
    @Test
    public void test2GetProjectInfo() {
        String api = "/project/info";
        
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
    public void test2GetDocInfo() {
        String api = "/doc/info";
        
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
    public void test2GetTimeInfo() {
        String api = "/time/info";
        
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
    public void test3UpdateTime() {
        String api = "/time/update";
        Integer id = 1010;
        
        Map map = new HashMap<String, Object>();
        map.put("timeUnit", "timeUnit100");
        map.put("drtnUnit", "drtnUnit100");
        map.put("dateFormat", "dateFormat100");
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(map, headers);
        // 发送请求
        ResponseEntity<String> resultEntity = restTemplate.exchange(url + api, HttpMethod.POST, formEntity, String.class);
        String contentAsString = resultEntity.getBody();
        
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        System.out.println("==>contentAsString: " + contentAsString);
        Assert.assertNotNull(contentAsString);
    }


}
