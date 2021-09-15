package com.wisdom.acm.sys.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.SysOrgUserSearchForm;
import com.wisdom.acm.sys.po.SysUserOrgPo;
import com.wisdom.acm.sys.vo.SysOrgSelectUserVo;
import com.wisdom.acm.sys.vo.SysOrgUserTreeVo;
import com.wisdom.acm.sys.vo.SysOrgUserVo;
import com.wisdom.acm.sys.vo.SysUserVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.sys.UserOrgVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserOrgService extends CommService<SysUserOrgPo> {

    /**
     * 查询外部用户同一公司/部门下的用户 for activiti
     * @param id
     * @return
     */
    List<GeneralVo> queryTeamUsersOutUser(Integer id);

    /**
     * 查找用户所在部门/组织 for activiti
     * @param userId
     * @return
     */
    List<GeneralVo> selectUserMainOrg(String userId);

    /**
     * 获取用户部门信息
     * @param bizType null:全局，ipt:ipt，project:项目团队
     * @param userId 用户ID
     * @return
     */
    List<UserOrgVo> queryListByUserId(String bizType, Integer userId);

    /**
     * 保存用户部门信息
     * @param sysUserOrgPo
     */
    void addOrgUserRelation(SysUserOrgPo sysUserOrgPo);

    List<SysOrgUserVo> queryListByOrgId(Integer orgId);

    List<SysOrgUserVo> queryListByOrgIds(List<Integer> orgIds);

    void insert(SysUserOrgPo entity);

    void insert(Integer orgId, Integer userId);

    /**
     * 根据用户ID查询所有的orgIds集合
     *
     * @param userId
     * @return
     */
    List<Integer> queryOrgIdsByUserId(Integer userId);

    /**
     * 删除用户部门关系（根据部门id）
     * @param orgIds
     */
    void deleteUserOrgRelationByOrgIds(List<Integer> orgIds);

    /**
     * 删除用户部门关系（根据用户id）
     * @param userIds
     */
    void deleteUserOrgRelationByUserIds(List<Integer> userIds);

    List<SysOrgUserTreeVo> queryOrgUsers();

    /**
     * 搜索组织下用户
     * @param sysOrgUserSearchForm
     * @param pageSize
     * @param currentPageNum
     * @param orgId
     * @return
     */
    PageInfo<SysUserVo> queryUsersByOrgId(SysOrgUserSearchForm sysOrgUserSearchForm, Integer pageSize, Integer currentPageNum, Integer orgId);

    void deleteUserOrgRelationByUserIdAndOrgId(List<Integer> userIds, Integer orgId);

    void updateSysUserOrgPoByIdAndUpOrDown(Integer id,Integer orgId,String upOrDown);
}
