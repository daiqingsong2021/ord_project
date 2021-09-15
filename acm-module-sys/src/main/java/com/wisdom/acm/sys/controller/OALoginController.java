package com.wisdom.acm.sys.controller;

import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommAuthService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.JwtAuthenticationRequest;
import com.wisdom.base.common.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@RestController
public class OALoginController {

    @Value("${oa.ssoLoginUrl}")
    private String ssoLoginUrl;

    @Value("${oa.loginUrl}")
    private String loginUrl;

    @Autowired
    private CommUserService userService;

    @Autowired
    private CommAuthService authService;

    private static Logger logger = LoggerFactory.getLogger(OALoginController.class);

    /**
     * 登录控制器
     *
     * @param request  request
     * @param response response
     * @throws Exception Exception
     */
    @RequestMapping("/sologin")
    public void loginIn(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String requestMethod = request.getMethod();
        requestMethod = requestMethod.toUpperCase();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String uid = "";
        String pwd = "";
        if (request.getParameter("uid") != null) {
            uid = request.getParameter("uid");
        }
        if (request.getParameter("pwd") != null) {
            pwd = request.getParameter("pwd");
        }
        logger.info("====OA单点登录，uid："+uid+",pwd:" +pwd+ "=====");
            if (uid != null && pwd != "") {
                //to the protected page
                //set cookies
                Cookie newCookie = new Cookie("UserAccount", uid);
                response.addCookie(newCookie);
                if (!ObjectUtils.isEmpty(uid)) {
                    ApiResult<UserInfo> result = this.userService.getUserInfoByName(uid);
                    logger.info("====OA单点登录，UserInfo："+result);
                    if (!ObjectUtils.isEmpty(result)) {
                        if (result.isSuccess()) {
                            if(ObjectUtils.isEmpty(result.getData())){
                                throw new BaseException("该用户不在本系统中，无法单点登录");
                            }
                            String password = result.getData().getPassword();
                            JwtAuthenticationRequest user = new JwtAuthenticationRequest(uid, password);
                            ApiResult<String> result1 = this.authService.createAuthenticationTokenSn(user);
                            if (result1.isSuccess()) {
                                String url = this.ssoLoginUrl + (this.ssoLoginUrl.indexOf("?") > 0 ? "&" : "?") + "token=" + result1.getData();
                                logger.info("====OA单点登录，url："+url);
                                response.sendRedirect(url);
                                return;
                            }
                        }
                    }

                }
                //登录不成功的页面 转到 之前登录页面
                logger.info("====OA单点登录，loginUrl："+ this.loginUrl);
                response.sendRedirect(this.loginUrl);
            } else {
                logger.error("验证错误!userId:"+uid);
                return;
            }
        return;
    }
}
