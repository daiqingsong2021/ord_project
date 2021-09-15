package com.wisdom.acm.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.SysOrgUserSearchForm;
import com.wisdom.acm.sys.mapper.UserOrgMapper;
import com.wisdom.acm.sys.po.SysUserOrgPo;
import com.wisdom.acm.sys.service.SysUserOrgRoleService;
import com.wisdom.acm.sys.service.SysUserOrgService;
import com.wisdom.acm.sys.service.SysUserService;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.sys.UserOrgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysUserOrgServiceImpl extends BaseService<UserOrgMapper, SysUserOrgPo> implements SysUserOrgService {

    @Autowired
    private SysUserOrgRoleService sysUserOrgRoleService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 查询外部用户同一公司/部门下的用户 for activiti
     * @param id
     * @return
     */
    @Override
    public List<GeneralVo> queryTeamUsersOutUser(Integer id) {
        return mapper.queryTeamUsersOutUser(id);
    }

    /**
     * 查找用户所在部门/组织 for activiti
     * @param userId
     * @return
     */
    @Override
    public List<GeneralVo> selectUserMainOrg(String userId) {
        return mapper.selectUserMainOrg(userId);
    }

    @Override
    public List<UserOrgVo> queryListByUserId(String bizType, Integer userId){
        return this.mapper.selectListByUserId(bizType, userId);
    }

    /**
     * 保存用户部门信息
     *
     * @param sysUserOrgPo
     */
    @Override
    public void addOrgUserRelation(SysUserOrgPo sysUserOrgPo) {
        this.insert(sysUserOrgPo);
    }

    @Override
    public List<SysOrgUserVo> queryListByOrgId(Integer orgId) {
        return mapper.selectListByOrgId(orgId);
    }

    @Override
    public List<SysOrgUserVo> queryListByOrgIds(List<Integer> orgIds) {
        return mapper.selectListByOrgIds(orgIds);
    }

    @Override
    public void insert(Integer orgId, Integer userId) {
        SysUserInfoVo userVo = this.sysUserService.getUserInfo(userId);
        SysUserOrgPo userOrgPo = new SysUserOrgPo();
        userOrgPo.setOrgId(orgId);
        userOrgPo.setUserId(userId);
        userOrgPo.setPosition(userVo.getPosition());
        userOrgPo.setProfessional(userVo.getProfessional());
        userOrgPo.setSort(mapper.selectUserOrgNextSort(orgId));
        this.insert(userOrgPo);
    }

    @Override
    public List<Integer> queryOrgIdsByUserId(Integer userId) {

        Example example = new Example(SysUserOrgPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        List<SysUserOrgPo> userOrgs = this.mapper.selectByExample(example);
        return ListUtil.toValueList(userOrgs, "orgId", Integer.class, true);
    }

    /**
     * 删除用户部门关系（根据部门id）
     *
     * @param orgIds
     */
    @Override
    public void deleteUserOrgRelationByOrgIds(List<Integer> orgIds) {
        if (!ObjectUtils.isEmpty(orgIds))
            mapper.deleteByOrgId(orgIds);
    }

    /**
     * 删除用户部门关系（根据用户id）
     *
     * @param userIds
     */
    @Override
    public void deleteUserOrgRelationByUserIds(List<Integer> userIds) {
        if (!ObjectUtils.isEmpty(userIds))
            mapper.deleteUserOrgRelationByUserIds(userIds);
    }

    @Override
    public List<SysOrgUserTreeVo> queryOrgUsers() {
        List<SysOrgUserTreeVo> list = mapper.selectOrgUsers();
        return list;
    }

    /**
     * 搜索组织下用户
     *
     * @param sysOrgUserSearchForm
     * @param pageSize
     * @param currentPageNum
     * @param orgId
     * @return
     */
    @Override
    public PageInfo<SysUserVo> queryUsersByOrgId(SysOrgUserSearchForm sysOrgUserSearchForm, Integer pageSize, Integer currentPageNum, Integer orgId) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<SysUserVo> sysUserVoList = mapper.selectUserByOrgId(sysOrgUserSearchForm, orgId);
        PageInfo<SysUserVo> pageInfo = new PageInfo<>(sysUserVoList);

        List<SysUserVo> userList = pageInfo.getList();
        if (!ObjectUtils.isEmpty(userList)) {
            List<Integer> ids = new ArrayList<>();
            for (SysUserVo sysOrgSelectUserVo : userList) {
                ids.add(sysOrgSelectUserVo.getId());
            }

            //获取用户角色
            List<SysUserOrgRoleVo> roles = sysUserOrgRoleService.queryUserRoleByUserIdAndOrgId(ids,orgId);
            Map<Integer, List<SysUserOrgRoleVo>> roleMap = ListUtil.bulidTreeListMap(roles, "userId", Integer.class);
            SysRoleVo sysRoleVo1 = null;
            List<SysRoleVo> roleVos1 = null;
            //添加用户角色信息
            for (SysUserVo sysUserVo : userList) {
                roleVos1 = new ArrayList<>();
                if (!ObjectUtils.isEmpty(roleMap.get(sysUserVo.getId()))) {
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

    @Override
    public void deleteUserOrgRelationByUserIdAndOrgId(List<Integer> userIds, Integer orgId) {
        mapper.deleteUserOrgRelationByUserIdsAndOrgId(userIds,orgId);
    }



    @Override
    public void updateSysUserOrgPoByIdAndUpOrDown(Integer id,Integer orgId, String upOrDown) {
        if(!ObjectUtils.isEmpty(id)){
            SysUserOrgPo userOrgPo = this.selectById(id);

            Integer sortNum = userOrgPo.getSort();

            if(!ObjectUtils.isEmpty(sortNum)){
                if("up".equals(upOrDown)){
                    SysUserOrgPo upUserOrgPo = this.selectUserOrgPoByOrgIdAndSort(orgId,sortNum-1);
                    if(upUserOrgPo != null){
                        upUserOrgPo.setSort(sortNum);
                        this.updateById(upUserOrgPo);

                        userOrgPo.setSort(sortNum-1);
                        this.updateById(userOrgPo);
                    }
                }else if("down".equals(upOrDown)){
                    SysUserOrgPo downUserOrgPo = this.selectUserOrgPoByOrgIdAndSort(orgId,sortNum+1);
                    if(downUserOrgPo != null){
                        downUserOrgPo.setSort(sortNum);
                        this.updateById(downUserOrgPo);

                        userOrgPo.setSort(sortNum+1);
                        this.updateById(userOrgPo);
                    }
                }
            }
        }
    }

    private SysUserOrgPo selectUserOrgPoByOrgIdAndSort(Integer orgId,Integer sort){
        if(!ObjectUtils.isEmpty(orgId) && !ObjectUtils.isEmpty(sort)){
            Example example = new Example(SysUserOrgPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId",orgId);
            criteria.andEqualTo("sort",sort);
            return this.selectOneByExample(example);
        }
        return  null;
    }
}
