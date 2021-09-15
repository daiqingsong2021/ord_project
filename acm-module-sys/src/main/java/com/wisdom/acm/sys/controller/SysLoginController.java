package com.wisdom.acm.sys.controller;

import com.wisdom.acm.sys.service.SysLoginService;
import com.wisdom.acm.sys.vo.SysUserLoginVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


@RestController
@RequestMapping("login")
public class SysLoginController extends BaseController {

    @Autowired
    private SysLoginService userLoginService;

    @Autowired
    protected HttpServletRequest request;
    /**
     * 获取用户登录首页信息
     * @param
     *
     * @return
     */
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    @AddLog(title = "用户登录",module = LoggerModuleEnum.SYS)
    public ApiResult getUserInfo() {
        String userName = null;
        try {
            userName = URLDecoder.decode(request.getHeader("userName"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SysUserLoginVo sysUserLoginVo = userLoginService.mgetUserLoginInfo(userName);
        this.setAcmLogger(new AcmLogger("用户登录，用户名：" + sysUserLoginVo.getActuName()));
        return ApiResult.success(sysUserLoginVo);
    }
}
