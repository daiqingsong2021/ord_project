package com.wisdom.base.common.util;

import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.sys.UserVo;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BaseUtil {
    /**
     * 从请求中获取token
     * @param request
     * @return
     */
    public static String  getLoginToken(HttpServletRequest request){
        Enumeration<String> tokens = request.getHeaderNames();
        Map<String,String> tokenMap = new HashMap<>();;
        while (tokens.hasMoreElements()) {
            String key = (String) tokens.nextElement();
            String value = request.getHeader(key);
            tokenMap.put(key,value);
        }
        String token = tokenMap.get("authorization");
        return  token ;
    }

    /**
     * 得到登录用户
     * @return
     */
    public static UserVo getLoginUser(){
        UserVo user = new UserVo();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = request.getHeader("userId");
        String userName = request.getHeader("userName");
        String actuName = FormatUtil.decode(request.getHeader("actuName"));
        if(!ObjectUtils.isEmpty(userId)) {
            user.setId(Integer.valueOf(userId));
        }
        user.setCode(userName);
        user.setName(Tools.isEmpty(actuName) ? userName : actuName);
        return user;
    }
}
