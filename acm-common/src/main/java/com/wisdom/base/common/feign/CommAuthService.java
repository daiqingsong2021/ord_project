package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.JwtAuthenticationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "acm-auth-server",configuration = FeignConfiguration.class)
public interface CommAuthService {

    /**
     * 根据用户信息得到token
     *
     * @param userVo
     * @return
     */
    @PostMapping(value = "/jwt/token")
    public ApiResult<String> createAuthenticationToken(@RequestBody JwtAuthenticationRequest userVo);

    /**
     * 根据用户信息得到token
     *
     * @param userVo
     * @return
     */
    @PostMapping(value = "/jwt/token/sn")
    public ApiResult<String> createAuthenticationTokenSn(@RequestBody JwtAuthenticationRequest userVo);

}
