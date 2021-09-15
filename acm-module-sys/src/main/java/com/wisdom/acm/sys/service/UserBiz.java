package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.mapper.UserLoginMapperExpansion;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.cache.annotation.Cache;
import com.wisdom.cache.annotation.CacheClear;
import com.wisdom.acm.sys.po.SysUserPo;
import com.wisdom.acm.sys.mapper.MenuMapper;
import com.wisdom.auth.client.jwt.UserAuthUtil;
import com.wisdom.base.common.constant.UserConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 */
@Service
//@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseService<UserLoginMapperExpansion, SysUserPo> {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private UserLoginMapperExpansion userLoginMapper;

    @Override
    public int insertSelective(SysUserPo entity) {
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getPassword());
        entity.setPassword(password);
        int count = super.insertSelective(entity);
        return count ;
    }

    @Override
    @CacheClear(pre="user{1.username}")
    public int updateSelectiveById(SysUserPo entity) {
        super.updateSelectiveById(entity);
        return super.updateSelectiveById(entity);
    }

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Cache(key="user{1}")
    public UserInfo getUserByUsername(String username){
        UserInfo user = new UserInfo();
        user.setUserName(username);
        UserInfo userInfo = userLoginMapper.selectUserInfo(username);
        //user = mapper.selectOne(user);
        return userInfo;
    }


}
