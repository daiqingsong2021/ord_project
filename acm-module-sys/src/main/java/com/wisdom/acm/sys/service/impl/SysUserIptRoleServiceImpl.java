package com.wisdom.acm.sys.service.impl;

import com.wisdom.acm.sys.mapper.UserIptRoleMapper;
import com.wisdom.acm.sys.po.SysUserIptRolePo;
import com.wisdom.acm.sys.service.SysUserIptRoleService;
import com.wisdom.acm.sys.vo.SysUserIptRoleVo;
import com.wisdom.base.common.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SysUserIptRoleServiceImpl extends BaseService<UserIptRoleMapper, SysUserIptRolePo> implements SysUserIptRoleService {

    @Override
    public void addIptUserRoleRelation(SysUserIptRolePo sysUserIptRolePo) {
        this.insert(sysUserIptRolePo);
    }

    /**
     * 获取iptuserrole关系
     * @param iptIds
     * @return
     */
    @Override
    public List<SysUserIptRolePo> queryUserIptRoleRelation(List<Integer> iptIds) {
        List<SysUserIptRolePo> list = mapper.queryUserIptRoleRelationByIptIds(iptIds);
        return list;
    }

    @Override
    public List<SysUserIptRoleVo> queryUserIptRoleRelationByUserId(List<Integer> userIds, Integer iptId) {
        List<SysUserIptRoleVo> list = mapper.queryUserIptRoleRelationByUserIds(userIds,iptId);
        return list;
    }

    /**
     * 根据ipt删除iptUserRole关系
     * @param iptIds
     */
    @Override
    public void deleteUserIptRoleRelation(List<Integer> iptIds) {
        if (!ObjectUtils.isEmpty(iptIds)){
            mapper.deleteIptUserRoleRelationByIpts(iptIds);
        }
    }

    /**
     * 删除iptUserRole关系（根据roleId）
     * @param roleIds
     */
    @Override
    public void deleteUserIptRoleRelationByRoleIds(List<Integer> roleIds) {
       if (ObjectUtils.isEmpty(roleIds)){
           mapper.selectUserIptRoleRelationByRoleIds(roleIds);
       }
    }

    @Override
    public SysUserIptRolePo validateUserIptRoleRelation(Integer roleId, Integer userId, Integer iptId) {
        SysUserIptRolePo sysUserIptRolePo = mapper.selectUserIptRoleExistRelation(roleId,userId,iptId);
        return sysUserIptRolePo;
    }

    @Override
    public void deleteIptUserByUserIdAndIptId(List<Integer> userIds, Integer iptId) {
        if (!ObjectUtils.isEmpty(userIds)){
            mapper.deleteIptUserRoleByUserIdAndIptId(userIds,iptId);
        }
    }

    @Override
    public void deleteUserIptRelationByUserIds(List<Integer> userIds) {
        if (!ObjectUtils.isEmpty(userIds)){
            mapper.deleteUserIptRelationByUserIds(userIds);
        }
    }

    @Override
    public List<SysUserIptRolePo> querySysUserIptRoleByRoleIds(List<Integer> roleIds){
        Example example = new Example(SysUserIptRolePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("roleId",roleIds);
        List<SysUserIptRolePo> list = this.selectByExample(example);
        return list;
    }
}
