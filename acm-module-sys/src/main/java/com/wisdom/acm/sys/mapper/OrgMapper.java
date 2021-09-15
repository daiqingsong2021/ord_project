package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.form.JlSectionAddForm;
import com.wisdom.acm.sys.form.SysSearchOrgForm;
import com.wisdom.acm.sys.po.OrgSectionRelation;
import com.wisdom.acm.sys.po.SectionRelationPo;
import com.wisdom.acm.sys.po.SysOrgPo;
import com.wisdom.acm.sys.vo.*;

import com.wisdom.base.common.mapper.CommMapper;
import com.wisdom.base.common.vo.OrgVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrgMapper extends CommMapper<SysOrgPo> {

    List<Integer> selectOrgsBySearch(@Param("search") SysSearchOrgForm search);

    /**
     * 获取所有子节点（树形）
     * @param orgId
     * @return
     */
    List<SysOrgVo> selectOrgsAllByPid(@Param("orgId") Integer orgId);

    /**
     * 查询多个id
     * @param orgFalse
     * @return
     */
    List<SysOrgVo> selectOrgsByIds(@Param("orgList") List<IptOrgSelectVo> orgFalse);

    List<SysOrgUserVo> selectRolationsByOrgIds(@Param("orgIds") List<Integer> orgIdList);

    /**
     * 获取组织信息
     * @param userId
     * @return
     */
    List<SysOrgInfoVo> selectOrgsByUserId(Integer userId);


    /**
     * 获取主要组织信息
     * @param userId
     * @return
     */
    List<SysOrgInfoVo> selectMainOrgByUserId(Integer userId);

    /**
     * 获取组织信息
     * @param orgId
     * @return
     */
    SysOrgInfoVo selectOrgInfoById(Integer orgId);


    /**
     * 获取组织列表
     * @param orgIds
     * @return
     */
    List<SysOrgInfoVo> selectOrgInfoByIds(@Param("orgIds") List<Integer> orgIds);

    /**
     * 查询所有项目团队
     * @param projectId 项目ID
     * @param type org/section
     * @return
     */
    List<ProjectTeamVo> selectProjectTeam(@Param("projectId") int projectId, @Param("type") String type);

    /**
     * 查询所有扩展字段为 标段的数据
     * @return
     */
    List<ProjectTeamVo> selectSections();

    List<ProjectTeamVo> selectProjectTeamList(@Param("bizType") String bizType, @Param("bizId") Integer bizId);

    ProjectTeamVo selectProjectTeamById(@Param("id") Integer id);

    /**
     * 获取多个组织信息
     * @param orgIds
     * @return
     */
    List<SysOrgInfoVo> selectOrgInfosByIds(@Param("orgIds") List<Integer> orgIds);

    int selectProjectTeamNextSort(@Param("bizType") String bizType,@Param("bizId") Integer bizId, @Param("parentId") Integer parentId);

    List<SysOrgVo> selectOrgList();

    List<OrgVo> selectOrgByOrgIds(@Param("orgIds") List<Integer> orgIds);

    List<SysOrgVo> selectOrgByProjectId(@Param("projectId") Integer projectId);

    /**
     * 根据项目ID计算项目是否开启项目团队
     *
     * @param projectId
     * @return
     */
    boolean isUseTeamByProjectById(Integer projectId);

    List<SysOrgUserTreeVo> selectOrgUserTreeVo();

    Integer selectProjectIsExist(@Param("projectId") Integer projectId);

    /**
     * 查找项目团队下用户角色
     * @param projectId
     * @param userId
     * @return
     */
    List<SysRoleVo> queryTeamRoles(@Param("projectId") Integer projectId, @Param("userId") Integer userId);

    /**
     *查询施工标下的监理标
     * @param sectionId
     * @return
     */
    List<ProjectTeamVo> selectJlSectionList(@Param("sectionId") Integer sectionId);

    /**
     * 查询需要分配的监理标
     * @param sectionId
     * @return
     */
    List<ProjectTeamVo> selectXyJlSectionList(@Param("sectionId") Integer sectionId);

    /**
     * 插入施工监理表关系
     * @param sectionRelationPos
     */
    void insertJlSection(@Param("sectionRelationPos") List<SectionRelationPo> sectionRelationPos);

    /**
     * 批量删除施工监理标
     * @param jlSectionAddForms
     */
    void deleteJlSection(@Param("jlSectionAddForms") List<JlSectionAddForm> jlSectionAddForms);
    void deleteOrgSectionRelationBySectionId(int sectionId);
    OrgSectionRelation getOrgSectionRelation(int sectionId);
    int insertOrgSectionRelation(OrgSectionRelation relation);
}
