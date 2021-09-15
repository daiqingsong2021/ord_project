package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.po.SysRoleAuthPo;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.mapper.CommMapper;
import com.wisdom.base.common.vo.GeneralVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AuthMapper extends CommMapper<SysRoleAuthPo> {

    /**
     * 根据角色和菜单CODE获取权限集合
     * @param menuCode
     * @param roleIds
     * @return
     */
    List<GeneralVo> selectAuths(@Param("menuCode")String menuCode, @Param("roleIds")List<Integer> roleIds);

    /**
     * 根据角色id获取权限配置
     * @param roleId
     * @return
     */
    List<SysRoleAuthPo> selectAuthByRoleId(@Param("roleId")Integer roleId);

    /**
     * 删除权限关系
     */
    void delefuncAuth(@Param("delAuths") List<SysRoleAuthUpdateVo> delAuths);

    /**
     *保存权限
     */
    void insertRoleAuth( @Param("addRoleAuths") List<SysRoleAuthUpdateVo> sysRoleAuthPos,@Param("roleId") Integer roleId);

    List<SysRoleAuthPo> selectRoleAuthByUserId(@Param("userId") Integer userId);


    /**
     * 获取用户菜单权限
     * @param userId
     * @return
     */
    List<String> selectAuthByUserId(@Param("userId") Integer userId);

    /**
     * 根据权限代码，用户ID查询拥有权限的所在OrgId集合
     *
     * @param funcCode
     * @param userId
     * @return
     */
    List<Integer> selectOrgIdsByUserIdAndFuncCode(@Param("funcCode") String funcCode, @Param("userId") Integer userId);

    void deleteRoleAuthByRoleIds(@Param("roleIds") List<Integer> roleIds);

    void deleteRoleAuthByMenuCodes(@Param("resCodes") List<String> resCodes,@Param("resType") String resType);

    List<String> selectAuthBaseMenuByUserId(@Param("userId") Integer userId);

    /**
     * 根据roleAuthId 删除wsd_sys_roleauth
     * @param idsMap
     */
    void deleteByRoleIds(@Param("idsMap") Map<String, Object> idsMap);
}
