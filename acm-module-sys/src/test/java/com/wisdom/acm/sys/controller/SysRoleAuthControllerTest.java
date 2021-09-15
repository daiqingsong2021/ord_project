package com.wisdom.acm.sys.controller;

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
 * SysRoleAuthController Tester.
 *
 * @author hejian
 * @version 1.0
 * @since <pre>2019-03-18</pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SysRoleAuthControllerTest {
    private String url = "http://127.0.0.1:8762/auth";
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
    public void test3UpdateRoleFunc() {
        //String api = "/{roleId}/update/auth";
        String api = "/9999/update/auth";

        Integer p0 = 1;
        Integer id = 1010;
        
        Map map = new HashMap<String, Object>();
        map.put("id", id);
        
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
    public void test2QueryOrgIdsByUserIdAndFuncCode() {
        //String api = "/{funcCode}/{userId}/orgId/list";
        String api = "/funcCode/9999/orgId/list";

        String p0 = "String100";
        Integer p1 = 1;
        
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
    public void test2QueryAuthByRoleId() {
        //String api = "/{roleId}/list";
        String api = "/9999/list";

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


}
