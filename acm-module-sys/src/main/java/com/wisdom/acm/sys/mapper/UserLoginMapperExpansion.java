package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.po.SysUserPo;
import com.wisdom.acm.sys.vo.SysUserLoginVo;
import com.wisdom.base.common.mapper.CommMapper;
import com.wisdom.base.common.vo.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserLoginMapperExpansion extends CommMapper<SysUserPo> {
    public List<SysUserPo> selectMemberByGroupId(@Param("groupId") int groupId);
    public List<SysUserPo> selectLeaderByGroupId(@Param("groupId") int groupId);

    public UserInfo selectUserInfo(@Param("userName") String userName);
    /**
     * 获取用户登录信息
     * @param userName
     * @return
     */
    public SysUserLoginVo selectUserLoginInfo(@Param("userName") String userName);
}