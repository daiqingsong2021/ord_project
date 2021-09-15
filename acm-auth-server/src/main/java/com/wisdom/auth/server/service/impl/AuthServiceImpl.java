package com.wisdom.auth.server.service.impl;

import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.auth.server.common.util.jwt.JWTInfo;
import com.wisdom.auth.server.feign.IUserService;
import com.wisdom.auth.server.service.AuthService;
import com.wisdom.auth.server.util.user.JwtAuthenticationRequest;
import com.wisdom.auth.server.util.user.JwtTokenUtil;
import com.wisdom.base.common.exception.auth.UserInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthServiceImpl implements AuthService {

    private JwtTokenUtil jwtTokenUtil;
    private IUserService userService;

    @Autowired
    public AuthServiceImpl(
            JwtTokenUtil jwtTokenUtil,
            IUserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    public String login(JwtAuthenticationRequest authenticationRequest) throws Exception {
        ApiResult<UserInfo> apiResult = userService.validate(authenticationRequest);
        if (apiResult.getStatus() == 200) {
            UserInfo info = apiResult.getData();
            return jwtTokenUtil.generateToken(new JWTInfo(info.getUserName(), info.getId() + "", info.getLastUpdIp(),info.getActuName(),info.getUserType()));
        } else {
            throw new UserInvalidException("账户名或密码错误!");
        }
    }

    @Override
    public String snLogin(JwtAuthenticationRequest authenticationRequest) throws Exception {
        ApiResult<UserInfo> apiResult = userService.snValidate(authenticationRequest);
        if (apiResult.getStatus() == 200) {
            UserInfo info = apiResult.getData();
            return jwtTokenUtil.generateToken(new JWTInfo(info.getUserName(), info.getId() + "", info.getLastUpdIp(),info.getActuName(),info.getUserType()));
        } else {
            throw new UserInvalidException(apiResult.getMessage());
        }
    }

    @Override
    public void validate(String token) throws Exception {
        jwtTokenUtil.getInfoFromToken(token);
    }

    @Override
    public String refresh(String oldToken) throws Exception {
        return jwtTokenUtil.generateToken(jwtTokenUtil.getInfoFromToken(oldToken));
    }
}
