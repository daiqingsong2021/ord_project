package com.wisdom.auth.server.controller;

import com.wisdom.auth.server.service.AuthService;
import com.wisdom.auth.server.util.user.JwtAuthenticationRequest;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.ObjectRestResponse;
import com.wisdom.base.common.enums.ResultEnum;
import com.wisdom.base.common.util.ClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("jwt")
@Slf4j
@CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.POST}, origins="*")
public class AuthController {
    @Value("${jwt.token-header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "token", method = RequestMethod.POST)
    public ApiResult<String> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws Exception {
        log.info(authenticationRequest.getUserName()+" require logging...");
        String clientIp = ClientUtil.getClientIp(request);
        authenticationRequest.setUserHost(clientIp);
        final String token = authService.login(authenticationRequest);
        return ApiResult.success(token);
    }

    @RequestMapping(value = "token/sn", method = RequestMethod.POST)
    public ApiResult<String> createAuthenticationTokenSn(@RequestBody JwtAuthenticationRequest authenticationRequest) throws Exception {
        log.info(authenticationRequest.getUserName()+" require logging...");
        String clientIp = ClientUtil.getClientIp(request);
        authenticationRequest.setUserHost(clientIp);
        final String token = authService.snLogin(authenticationRequest);
        return ApiResult.success(token);
    }

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public ObjectRestResponse<String> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws Exception {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        return new ObjectRestResponse<>().data(refreshedToken);
    }

    @RequestMapping(value = "verify", method = RequestMethod.GET)
    public ObjectRestResponse<?> verify(String token) throws Exception {
        authService.validate(token);
        return new ObjectRestResponse<>();
    }
}
