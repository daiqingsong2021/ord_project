package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.po.SysUserIptRolePo;
import com.wisdom.acm.sys.vo.SysUserIptRoleVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface SysUserIptRoleService extends CommService<SysUserIptRolePo> {

    /**
     * 增加ipt用户角色关系
     * @param sysUserIptRolePo
     */
    void addIptUserRoleRelation(SysUserIptRolePo sysUserIptRolePo);

    /**
     * 获取iptuserrole关系
     * @param iptIds
     * @return
     */
    List<SysUserIptRolePo> queryUserIptRoleRelation(List<Integer> iptIds);


    /**
     * 获取iptuserrole关系
     * @param userIds
     * @param iptId
     * @return
     */
    List<SysUserIptRoleVo> queryUserIptRoleRelationByUserId(List<Integer> userIds, Integer iptId);


    /**
     * 根据ipt删除iptUserRole关系
     * @param iptIds
     */
    void deleteUserIptRoleRelation(List<Integer> iptIds);

    /**
     * 删除iptUserRole关系（根据roleId）
     * @param roleIds
     */
    void deleteUserIptRoleRelationByRoleIds(List<Integer> roleIds);

    SysUserIptRolePo validateUserIptRoleRelation(Integer roleId, Integer userId, Integer iptId);

    void deleteIptUserByUserIdAndIptId(List<Integer> userIds, Integer iptId);

    void deleteUserIptRelationByUserIds(List<Integer> userIds);

    List<SysUserIptRolePo> querySysUserIptRoleByRoleIds(List<Integer> roleIds);
}
