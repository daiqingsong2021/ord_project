package com.wisdom.acm.sys.controller.app;

import com.wisdom.acm.sys.service.SysLoginService;
import com.wisdom.acm.sys.vo.app.AppUserInfoVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommAuthService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.JwtAuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


@RestController
@RequestMapping("app")
public class SysAppLoginController extends BaseController {

    @Autowired
    private SysLoginService userLoginService;

    @Autowired
    private CommAuthService commAuthService;
    /**
     * 获取用户登录首页信息
     * @param
     *
     * @return
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @AddLog(title = "用户登录",module = LoggerModuleEnum.SYS)
    public ApiResult getUserLoginInfo(@RequestBody JwtAuthenticationRequest userVo) {

        ApiResult<String> apiResult = commAuthService.createAuthenticationToken(userVo);
        String token = "";
        if(apiResult.getStatus() == 200){
            token = apiResult.getData();
        }else {
            throw new BaseException(apiResult.getMessage());
        }
        String userName = userVo.getUserName();
        AppUserInfoVo appUserInfoVo = userLoginService.getUserAppLoginInfo(userName,token);
        this.setAcmLogger(new AcmLogger("用户登录，用户名：" + appUserInfoVo.getName()));
        return ApiResult.success(appUserInfoVo);
    }
}
