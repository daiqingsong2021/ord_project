package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.po.SysRoleAuthPo;
import com.wisdom.acm.sys.po.SysRolePo;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.GeneralVo;

import java.util.List;
import java.util.Map;

public interface SysRoleAuthService extends CommService<SysRoleAuthPo> {

    /**
     * 根据角色id获取权限配置
     * @param roleId
     * @return
     */
    List<SysAuthMenuVo> queryAuthAllByRoleId(Integer roleId);

    /**
     * 角色授权
     *
     */
    String updateRoleAuths(Integer roleId,List<SysRoleAuthUpdateVo> auths);

    /**
     * 根据用户查询菜单权限
     * @param userId
     * @return
     */
    List<String> queryAuthByUserId(Integer userId);

    /**
     * 根据权限代码，用户ID查询拥有权限的所在OrgId集合
     *
     * @param funcCode
     * @param userId
     * @return
     */
    List<Integer> queryOrgIdsByUserIdAndFuncCode(String funcCode, Integer userId);

    /**
     * 删除角色权限（根据roleId）
     * @param roleIds
     */
    void deleteRoleAuthByRoles(List<Integer> roleIds);

    /**
     * 删除角色权限（根据Code）
     * @param resCodes
     */
    void deleteRoleAuthByResCode(List<String> resCodes,String resType);

    List<SysRoleAuthPo> queryAuthFuncByRoleIds(List<Integer> roleIds);

    /**
     * 根据用户ID查询用户所在组织的权限集合
     *
     * @param userId
     * @return
     */
    Map<Integer, List<String>> getOrgFuncsByUserId(Integer userId);

    /**
     * 获取用户权限
     * @param menuCode 模板代码
     * @param userId 用户ID
     * @return
     */
    List<GeneralVo> getUserAuth(String menuCode, Integer userId);

}
