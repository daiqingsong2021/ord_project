package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.po.SysUserIptRolePo;
import com.wisdom.acm.sys.vo.SysUserIptRoleVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserIptRoleMapper extends CommMapper<SysUserIptRolePo> {
    /**
     * 获取iptuserrole关系
     * @param iptIds
     * @return
     */
    List<SysUserIptRolePo> queryUserIptRoleRelationByIptIds(@Param("iptIds") List<Integer> iptIds);


    /**
     * s删除userIptRole关系
     * @param iptIds
     */
    void deleteIptUserRoleRelationByIpts(@Param("iptIds") List<Integer> iptIds);

    void selectUserIptRoleRelationByRoleIds(@Param("roleIds") List<Integer> roleIds);

    SysUserIptRolePo selectUserIptRoleExistRelation(@Param("roleId") Integer roleId,@Param("userId") Integer userId,@Param("iptId") Integer iptId);

    List<SysUserIptRoleVo> queryUserIptRoleRelationByUserIds(@Param("userIds") List<Integer> userIds,@Param("iptId") Integer iptId);

    void deleteIptUserRoleByUserIdAndIptId(@Param("userIds") List<Integer> userIds,@Param("iptId") Integer iptId);

    void deleteUserIptRelationByUserIds(@Param("userIds") List<Integer> userIds);
}
