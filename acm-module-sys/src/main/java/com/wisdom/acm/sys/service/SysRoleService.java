package com.wisdom.acm.sys.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.SysSearchRoleForm;
import com.wisdom.acm.sys.po.SysRolePo;
import com.wisdom.acm.sys.form.SysRoleAddForm;
import com.wisdom.acm.sys.form.SysRoleUpdateForm;
import com.wisdom.acm.sys.vo.SysRoleVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface SysRoleService extends CommService<SysRolePo> {
    /**
     * 获取角色列表
     * @return
     * @param pageSize
     * @param currentPageNum
     */
    public List<SysRoleVo> queryRoles();

    /**
     * 增加角色
     * @param role
     */
    public SysRolePo addRole( SysRoleAddForm role);

    /**
     * 修改角色
     * @param role
     */
    public SysRolePo updateRole(SysRoleUpdateForm role);

    /**
     * 删除角色
     * @param ids
     */
    public void deleteRole( List<Integer> ids);

    /**
     * 获取角色信息
     * @param roleId
     * @return
     */
    SysRoleVo getRoleInfo(Integer roleId);

    /**
     * 获取多个角色信息
     * @param roleIds
     * @return
     */
    List<SysRoleVo> queryRolesByIds(List<Integer> roleIds);

    /**
     * 搜索角色
     * @param searchMap
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    PageInfo<SysRoleVo> queryRoleBySearch(SysSearchRoleForm searchMap, Integer pageSize, Integer currentPageNum);

    List<String> queryRoleCodesByIds(List<Integer> roleIds);

    String queryRoleNames(List<Integer> ids);

    SysRolePo queryRolePoById(Integer roleId);
}
