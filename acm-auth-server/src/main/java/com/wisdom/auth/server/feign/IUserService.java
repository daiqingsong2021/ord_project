package com.wisdom.auth.server.feign;

import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.auth.server.configuration.FeignConfiguration;
import com.wisdom.auth.server.util.user.JwtAuthenticationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户
 */
@FeignClient(value = "acm-module-sys", configuration = FeignConfiguration.class)
public interface IUserService {

    @RequestMapping(value = "/user/validate", method = RequestMethod.POST)
    public ApiResult<UserInfo> validate(@RequestBody JwtAuthenticationRequest authenticationRequest);

    @RequestMapping(value = "/user/validate/sn", method = RequestMethod.POST)
    public ApiResult<UserInfo> snValidate(@RequestBody JwtAuthenticationRequest authenticationRequest);

    @RequestMapping(value = "/user/validate/sso", method = RequestMethod.POST)
    public ApiResult<UserInfo> ssoValidate(@RequestBody JwtAuthenticationRequest authenticationRequest);
}
