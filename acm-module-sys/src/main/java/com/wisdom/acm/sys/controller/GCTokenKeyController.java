package com.wisdom.acm.sys.controller;

import com.wisdom.acm.sys.util.EncodingUtil;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.webservice.PropertiesLoaderUtils;
import com.wisdom.webservice.token.RSASecurityUtils;
import com.wisdom.webservice.waiyantoken.WaiYanTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author：wqd
 * Date：2019-11-11 15:23
 * Description：<描述>
 */
@RestController
@RequestMapping(value = "gcToken")
public class GCTokenKeyController {
    private static Logger logger = LoggerFactory.getLogger(GCTokenKeyController.class);

    /**
     * 用户基本信息服务
     */
    @Autowired
    private CommUserService commUserService;

    @GetMapping(value = "/getTokenKey")
    public ApiResult getTokenKey() {
        String token = WaiYanTokenUtils.getInstance().getToken();
        if (StringUtils.isBlank(token)) {
            throw new BaseException("获取登录合同外延token失败");
        }
        StringBuilder stringBuilder = new StringBuilder();
        UserInfo userInfo = commUserService.getLoginUser();//获取用户信息
        if (null == userInfo || StringUtils.isBlank(userInfo.getUserName())) {
            throw new BaseException("获取用户信息失败");
        }
        stringBuilder.append(token);
        stringBuilder.append(",");
        stringBuilder.append(userInfo.getUserName());

        PropertiesLoaderUtils prop = new PropertiesLoaderUtils("application.yml");
        String encrypt;
        try {
            encrypt = RSASecurityUtils.encrypt(prop.getProperty("webservice.hetongwaiyantoken.publickey"), stringBuilder.toString());
        } catch (Exception e) {
            logger.error("加密gcTokenKey失败, 原因为 {}", e);
            throw new BaseException("加密gcTokenKey失败");
        }
        String uriComponent = EncodingUtil.encodeURIComponent(encrypt);
        return ApiResult.success(uriComponent);
    }
}
