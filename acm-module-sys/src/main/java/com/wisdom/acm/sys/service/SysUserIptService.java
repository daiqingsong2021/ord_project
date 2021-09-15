package com.wisdom.acm.sys.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.SysIptUserSearchForm;
import com.wisdom.acm.sys.po.SysUserIptPo;
import com.wisdom.acm.sys.vo.SysIptUserVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface SysUserIptService extends CommService<SysUserIptPo> {

    /**
     * 删除iptuser关系(根据iptId)
     * @param iptIds
     */
    void deleteUserByIptId(List<Integer> iptIds);

    /**
     * 删除iptUser关系（根据用户id）
     * @param userIds
     */
    void deleteUserIptRelationByUserIds(List<Integer> userIds);

    /**
     * 分配ipt用户
     * @param sysUserIptPo
     */
    void addUserIptRelation(SysUserIptPo sysUserIptPo);

    /**
     * 获取useript关系
     * @param iptIds
     * @return
     */
    List<SysUserIptPo> queryUserIptRelation(List<Integer> iptIds);

    /**
     * 验证是否已存在useript关系
     * @param userId
     * @param iptId
     */
    SysUserIptPo validateUserIptRelationExist(Integer userId, Integer iptId);

    PageInfo<SysIptUserVo> queryUsersByIptId(SysIptUserSearchForm sysIptUserSearchForm, Integer pageSize, Integer currentPageNum, Integer iptId);

    void deleteIptUserByUserIdAndIptId(List<Integer> userIds, Integer iptId);
}
