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
 * BaseCoderuleCellController Tester.
 *
 * @author hejian
 * @version 1.0
 * @since <pre>2019-03-18</pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BaseCoderuleCellControllerTest {
    private String url = "http://127.0.0.1:8780/coderulecell";
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
    public void test2GetCoderuleCellById() {
        //String api = "/{id}/info";
        String api = "/9999/info";

        Integer p0 = 1;
        
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
    public void test1AddCoderuleCell() {
        String api = "/add";
        
        Map map = new HashMap<String, Object>();
        map.put("ruleId", 9999);
        map.put("ruleTypeId", 9999);
        map.put("position", 9999);
        map.put("ruleCellName", "ruleCellName100");
        map.put("ruleType", "ruleType100");
        map.put("cellValue", "cellValue100");
        map.put("maxLength", 9999);
        map.put("fillChar", 9999);
        map.put("seqMinValue", 9999);
        map.put("seqSteep", 9999);
        map.put("connector", "connector100");
        
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
    public void test3UpdateCoderuleCell() {
        String api = "/update";
        Integer id = 1010;
        
        Map map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("ruleType", "ruleType100");
        map.put("cellValue", "cellValue100");
        map.put("maxLength", 9999);
        map.put("fillChar", 9999);
        map.put("seqMinValue", 9999);
        map.put("seqSteep", 9999);
        map.put("connector", "connector100");
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(map, headers);
        // ????????????
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
    public void test4DeleteCoderuleCellByIds() {
        String api = "/delete";
        int[] ids = {9999, 9999};
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(ids, headers);
        // ????????????
        ResponseEntity<String> resultEntity = restTemplate.exchange(url + api, HttpMethod.DELETE, formEntity, String.class);
        String contentAsString = resultEntity.getBody();
        
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        System.out.println("==>contentAsString: " + contentAsString);
        Assert.assertNotNull(contentAsString);
    }


}
