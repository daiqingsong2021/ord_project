package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.form.SysSearchRoleForm;
import com.wisdom.acm.sys.po.SysRolePo;
import com.wisdom.acm.sys.vo.SysRoleVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends CommMapper<SysRolePo> {

    /**
     * 获取角色信息根据id
     * @param roleId
     * @return
     */
    SysRoleVo selectRoleVoById(@Param("roleId") Integer roleId);

    /**
     * 获取角色列表
     * @return
     */
    List<SysRoleVo> selectRoleAll();

    /**
     * 搜索角色
     * @param searchMap
     * @return
     */
    List<SysRoleVo> selectRoleBySearch(@Param("search") SysSearchRoleForm searchMap);

    /**
     * 获取多个角色信息
     * @param roleIds
     * @return
     */
    List<SysRoleVo> selectRolesByIds(@Param("roleIds") List<Integer> roleIds);
}
