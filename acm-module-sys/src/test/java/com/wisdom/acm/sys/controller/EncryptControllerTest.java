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
 * EncryptController Tester.
 *
 * @author hejian
 * @version 1.0
 * @since <pre>2019-03-25</pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EncryptControllerTest {
    private String url = "http://127.0.0.1:8762/encrypt";
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
    public void test3Save() {
        String api = "/ssl/save";
        
        Map map = new HashMap<String, Object>();
        map.put("id", 9999);
        map.put("userName", "userName100");
        map.put("actuName", "actuName100");
        map.put("sex", 9999);
        map.put("phone", "phone100");
        map.put("cardType", "cardType100");
        map.put("cardNum", "cardNum100");
        map.put("birth", "2019-12-12");
        map.put("entryDate", "2019-12-12");
        map.put("quitDate", "2019-12-12");
        map.put("level", "level100");
        map.put("sort", 9999);
        map.put("staffStatus", 9999);
        // 参数 SysUserUpdateFrom 
        map.put("roles", "roles100");
        map.put("orgId", 9999);
        
        //
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity formEntity = new HttpEntity(map, headers);
        String contentAsString = restTemplate.getForObject(url + api, String.class, formEntity);
        
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        System.out.println("==>contentAsString: " + contentAsString);
        Assert.assertNotNull(contentAsString);
    }

    /**
     *
     */
    @Test
    public void test3EncryptStr() {
        //String api = "/{name}";
        String api = "/name";

        String p0 = "String100";
        
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
    public void test3AesEncrypt() {
        //String api = "/aes/{content}";
        String api = "/aes/content";

        String p0 = "String100";
        
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
