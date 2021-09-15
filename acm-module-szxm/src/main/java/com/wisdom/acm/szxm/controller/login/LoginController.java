package com.wisdom.acm.szxm.controller.login;

import EIAC.EAC.SSO.AppSSOBLL;
import EIAC.EAC.SSO.ReadConfig;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
public class LoginController {

    @Value("${sn.acmSoLoginUrl}")
    private String acmSoLoginUrl;

    @Value("${sn.acmLoginUrl}")
    private String acmLoginUrl;

    @Autowired
    private CommUserService userService;

    @Autowired
    private CommAuthService authService;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

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

        //发送到EAC验证
        String posturl = "";
        String defaulturl = request.getRequestURL().toString() + "?" + request.getQueryString();
        logger.info("defaulturl:" + defaulturl);
        AppSSOBLL app = new AppSSOBLL();
        String IASID;
        String TimeStamp = "";
        String ReturnURL = "";
        String UserAccount = "";
        String Result = "";
        String ErrorDescription = "";
        String Authenticator = "";
        if (request.getParameter("IASID") != null) {
            IASID = request.getParameter("IASID");
        }
        if (request.getParameter("ReturnURL") != null) {
            ReturnURL = request.getParameter("ReturnURL");
        }
        if (request.getParameter("UserAccount") != null) {
            UserAccount = request.getParameter("UserAccount");
        }
        if (request.getParameter("Result") != null) {
            Result = request.getParameter("Result");
        }
        if (request.getParameter("ErrorDescription") != null) {
            ErrorDescription = request.getParameter("ErrorDescription");
        }
        if (request.getParameter("Authenticator") != null) {
            Authenticator = request.getParameter("Authenticator");
        }
        if (request.getParameter("TimeStamp") != null) {
            TimeStamp = request.getParameter("TimeStamp");
        }


        //取时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();//得到当前系统时间
        String str_date1 = formatter.format(currentTime); //将日期时间格式化

        if (TimeStamp == "") {
            TimeStamp = str_date1;
        }
        logger.info("timestamp is {}", str_date1);

        //可以读配置文件
        String IASKey;
        IASID = ReadConfig.getString("IASID");
        IASKey = ReadConfig.getString("IASKey");
        //SSO的URL
        String SSOURL = ReadConfig.getString("PostUrl");

        logger.info("IASID is {},IASKey is{}, PostUrl is {}", IASID, IASKey, SSOURL);

        //得到post的html
        if (UserAccount == null || UserAccount == "") {
            posturl = app.PostString1(IASID, TimeStamp, defaulturl, null, SSOURL, IASKey);
            out.print(posturl);    //post to EAC(SSO)
        }
        //结束

        //接收从EAC返回的
        if (UserAccount != null && UserAccount != "") {
            if (!Result.equals("0")) {
                out.print("Result验证不成功！");
                out.print(IASID + ":" + TimeStamp + ":" + UserAccount + ":" + Result + ":" + ErrorDescription + ":" + Authenticator);
                return;
            }

            if (app.ValidateFromEAC(IASID, TimeStamp, UserAccount, Result, ErrorDescription, Authenticator)) {
                //to the protected page
                //set cookies
                Cookie newCookie = new Cookie("UserAccount", UserAccount);
                response.addCookie(newCookie);
                if (!ObjectUtils.isEmpty(UserAccount)) {
                    ApiResult<UserInfo> result = this.userService.getUserInfoByName(UserAccount);
                    if (!ObjectUtils.isEmpty(result)) {
                        if (result.isSuccess()) {
                            if(ObjectUtils.isEmpty(result.getData())){
                                throw new BaseException("该用户不在本系统中，无法单点登录");
                            }
                            String password = result.getData().getPassword();
                            JwtAuthenticationRequest user = new JwtAuthenticationRequest(UserAccount, password);
                            ApiResult<String> result1 = this.authService.createAuthenticationTokenSn(user);
                            if (result1.isSuccess()) {

                                String taskId = request.getParameter("taskId");
                                String procInstId = request.getParameter("procInstId");
                                String type = request.getParameter("type");

                                String url = this.acmSoLoginUrl + (this.acmSoLoginUrl.indexOf("?") > 0 ? "&" : "?") + "token=" + result1.getData();
                                if (StringHelper.isNotNullAndEmpty(taskId) && StringHelper.isNotNullAndEmpty(procInstId) && StringHelper.isNotNullAndEmpty(type)) {
                                    url = this.acmSoLoginUrl + (this.acmSoLoginUrl.indexOf("?") > 0 ? "&" : "?") + "token="
                                            + result1.getData() + "&taskId=" + taskId + "&procInstId=" + procInstId + "&type=" + type;
                                }
                                response.sendRedirect(url);
                                return;
                            }
                        }
                    }

                }
                //登录不成功的页面 转到 之前登录页面
                response.sendRedirect(this.acmLoginUrl);
            } else {
                out.print("验证错误");
                out.print(IASID + ":" + TimeStamp + ":" + UserAccount + ":" + Result + ":" + ErrorDescription + ":" + Authenticator);
                return;
            }

        }
        return;
    }

    /**
     *登录安全信息化平台
     * @param response
     * @throws IOException
     */
    @GetMapping("/checkLoginOpen")
    public ApiResult checkLoginOpen(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        //获取用户信息
        UserInfo userInfo = userService.getLoginUser();
        if(ObjectUtils.isEmpty(userInfo)){
            throw new BaseException("获取用户信息失败");
        }
        String pattern="^1[0-9]\\d{9}$";
        String phone = userInfo.getPhone();
        if(StringHelper.isNullAndEmpty(phone) || !Pattern.matches(pattern, phone)){
            throw new BaseException("该用户电话为空,或者电话格式错误");
        }

        String postUrl = szxmCommonUtil.loginSafeInfoPlatform(phone);
        if(StringHelper.isNullAndEmpty(postUrl)){
            throw new BaseException("生成安全信息化平台token失败");
        }
        return ApiResult.success(postUrl);
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     * @author lzf
     */
    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;
        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                for (int i = 1; i < arrSplit.length; i++) {
                    strAllParam = arrSplit[i];
                }
            }
        }
        return strAllParam;
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     * @author lzf
     */
    public static Map<String, String> urlSplit(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    public static void main(String args[]) {
        Map<String, String> xx = LoginController.urlSplit("http://www.baidu.com?url={\"taskId\":3162547,\"procInstId\":3162535,\"type\":\"flow\"}");
        System.out.println(xx.get("url"));
    }
}
