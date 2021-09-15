package com.wisdom.acm.sys.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.sys.mapper.UserOrgRoleMapper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.wisdom.acm.sys.po.SysRolePo;
import com.wisdom.acm.sys.po.SysUserIptRolePo;
import com.wisdom.acm.sys.po.SysUserOrgRolePo;
import com.wisdom.acm.sys.service.SysRoleService;
import com.wisdom.acm.sys.service.SysUserOrgRoleService;
import com.wisdom.acm.sys.service.SysUserService;
import com.wisdom.acm.sys.vo.SysRoleVo;
import com.wisdom.acm.sys.vo.SysUserOrgRoleVo;
import com.wisdom.acm.sys.vo.SysUserVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.RoleUserVo;
import com.wisdom.base.common.vo.UserRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class SysUserOrgRoleServiceImpl extends BaseService<UserOrgRoleMapper, SysUserOrgRolePo> implements SysUserOrgRoleService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 增加用户角色部门关系
     *
     * @param sysUserOrgRolePo
     */
    @Override
    public void addUserOrgRoleRelation(SysUserOrgRolePo sysUserOrgRolePo) {
        this.insert(sysUserOrgRolePo);
    }

    @Override
    public List<SysUserOrgRolePo> queryListByOrgIdAndUserId(Integer orgId, Integer userId) {
        Example example = new Example(SysUserOrgRolePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", orgId);
        criteria.andEqualTo("userId", userId);
        return this.mapper.selectByExample(example);
        //return mapper.selectListByOrgIdAndUserId(orgId, userId);
    }


    @Override
    public List<SysUserOrgRolePo> queryListByOrgIds(List<Integer> orgIds) {
        Example example = new Example(SysUserOrgRolePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("orgId", orgIds);
        List<SysUserOrgRolePo> list = this.mapper.selectByExample(example);
        return list;
    }

    @Override
    public void insert(Integer orgId, Integer userId, Integer roleId) {
        SysUserOrgRolePo userOrgRolePo = new SysUserOrgRolePo();
        userOrgRolePo.setOrgId(orgId);
        userOrgRolePo.setUserId(userId);
        userOrgRolePo.setRoleId(roleId);
        this.insert(userOrgRolePo);
    }

    /**
     * 删除用户部门角色关系(根据部门id)
     *
     * @param orgIds
     */
    @Override
    public void deleteUserOrgRoleRelationByOrgIds(List<Integer> orgIds) {
        if (!ObjectUtils.isEmpty(orgIds))
            mapper.deleteUserOrgRoleRelationByOrgIds(orgIds);
    }

    /**
     * 删除用户用户部门关系（根据用户id）
     *
     * @param userIds
     */
    @Override
    public void deleteUserOrgRoleRelationByUserIds(List<Integer> userIds) {
        if (!ObjectUtils.isEmpty(userIds))
            mapper.deleteUserOrgRoleRelationByUserIds(userIds);
    }

    /**
     * 删除用户部门角色关系（根据角色id）
     *
     * @param roleIds
     */
    @Override
    public void deleteUserOrgRoleRelationByRoleIds(List<Integer> roleIds) {
        if (!ObjectUtils.isEmpty(roleIds)) {
            mapper.deleteUserOrgRoleRelationByRoleIds(roleIds);
        }
    }

    /**
     * 获取用户角色
     *
     * @param ids
     * @return
     */
    @Override
    public List<SysUserOrgRoleVo> queryUserRoleByUserId(List<Integer> ids) {
        if (!ObjectUtils.isEmpty(ids)) {
            Map<String,Object> idsMap= Maps.newHashMap();
            List<Integer> userIds= Lists.newArrayList();
            for(int i=0; i<ids.size(); i++)
            {//0-199 200-399
                if(i%1000==0)
                {
                    userIds=Lists.newArrayList();
                    idsMap.put(String.valueOf(Integer.valueOf(i/1000)),userIds);
                }
                userIds.add(ids.get(i));
            }
            List<SysUserOrgRoleVo> list = mapper.selectUserRoleByUserIds(idsMap);
            return list;
        } else {
            return null;
        }
    }

    @Override
    public List<SysUserOrgRoleVo> queryUserRoleByUserIdAndOrgId(List<Integer> ids, Integer orgId) {
        if (!ObjectUtils.isEmpty(ids)) {
            List<SysUserOrgRoleVo> list = mapper.selectUserRoleByUserIdAndOrgId(ids, orgId);
            return list;
        }
        return null;
    }

    @Override
    public void deleteUserOrgRoleRelationByUserIdAndOrgId(List<Integer> userIds, Integer orgId) {
        mapper.deleteUserOrgRoleRelationByUserIdAndOrgId(userIds, orgId);
    }

    @Override
    public List<Integer> queryRoleIdByUserId(Integer id) {
        Example example = new Example(SysUserOrgRolePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", id);
        List<SysUserOrgRolePo> list = this.mapper.selectByExample(example);
        List<Integer> ids = new ArrayList<>();
        if (!ObjectUtils.isEmpty(list)) {
            list.forEach(sysUserOrgRolePo -> {
                ids.add(sysUserOrgRolePo.getRoleId());
            });
        }
        return ids;
    }

    @Override
    public List<SysRoleVo> queryRoleListByOrgIdAndUserId(Integer id, Integer userId) {
        List<SysRoleVo> list = mapper.selectRolesByOrgIdAndUserId(id, userId);
        return list;
    }


    @Override
    public Map<Integer, UserRoleVo> queryRoleVoMapByUserIds(List<Integer> userIds) {
        List<UserRoleVo> list = mapper.selectRoleVoMapByUserIds(userIds);
        Map<Integer, UserRoleVo> retMap = new LinkedHashMap<Integer, UserRoleVo>();
        if (!ObjectUtils.isEmpty(list)) {
            for (UserRoleVo userRoleVo : list) {
                UserRoleVo urv = retMap.get(userRoleVo.getUser().getId());
                if (ObjectUtils.isEmpty(urv)) {
                    retMap.put(userRoleVo.getUser().getId(), userRoleVo);
                } else {
                    urv.getRole().addAll(userRoleVo.getRole());
                }
            }
        }
        return retMap;
    }

    @Override
    public Map<Integer, RoleUserVo> queryUserVoMapByRoleIds(List<Integer> roleIds) {
        List<RoleUserVo> list = mapper.selectUserVoMapByRoleIds(roleIds);
        Map<Integer, RoleUserVo> retMap = new LinkedHashMap<>();
        for (RoleUserVo roleUserVo : list) {
            RoleUserVo ruv = retMap.get(roleUserVo.getRole().getId());
            if (ObjectUtils.isEmpty(ruv)) {
                retMap.put(roleUserVo.getRole().getId(), roleUserVo);
            } else {
                ruv.getUser().addAll(roleUserVo.getUser());
            }
        }
        return retMap;
    }

    @Override
    // @Cacheable(cacheNames = {"user-org-list"},key = "#userId",condition = "#id>0",unless = "#result==null",sync = false )
    public List<SysUserOrgRolePo> queryUserOrgRolePosByUserId(Integer userId) {

        Example example = new Example(SysUserOrgRolePo.class);
        example.setDistinct(true);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);

        return this.selectByExample(example);
    }


    public PageInfo<SysUserVo> queryUsersByRoleId(Integer roleId, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<SysUserVo> sysUserVoList = mapper.selectUserByRoleId(roleId);
        PageInfo<SysUserVo> pageInfo = new PageInfo<>(sysUserVoList);

        List<SysUserVo> userList = pageInfo.getList();
        if (!ObjectUtils.isEmpty(userList)) {
            List<Integer> ids = new ArrayList<>();
            for (SysUserVo sysUserVo : userList) {
                ids.add(sysUserVo.getId());
            }

            //获取用户角色
            List<SysUserOrgRoleVo> roles = this.queryUserRoleByUserIdAndRoleId(ids, roleId);
            Map<Integer, List<SysUserOrgRoleVo>> roleMap = ListUtil.bulidTreeListMap(roles, "userId", Integer.class);
            SysRoleVo sysRoleVo1 = null;
            List<SysRoleVo> roleVos1 = null;
            //添加用户角色信息
            for (SysUserVo sysUserVo : userList) {
                if (!ObjectUtils.isEmpty(roleMap.get(sysUserVo.getId()))) {
                    roleVos1 = new ArrayList<>();
                    for (SysUserOrgRoleVo sysUserOrgRoleVo : roleMap.get(sysUserVo.getId())) {
                        sysRoleVo1 = new SysRoleVo();
                        sysRoleVo1.setId(sysUserOrgRoleVo.getRoleId());
                        sysRoleVo1.setRoleName(sysUserOrgRoleVo.getRoleName());
                        roleVos1.add(sysRoleVo1);
                    }
                }
                sysUserVo.setRoles(roleVos1);
            }

            List<SysRoleVo> roleVos = null;
            if (!ObjectUtils.isEmpty(userList)) {
                for (SysUserVo sysUserVo : userList) {
                    roleVos = sysUserVo.getRoles();
                    StringBuffer str = new StringBuffer();
                    if (!ObjectUtils.isEmpty(roleVos)) {
                        for (SysRoleVo sysRoleVo : roleVos) {
                            str.append("," + sysRoleVo.getRoleName());
                        }
                        String retRole = str.substring(1);
                        sysUserVo.setRetRole(retRole);
                        sysUserVo.setRoles(null);
                    }
                }
            }
        }
        return pageInfo;
    }

    private List<SysUserOrgRoleVo> queryUserRoleByUserIdAndRoleId(List<Integer> ids, Integer roleId) {
        List<SysUserOrgRoleVo> list = mapper.selectUserRoleByUserIdAndRoleId(ids, roleId);
        return list;
    }

    /**
     * 分配用户角色
     *
     * @param vos
     */
    @Override
    public void addUserOrgRole(List<SysUserOrgRoleVo> vos) {
        if (!ObjectUtils.isEmpty(vos)) {
            //查询该角色下存在的用户
            Example example = new Example(SysUserOrgRolePo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("roleId", vos.get(0).getRoleId());
            List<SysUserOrgRolePo> userOrgRolePos = mapper.selectByExample(example);

            Map<String, SysUserOrgRolePo> rMap = new HashMap<>();
            if (!ObjectUtils.isEmpty(userOrgRolePos)) {
                for (SysUserOrgRolePo po : userOrgRolePos) {
                    if (!ObjectUtils.isEmpty(po.getOrgId()))
                        rMap.put(po.getOrgId().toString() + po.getUserId().toString(), po);
                }
            }

            int i = 0; //记录重复分配个数
            for (SysUserOrgRoleVo userOrgRolePo : vos) {
                if (ObjectUtils.isEmpty(rMap) || ObjectUtils.isEmpty(rMap.get(userOrgRolePo.getOrgId().toString() + userOrgRolePo.getUserId().toString()))) {
                    SysUserOrgRolePo sysUserOrgRolePo = dozerMapper.map(userOrgRolePo, SysUserOrgRolePo.class);
                    this.insert(sysUserOrgRolePo);
                } else {
                    i = i + 1;
                }
            }
            if (i == vos.size()) {
                throw new BaseException("重复分配！");
            }
        }
    }

    @Override
    public void deleteUserRole(List<Integer> vos) {
        if (!ObjectUtils.isEmpty(vos)) {
            super.deleteByIds(vos);
        }
    }

    /**
     * 获取角色删除用户关系的日志内容
     *
     * @param ids
     * @return
     */
    @Override
    public String queryUserRoleDeleteLogger(List<Integer> ids) {
        List<SysUserOrgRolePo> list = super.selectByIds(ids);
        List<Integer> userIds = new ArrayList<>();
        for (SysUserOrgRolePo sysUserOrgRolePo : list) {
            userIds.add(sysUserOrgRolePo.getUserId());
        }
        String userNames = sysUserService.queryUserNamesByIds(userIds);
        SysRolePo sysRolePo = sysRoleService.queryRolePoById(list.get(0).getRoleId());
        String logger = "删除\"" + sysRolePo.getRoleName() + "\"角色用户:" + userNames;
        return logger;
    }

    @Override
    public List<SysUserOrgRolePo> querySysUserOrgRoleByRoleIds(List<Integer> roleIds){
        Example example = new Example(SysUserOrgRolePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("roleId",roleIds);
        List<SysUserOrgRolePo> list = this.selectByExample(example);
        return list;
    }
}
