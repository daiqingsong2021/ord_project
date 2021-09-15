package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.form.SysSearchOrgForm;
import com.wisdom.acm.sys.po.SysOrgPo;
import com.wisdom.acm.sys.form.SysOrgAddForm;
import com.wisdom.acm.sys.form.SysOrgUpdateForm;
import com.wisdom.acm.sys.vo.SysOrgInfoVo;
import com.wisdom.acm.sys.vo.SysOrgSelectVo;
import com.wisdom.acm.sys.vo.SysOrgUserTreeVo;
import com.wisdom.acm.sys.vo.SysOrgVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.SelectVo;

import java.util.List;
import java.util.Map;

public interface SysOrgService extends CommService<SysOrgPo> {

    List<SysOrgInfoVo> queryOrgPosByProjectId(Integer projectId);

    boolean isUseTeamByProjectId(Integer projectId);

    /**
     * 获取用户的组织信息
     * @param userId
     * @return
     */
    SysOrgInfoVo getUserOrgInfo(Integer userId);

    /**
     * 获取组织信息
     *
     * @param OrgId
     * @return
     */
    SysOrgInfoVo getOrgInfo(Integer OrgId);

    /**
     * 搜索组织列表
     *
     * @param search
     * @return
     */
    List<SysOrgVo> queryOrgsBySearch(SysSearchOrgForm search);

    /**
     * 获取组织列表
     *
     * @return
     */
    List<SysOrgVo> queryOrgTree();

    /**
     * 根据组织id集合获取组织对象
     *
     * @param orgIds
     * @return
     */
    Map<Integer, OrgVo> queryOrgVoByOrgIds(List<Integer> orgIds);

    List<SysOrgInfoVo> queryOrgInfoVoByOrgIds(List<Integer> orgIds);

    SysOrgPo addOrg(SysOrgAddForm org);

    SysOrgPo updateOrg(SysOrgUpdateForm orgUpdate);

    /**
     * 从uuv同步数据  避免TransactionConfig 中切面事务控制
     * @param org
     * @return
     */
    SysOrgPo xinZengOrg(SysOrgAddForm org);

    /**
     * 从uuv同步数据  避免TransactionConfig 中切面事务控制
     * @param orgUpdate
     * @return
     */
    SysOrgPo gengXinOrg(SysOrgUpdateForm orgUpdate);

    void deleteOrg(List<Integer> orgIds);

    /**
     * 获取多个组织信息
     *
     * @param orgIds
     * @return
     */
    List<SysOrgInfoVo> queryOrgsByIds(List<Integer> orgIds);

    /**
     * 根据项目获取组织
     *
     * @param projectId
     * @return
     */
    List<SelectVo> queryOrgSelectVosByProjectId(Integer projectId);


    /**
     * 问题管理 根据项目获取组织
     *
     * @param projectId
     * @return
     */
    List<SelectVo> queryQuesOrgSelectVosByProjectId(Integer projectId);

    /**
     * 查询全局组织机构
     *
     * @return
     */
    List<SelectVo> queryGlobalOrgSelectVos();

    /**
     * 问题管理  查询全局组织机构
     * @return
     */
    List<SelectVo> queryQuesGlobalOrgSelectVos();

    List<SysOrgPo> queryOrgsByBizIdAndBizType(Integer bizId, String bizType);

    /**
     * 获取用户组织树形列表
     *
     * @return
     */
    List<SysOrgUserTreeVo> queryOrgUserTree();

    void deleteOrgUser(List<Integer> userIds, Integer orgId);

    String queryDeleteOrgUserLogger(List<Integer> userIds, Integer orgId);

    List<SelectVo> queryOrgNameSelectVos();

    List<SysOrgSelectVo> queryOrgSelectVo();

    List<SysOrgPo> getOrgPoByCode(String orgCode);

    SysOrgInfoVo querySysUserOrgPoByUserId(Integer userId);
}
