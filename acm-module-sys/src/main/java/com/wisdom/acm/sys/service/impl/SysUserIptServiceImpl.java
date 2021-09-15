package com.wisdom.acm.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.SysIptUserSearchForm;
import com.wisdom.acm.sys.mapper.UserIptMapper;
import com.wisdom.acm.sys.po.SysUserIptPo;
import com.wisdom.acm.sys.service.SysUserIptRoleService;
import com.wisdom.acm.sys.service.SysUserIptService;
import com.wisdom.acm.sys.vo.SysIptUserVo;
import com.wisdom.acm.sys.vo.SysUserIptRoleVo;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.GeneralVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysUserIptServiceImpl extends BaseService<UserIptMapper, SysUserIptPo> implements SysUserIptService {

    @Autowired
    private SysUserIptRoleService sysUserIptRoleService;

    /**
     * 删除iptuser关系(根据iptId)
     *
     * @param iptIds
     */
    @Override
    public void deleteUserByIptId(List<Integer> iptIds) {
        if (!ObjectUtils.isEmpty(iptIds)) {
            mapper.deleteUserByIptId(iptIds);
        }
    }

    /**
     * 分配ipt用户
     *
     * @param sysUserIptPo
     */
    @Override
    public void addUserIptRelation(SysUserIptPo sysUserIptPo) {
        this.insert(sysUserIptPo);
    }

    /**
     * 获取useript关系
     *
     * @param iptIds
     * @return
     */
    @Override
    public List<SysUserIptPo> queryUserIptRelation(List<Integer> iptIds) {
        List<SysUserIptPo> list = mapper.queryUserIptRelation(iptIds);
        return list;
    }

    /**
     * 验证是否已存在useript关系
     *
     * @param userId
     * @param iptId
     */
    @Override
    public SysUserIptPo validateUserIptRelationExist(Integer userId, Integer iptId) {
        SysUserIptPo sysUserIptPo = mapper.selectUserIptRelationExist(userId, iptId);
        return sysUserIptPo;
    }

    /**
     * 删除iptUser关系（根据用户id）
     *
     * @param userIds
     */
    @Override
    public void deleteUserIptRelationByUserIds(List<Integer> userIds) {
        if (!ObjectUtils.isEmpty(userIds)){
            mapper.deleteUserIptRelationByUserIds(userIds);
        }
    }

    @Override
    public PageInfo<SysIptUserVo> queryUsersByIptId(SysIptUserSearchForm sysIptUserSearchForm, Integer pageSize, Integer currentPageNum, Integer iptId) {
        //用户分页
        PageHelper.startPage(currentPageNum, pageSize);
        List<SysIptUserVo> list = mapper.selectUserByIptId(sysIptUserSearchForm,iptId);
        PageInfo<SysIptUserVo> pageInfo = new PageInfo<>(list);

        List<SysIptUserVo> userIptPos = pageInfo.getList();
        if(!ObjectUtils.isEmpty(userIptPos)){
            //用户id集合
            List<Integer> ids = new ArrayList<>();
            for (SysIptUserVo sysIptUserVo : userIptPos) {
                ids.add(sysIptUserVo.getId());
            }

            //用户角色集合
            List<SysUserIptRoleVo> roles = sysUserIptRoleService.queryUserIptRoleRelationByUserId(ids,iptId);
            Map<Integer,List<SysUserIptRoleVo>> roleMap = ListUtil.bulidTreeListMap(roles,"userId",Integer.class);

            GeneralVo generalVo = null;
            List<GeneralVo> generalVos = null;
            //拼装角色
            for (SysIptUserVo sysIptUserVo : userIptPos) {
                generalVos = new ArrayList<>();
                if (!ObjectUtils.isEmpty(roleMap.get(sysIptUserVo.getId()))){
                    for (SysUserIptRoleVo sysUserIptRoleVo : roleMap.get(sysIptUserVo.getId())){
                        generalVo = new GeneralVo();
                        generalVo.setId(sysUserIptRoleVo.getRoleId());
                        generalVo.setName(sysUserIptRoleVo.getRoleName());
                        generalVos.add(generalVo);
                    }
                    sysIptUserVo.setRole(generalVos);
                }
            }
        }
        return pageInfo;
    }

    @Override
    public void deleteIptUserByUserIdAndIptId(List<Integer> userIds, Integer iptId) {
        if (!ObjectUtils.isEmpty(userIds)){
            mapper.deleteIptUserByUserIdAndIptId(userIds,iptId);
        }
    }
}
