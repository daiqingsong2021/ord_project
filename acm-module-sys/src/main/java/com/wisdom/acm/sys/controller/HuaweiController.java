package com.wisdom.acm.sys.controller;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.wisdom.acm.sys.util.HuaweiAPIUtil;
import com.wisdom.base.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/huawei")
@Slf4j
public class HuaweiController {

    private String key = "4c9d4071-8f13-4b61-8a63-392b1c0ceec5";

    @Value("${huawei.frontEndUrl:http://hw.wisdomidata.com/login}")
    private String frontEndUrl;

    private Map<String, Date> instance = Maps.newHashMap();

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

    /**
     * 购买
     * @param request
     * @param response
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/product")
    public ResponseEntity product(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        log.info(request.getQueryString());
        Map<String, Object> result = Maps.newHashMap();
        String signature = "";
        String body = "";
        try {
            boolean flag = HuaweiAPIUtil.verifyRequestParams(request, key, 256);
            if (!flag) {
                result.put("resultCode", "000001");
            } else {
                String activity = request.getParameter("activity");
                switch (activity){
                    case "newInstance":
                        result = this.buy(request);
                        break;
                    case "refreshInstance":
                        result = this.renew(request);
                        break;
                    case "expireInstance":
                        result = this.overdue(request);
                        break;
                    case "releaseInstance":
                        result = this.release(request);
                        break;
                    default:
                        result.put("resultCode","000002");
                        result.put("resultMsg","activity:"+activity +"不合法");
                }
            }
            body = JsonUtil.toJson(result);
            signature = HuaweiAPIUtil.generateResponseBodySignature(key, body);
        }catch (Exception e){
            log.error(e.getMessage());
            result.put("resultCode", "000005");
            body = JsonUtil.toJson(result);
            signature = HuaweiAPIUtil.generateResponseBodySignature(key, body);
        }
        return ResponseEntity.ok().header("Body-Sign", "sign_type=\"HMAC-SHA256\", signature= \"" + signature + "\"").body(body);
    }

    private Map<String,Object> buy(HttpServletRequest request) throws ParseException {
        Map<String, Object> result = Maps.newHashMap();
        String instanceId = request.getParameter("businessId");
        String expireTime = request.getParameter("expireTime");
        instance.put(instanceId, sdf.parse(expireTime));

        String userName = "admin";
        String password = "123456";
        result.put("resultCode", "000000");
        result.put("instanceId", instanceId);
        result.put("encryptType",2);
        result.put("appInfo", ImmutableMap.builder()
                .put("frontEndUrl", frontEndUrl)
                .put("adminUrl", frontEndUrl)
                .put("userName", HuaweiAPIUtil.generateSaaSUsernameOrPwd(key, userName, 128))
                .put("password", HuaweiAPIUtil.generateSaaSUsernameOrPwd(key, password, 128))
                .build());
        return result;
    }

    private Map<String,Object> renew(HttpServletRequest request) throws ParseException{
        Map<String, Object> result = Maps.newHashMap();
        String instanceId = request.getParameter("instanceId");
        String expireTime = request.getParameter("expireTime");
        if(instance.containsKey(instanceId)) {
            instance.put(instanceId, sdf.parse(expireTime));
            result.put("resultCode", "000000");
        }else {
            result.put("resultCode", "000003");
        }
        return result;
    }
    private Map<String,Object> overdue(HttpServletRequest request) {
        Map<String, Object> result = Maps.newHashMap();
        String instanceId = request.getParameter("instanceId");
        if(instance.containsKey(instanceId)) {
            log.info(instanceId + "已过期");
            instance.put(instanceId, null);
            result.put("resultCode", "000000");
        }else{  //实例ID不存在
            result.put("resultCode", "000003");
        }
        return result;
    }

    /**
     * 释放
     * @param request
     * @return
     */
    private Map<String,Object> release(HttpServletRequest request) {
        Map<String, Object> result = Maps.newHashMap();
        String instanceId = request.getParameter("instanceId");
        if(instance.containsKey(instanceId)) {
            log.info(instanceId + "释放");
            instance.remove(instanceId);
            result.put("resultCode", "000000");
        }else{  //实例ID不存在
            result.put("resultCode", "000003");
        }
        return result;
    }
}
