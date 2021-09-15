package com.wisdom.acm.sys.controller;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import com.wisdom.acm.sys.form.SysUserUpdateFrom;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.encrypt.algorithm.AesEncryptAlgorithm;
import com.wisdom.encrypt.core.EncryptionConfig;
import com.wisdom.encrypt.springboot.annotation.DecryptRequestBody;
import com.wisdom.encrypt.springboot.annotation.EncryptResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "encrypt")
public class EncryptController {

    @Autowired
    private EncryptionConfig encryptionConfig;

    @DecryptRequestBody
    @EncryptResponseBody
    @PostMapping("/save")
    public ApiResult save(@RequestBody SysUserUpdateFrom user) {
        return ApiResult.success(user);
    }


    /**
     * spring.encrypt.enabled = true
     * @param user
     * @return
     */
    @PostMapping("/save1")
    public ApiResult save1(@RequestBody SysUserUpdateFrom user) {
        return ApiResult.success(user);
    }

    /**
     * 此为URI传参，payload为空时，不需要使用@DecryptRequestBody
     * @param name
     * @return
     */
    @EncryptResponseBody
    @GetMapping("/{name}")
    public String getEncryptStr(@PathVariable("name")String name) {
        return "加密字符串" + name;
    }


    @GetMapping("/aes/{content}")
    public String getAesEncryptString(@PathVariable("content")String content) {
        AesEncryptAlgorithm aesEncryptAlgorithm = new AesEncryptAlgorithm();
        String encrpty = null;
        try {
            encrpty = aesEncryptAlgorithm.encrypt(content, encryptionConfig.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrpty;
    }

    @PostMapping("/aes/json")
    public String getAesEncryptJson(@RequestBody Map<String, Object> map) {
        AesEncryptAlgorithm aesEncryptAlgorithm = new AesEncryptAlgorithm();
        String encrpty = null;
        try {
            JSONObject json = new JSONObject(map);
            String toJSONString = json.toJSONString();
            //System.out.println(toJSONString);
            encrpty = aesEncryptAlgorithm.encrypt(toJSONString, encryptionConfig.getKey());
            //System.out.println(encrpty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrpty;
    }

}