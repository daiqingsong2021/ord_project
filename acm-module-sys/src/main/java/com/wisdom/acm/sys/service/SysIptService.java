package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.form.SysIptAddForm;
import com.wisdom.acm.sys.form.SysIptUpdateForm;
import com.wisdom.acm.sys.form.SysSearchIptForm;
import com.wisdom.acm.sys.po.SysIptPo;
import com.wisdom.acm.sys.po.SysUserIptPo;
import com.wisdom.acm.sys.po.SysUserIptRolePo;
import com.wisdom.acm.sys.vo.IptImportAddVo;
import com.wisdom.acm.sys.vo.SysIptInfoVo;
import com.wisdom.acm.sys.form.SysUserIptAddForm;
import com.wisdom.acm.sys.vo.SysIptVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface SysIptService extends CommService<SysIptPo> {

    /**
     * 获取ipt信息
     * @param iptId
     * @return
     */
    SysIptInfoVo getIptInfo(Integer iptId);

    SysIptPo  addIpt(SysIptAddForm ipt);

    SysIptPo  updateIpt(SysIptUpdateForm iptUpdate);

    void deleteIpt(List<Integer> iptId);

    /**
     * 搜索ipt
     * @param searchMap
     * @return
     */
    List<SysIptVo> queryIptsBySearch(SysSearchIptForm searchMap);

    boolean importIptFromOrg(int iptId, IptImportAddVo iptImportAddVo, String hostIp, String userName);

    /**
     * ipt分配用户
     * @param sysUserIptAddForm
     * @param iptId
     */
    void addIptUser(List<SysUserIptAddForm> sysUserIptAddForm, Integer iptId);

    /**
     * 获取ipt列表
     * @return
     */
    List<SysIptVo> queryIptTree();

    /**
     * 获取多个ipt信息
     * @param iptIds
     * @return
     */
    List<SysIptInfoVo> queryIptByIds(List<Integer> iptIds);

    /**
     * 获取userIpt关系
     * @param iptIds
     * @return
     */
    List<SysUserIptPo> queryUserIptRelationByIptIds(List<Integer> iptIds);

    /**
     * 获取useriptrole关系
     * @param iptIds
     * @return
     */
    List<SysUserIptRolePo> queryUserIptRoleRelationByiptIds(List<Integer> iptIds);

    void deleteIptUser(List<Integer> userIds, Integer iptId);

    void addIptUserRole(SysUserIptAddForm sysUserIptAddForm, Integer iptId);

    String queryAssignIptUserLogger(List<SysUserIptAddForm> list, Integer iptId);

    String queryDeleteIptUserLogger(List<Integer> userIds, Integer iptId);
}
