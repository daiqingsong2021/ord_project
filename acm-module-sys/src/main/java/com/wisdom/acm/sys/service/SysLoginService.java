package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.vo.SysUserLoginVo;
import com.wisdom.acm.sys.vo.app.AppUserInfoVo;

public interface SysLoginService {
    /**
     * 获取用户登录信息
     * @param userName
     * @return
     */
    public SysUserLoginVo mgetUserLoginInfo(String userName);

    AppUserInfoVo getUserAppLoginInfo(String userName,String token);
}
