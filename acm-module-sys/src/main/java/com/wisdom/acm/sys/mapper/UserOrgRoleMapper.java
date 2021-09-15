package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.po.SysUserOrgRolePo;
import com.wisdom.acm.sys.vo.SysRoleVo;
import com.wisdom.acm.sys.vo.SysUserOrgRoleVo;
import com.wisdom.acm.sys.vo.SysUserVo;
import com.wisdom.base.common.mapper.CommMapper;
import com.wisdom.base.common.vo.RoleUserVo;
import com.wisdom.base.common.vo.UserRoleVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface UserOrgRoleMapper extends CommMapper<SysUserOrgRolePo> {

    @Select("select * from wsd_sys_userorg_role where org_id = #{orgId} and user_id = #{userId} ")
    List<SysUserOrgRolePo> selectListByOrgIdAndUserId(@Param("orgId") Integer orgId,@Param("userId") Integer userId);

    void deleteUserOrgRoleRelationByOrgIds(@Param("orgIds") List<Integer> orgIds);

    void deleteUserOrgRoleRelationByUserIds(@Param("userIds") List<Integer> userIds);

    void deleteUserOrgRoleRelationByRoleIds(@Param("roleIds") List<Integer> roleIds);

    List<SysUserOrgRoleVo> selectUserRoleByUserIds(@Param("idsMap") Map<String,Object> idsMap);

    List<SysUserOrgRoleVo> selectUserRoleByOrgIdAndUserIds(@Param("userIds") List<Integer> ids,@Param("orgId") Integer orgId);

    List<SysUserOrgRoleVo> selectUserRoleByUserIdAndOrgId(@Param("userIds") List<Integer> ids,@Param("orgId") Integer orgId);

    void deleteUserOrgRoleRelationByUserIdAndOrgId(@Param("userIds") List<Integer> userIds,@Param("orgId") Integer orgId);

    List<SysRoleVo> selectRolesByOrgIdAndUserId(@Param("orgId") Integer id,@Param("userId") Integer userId);

    List<UserRoleVo> selectRoleVoMapByUserIds(@Param("userIds") List<Integer> userIds);

    List<RoleUserVo> selectUserVoMapByRoleIds(@Param("roleIds") List<Integer> roleIds);

    List<SysUserVo> selectUserByRoleId(@Param("roleId") Integer roleId);

    List<SysUserOrgRoleVo> selectUserRoleByUserIdAndRoleId(@Param("userIds") List<Integer> ids,@Param("roleId") Integer roleId);
}
