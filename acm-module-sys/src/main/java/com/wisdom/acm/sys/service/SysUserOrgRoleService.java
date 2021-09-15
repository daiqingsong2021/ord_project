package com.wisdom.acm.sys.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.po.SysUserOrgRolePo;
import com.wisdom.acm.sys.vo.SysRoleVo;
import com.wisdom.acm.sys.vo.SysUserOrgRoleVo;
import com.wisdom.acm.sys.vo.SysUserVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.RoleUserVo;
import com.wisdom.base.common.vo.UserRoleVo;

import java.util.List;
import java.util.Map;

public interface SysUserOrgRoleService extends CommService<SysUserOrgRolePo> {

    /**
     * 增加用户部门角色的关系
     * @param sysUserOrgRolePo
     */
    void addUserOrgRoleRelation(SysUserOrgRolePo sysUserOrgRolePo);

    List<SysUserOrgRolePo> queryListByOrgIdAndUserId(Integer orgId, Integer userId);

    List<SysUserOrgRolePo> queryListByOrgIds(List<Integer> orgId);

    void insert(SysUserOrgRolePo userOrgRolePo);

    void insert(Integer orgId, Integer userId, Integer roleId);

    void delete(SysUserOrgRolePo entity);

    /**
     * 删除用户部门角色关系(根据部门id)
     * @param orgIds
     */
    void deleteUserOrgRoleRelationByOrgIds(List<Integer> orgIds);

    /**
     * 删除用户用户部门关系（根据用户id）
     * @param userIds
     */
    void deleteUserOrgRoleRelationByUserIds(List<Integer> userIds);

    /**
     * 删除用户部门角色关系（根据角色id）
     * @param roleIds
     */
    void deleteUserOrgRoleRelationByRoleIds(List<Integer> roleIds);

    /**
     * 获取用户角色
     * @param ids
     * @return
     */
    List<SysUserOrgRoleVo> queryUserRoleByUserId(List<Integer> ids);

    List<SysUserOrgRoleVo> queryUserRoleByUserIdAndOrgId(List<Integer> ids, Integer orgId);

    void deleteUserOrgRoleRelationByUserIdAndOrgId(List<Integer> userIds, Integer orgId);

    List<Integer> queryRoleIdByUserId(Integer id);

    List<SysRoleVo> queryRoleListByOrgIdAndUserId(Integer id, Integer userId);

    /**
     * 根据用户ID查找用户
     *
     * @param userIds
     * @return
     */
    Map<Integer, UserRoleVo> queryRoleVoMapByUserIds(List<Integer> userIds);


    /**
     * 根据角色ID查找用户角色
     *
     * @param roleIds
     * @return
     */
    Map<Integer, RoleUserVo> queryUserVoMapByRoleIds(List<Integer> roleIds);

    /**
     * 根据用户ID查询所有的组织角色
     *
     * @param userId
     * @return
     */
    List<SysUserOrgRolePo> queryUserOrgRolePosByUserId(Integer userId);

     /**
      *   根据角色id获取用户列表
     * @param roleId
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    PageInfo<SysUserVo> queryUsersByRoleId(Integer roleId, Integer pageSize, Integer currentPageNum);

    /**
     * 分配角色下用户
     * @param vos
     */
    void addUserOrgRole(List<SysUserOrgRoleVo> vos);

    /**
     * 删除角色用户
     * @param vos
     */
    void deleteUserRole(List<Integer> vos);

    String queryUserRoleDeleteLogger(List<Integer> ids);

    List<SysUserOrgRolePo> querySysUserOrgRoleByRoleIds(List<Integer> roleIds);
}
