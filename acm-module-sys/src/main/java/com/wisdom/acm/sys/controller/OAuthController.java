package com.wisdom.acm.sys.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wisdom.acm.sys.modle.DeptInfo;
import com.wisdom.acm.sys.modle.UserInfo;
import com.wisdom.acm.sys.util.CookieUtil;
import com.wisdom.acm.sys.util.HttpClientUtil;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommAuthService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.JwtAuthenticationRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Controller
@RequestMapping("/oauth")
public class OAuthController {
    @Value("${sso.appId}")
    private String appId;

    @Value("${sso.appSecret}")
    private String appSecret;

    @Value("${sso.oauthUrl}")
    private String oauthUrl;

    @Value("${sso.uiasUrl}")
    private String uiasUrl;

    @Value("${sso.portalUrl}")
    private String portalUrl;

    @Value("${sso.ssoLoginUrl}")
    private String ssoLoginUrl;

    @Autowired
    private CommUserService userService;

    @Autowired
    private CommAuthService authService;

    private static Logger logger = LoggerFactory.getLogger(OALoginController.class);

    @RequestMapping("/userinfo")
    public String getUserInfo(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String sUserId = (String) request.getSession().getAttribute("userId");
        JSONObject jsonObject = new JSONObject();
        String access_token = CookieUtil.getCookie(request,"access_token");
        String openid = CookieUtil.getCookie(request,"openid");
//        Boolean flag = false;
//        if(StringUtils.isEmpty(userId)){
//            userId = "";
//        }
//        if(StringUtils.isEmpty(sUserId)){
//            sUserId = "";
//        }
//        if(!userId.equals(sUserId)){
//            flag = true;
//        }
        logger.info("====中科软单点登录，userId："+userId);
        if (userId != null) {
            //to the protected page
            //set cookies
            Cookie newCookie = new Cookie("UserAccount", userId);
            response.addCookie(newCookie);
            if (!ObjectUtils.isEmpty(userId)) {
                ApiResult<com.wisdom.base.common.vo.UserInfo> result = this.userService.getUserInfoByName(userId);
                logger.info("====中科软单点登录，UserInfo："+result);
                if (!ObjectUtils.isEmpty(result)) {
                    if (result.isSuccess()) {
                        if(ObjectUtils.isEmpty(result.getData())){
                            throw new BaseException("该用户不在本系统中，无法单点登录");
                        }
                        String password = result.getData().getPassword();
                        JwtAuthenticationRequest user = new JwtAuthenticationRequest(userId, password);
                        ApiResult<String> result1 = this.authService.createAuthenticationTokenSn(user);
                        if (result1.isSuccess()) {
                            String url = this.ssoLoginUrl + (this.ssoLoginUrl.indexOf("?") > 0 ? "&" : "?") + "token=" + result1.getData();
                            logger.info("====中科软单点登录，url："+url);
                            return "redirect:" + url;
                        }
                    }
                }

            }
            //登录不成功的页面 转到 之前登录页面
            logger.info("====OA单点登录，loginUrl："+ portalUrl + "login");
            return "redirect:" + portalUrl + "login";
        } else {
            logger.error("验证错误!userId is null!");
        }

        if(StringUtils.isEmpty(access_token) || StringUtils.isEmpty(openid) || StringUtils.isEmpty(userId) || !userId.equals(sUserId)){
            String code = request.getParameter("code");
            jsonObject.put("appId", appId);
            jsonObject.put("appSecret", appSecret);
            jsonObject.put("code", code);
            String accseeTokenUrl = oauthUrl + "/oauth2/access_token"; // oauth获取token接口
            logger.info("=====appId:"+appId+";appSecret:"+appSecret+";code:"+code+";accseeTokenUrl:"+accseeTokenUrl);
            String result = HttpClientUtil.doPost(accseeTokenUrl, jsonObject);
            logger.info("=====result:"+result);
            if (StringUtils.isNotEmpty(result)) {
                access_token = (String) JSONObject.parseObject(result).get("access_token");
                openid = (String) JSONObject.parseObject(result).get("openid");
                if(StringUtils.isEmpty(access_token) || StringUtils.isEmpty(openid)){
                    logger.info("=====redirect:"+portalUrl+"login");
                   return "redirect:" + portalUrl + "login";
                }else{
                    CookieUtil.setCookie(request, response, "access_token", access_token);
                    CookieUtil.setCookie(request, response, "openid", openid);
                }
            }
        }
        jsonObject = new JSONObject();
        jsonObject.put("appId", appId);
        jsonObject.put("appSecret", appSecret);
        jsonObject.put("access_token", access_token);
        jsonObject.put("openid", openid);
        String getUserInfoUrl = oauthUrl + "/oauth2/userinfo"; // oauth 获取用户信息接口
        logger.info("=====jsonObject:"+jsonObject);
        String userResult = HttpClientUtil.doPost(getUserInfoUrl, jsonObject);
        logger.info("=====userResult:"+userResult);
        String userid = (String) JSONObject.parseObject(userResult).get("userId");
        setUserInfo(request, JSONObject.parseObject(userResult));
        setDeptInfo(request, userid);
        // 根据系统ID和用户ID获取其所拥有的业务角色
        String flowRolesResultUrl = uiasUrl + "/resource/getFlowRolesById?uid=" + userid + "&subId=" + request.getParameter("subId");
        logger.info("=====flowRolesResultUrl:"+flowRolesResultUrl);
        String flowRoles = HttpClientUtil.doGet(flowRolesResultUrl, "UTF-8");
        logger.info("=====flowRoles:"+flowRoles);
        JSONObject json = JSONObject.parseObject(flowRoles);
        if (json.size() > 0) {
            if(!json.get("status").equals("0")){
                request.setAttribute("rolesInfo", json.get("rolesInfo"));
            }
        }
        // 根据系统ID和用户ID获取其所拥有的系统角色
        String sysRolesResultUrl = uiasUrl + "/resource/getRolesById?userid=" + userid + "&subId=" + request.getParameter("subId");
        logger.info("=====sysRolesResultUrl:"+sysRolesResultUrl);
        String sysRoles = HttpClientUtil.doGet(sysRolesResultUrl, "UTF-8");
        logger.info("=====sysRoles:"+sysRoles);
        JSONObject sjson = JSONObject.parseObject(sysRoles);
        if (sjson.size() > 0) {
            if(!json.get("status").equals("0")){
                request.setAttribute("sysRoles", sjson.get("SysRoleSystvo"));
            }
        }
        return "index";
    }
    /**
     * 用户信息写入session
     *
     * @param request 请求
     * @param user 用户json
     */
    private void setUserInfo(HttpServletRequest request, JSONObject user) {
        HttpSession session = request.getSession();
        String userid = (String) user.get("userId");
        String username = (String) user.get("username");
        String status = (String) user.get("status");
        String user_sex = (String) user.get("user_sex");
        String usernamefull = (String) user.get("usernamefull");
        String phone = (String) user.get("phone");
        String user_email = (String) user.get("user_email");
        String caGCode = (String) user.get("caGCode");
        UserInfo info = new UserInfo();
        info.setUid(userid);
        info.setUserName(username);
        info.setStatus(status);
        info.setUserGender(user_sex);
        info.setUserFullName(usernamefull);
        info.setPhone(phone);
        info.setEmail(user_email);
        info.setCaGCode(caGCode);
        session.setAttribute("userInfo", info);
        session.setAttribute("userId", userid);
        session.setAttribute("userName", username);
        session.setAttribute("userFullName", usernamefull);
    }

    /**
     * 部门信息写入session
     *
     * @param request 请求
     * @param userId 用户ID
     */
    private void setDeptInfo(HttpServletRequest request, String userId) {
        String url = "http://172.16.2.34:18001/uias/dept/getAllDeptByUserId?userId=" + userId;
        String result = HttpClientUtil.doGet(url, "UTF-8");
        JSONObject json = JSONObject.parseObject(result);
        if (json.size() > 0) {
            JSONArray deptInfo = (JSONArray) json.get("deptInfo");
            if (deptInfo.size() > 0) {
                JSONObject deptObj = (JSONObject) deptInfo.get(0);
                String deptid = (String) deptObj.get("deptid");
                String treeId = (String) deptObj.get("treeId");
                String deptname = (String) deptObj.get("deptname");
                String abbreviation = (String) deptObj.get("abbreviation");
                String superId = (String) deptObj.get("superId");
                int orderNo = (Integer) deptObj.get("orderNo");
                String deptNumber = (String) deptObj.get("deptNumber");
                String peak_deptId = (String) deptObj.get("peak_deptId");
                String historyId = (String) deptObj.get("historyId");
                String historycode = (String) deptObj.get("historycode");
                String deptPhone = (String) deptObj.get("deptPhone");
                String status = (String) deptObj.get("status");
                String deptlevel = (String) deptObj.get("deptlevel");
                String dptmMail = (String) deptObj.get("dptmMail");
                String dptmManager = (String) deptObj.get("dptmManager");
                String fromUnit = (String) deptObj.get("fromUnit");
                String dptmAdmin = (String) deptObj.get("dptmAdmin");
                String note = (String) deptObj.get("note");
                String unitType = (String) deptObj.get("unitType");
                HttpSession session = request.getSession();
                DeptInfo info = new DeptInfo();
                info.setDeptid(deptid);
                info.setTreeId(treeId);
                info.setDeptname(deptname);
                info.setAbbreviation(abbreviation);
                info.setSuperId(superId);
                info.setOrderNo(orderNo);
                info.setDeptnumber(deptNumber);
                info.setPeakDeptid(peak_deptId);
                info.setHistoryId(historyId);
                info.setHistorycode(historycode);
                info.setDeptPhone(deptPhone);
                info.setStatus(status);
                info.setDeptlevel(deptlevel);
                info.setDptmMail(dptmMail);
                info.setDptmManager(dptmManager);
                info.setFromUnit(fromUnit);
                info.setDptmAdmin(dptmAdmin);
                info.setNote(note);
                info.setUnitType(unitType);
                session.setAttribute("deptInfo", info);
                session.setAttribute("deptId", deptid);
                session.setAttribute("deptName", deptname);
            }
        }
    }

    @ResponseBody
    @RequestMapping("/logout")
    public String logOut(HttpServletRequest request){
        Enumeration<?> e = request.getSession().getAttributeNames();
        while (e.hasMoreElements())
        {
            Object o = e.nextElement();
            request.getSession().removeAttribute(o.toString());
        }
        return "logout";
    }
}
