package com.wisdom.acm.sys.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.JlSectionAddForm;
import com.wisdom.acm.sys.form.ProjectTeamAddForm;
import com.wisdom.acm.sys.form.ProjectTeamUpdateForm;
import com.wisdom.acm.sys.po.OrgSectionRelation;
import com.wisdom.acm.sys.vo.ProjectTeamUserVo;
import com.wisdom.acm.sys.vo.ProjectTeamVo;
import com.wisdom.acm.sys.vo.SysRoleVo;

import java.util.List;
import java.util.Map;

public interface ProjectTeamService {

    /**
     * 项目团队（不含标段）
     * @param projectId
     * @return
     */
    List<ProjectTeamVo> queryProjectTeam(int projectId);

    /**
     * 删除业务数据时删除项目团队
     * @param bizType 业务数据类型
     * @param bizId 业务ID
     * @return
     */
    List<Integer> deleteByBiz(String bizType, Integer bizId);

    int add(ProjectTeamAddForm form);

    int update(ProjectTeamUpdateForm form);

    void delete(List<Integer> ids);

    ProjectTeamVo get(Integer id);

    List<ProjectTeamVo> queryProjectTeamList(String bizType, Integer bizId);

    List<ProjectTeamVo> queryProjectTeamTree(String bizType, Integer bizId);

    /**
     *  查询团队下的用户信息
     * @param teamId
     * @return
     */
    List<ProjectTeamUserVo> queryUserListByTeamId(Integer teamId);

    /**
     * 为项目团队分配用户
     * @param teamId
     * @param users
     */
    void assignUser(Integer teamId, List<Map<String,Object>> users);

    /**
     * 修改团队下的用户的角色
     * @param teamId
     * @param userId
     * @param roleIds
     */
    void updateUserRole(Integer teamId, Integer userId, List<Integer> roleIds);

    /**
     * 导入团队
     * @param dataSource
     * @param bizType
     * @param bizId
     * @param parentId
     * @param data
     */
    void importProjectTeam(String dataSource,String bizType, Integer bizId,Integer parentId,Map<String,Object> data );

    /**
     * 复制项目团队
     * @param sourceBizType
     * @param sourceBizId
     * @param targetBizType
     * @param targetBizId
     * @param teamCanEmpty false:抛出异常
     */
    void copyProjectTeam(String sourceBizType, Integer sourceBizId, String targetBizType, Integer targetBizId,boolean teamCanEmpty);

    /**
     * 删除项目团队下用户
     * @param ids
     */
    void deleteUsers(List<Integer> ids);

    String queryLoggers(Integer teamId, List<Map<String, Object>> data);

    String queryTeamUserlogger(List<Integer> ids);

    /**
     * 项目团队排序
     * @return
     */
    List<ProjectTeamVo> updateProjectTeamSortByIdAndUpOrDown(Integer id,String upOrDown);

    /**
     * 团队成员排序
     * @param id
     * @param teamId
     * @param upOrDown
     * @return
     */
    List<ProjectTeamUserVo> updateProjectTeamUserSortByIdAndUpdateOrDown(Integer teamId,Integer id,String upOrDown);

    /**
     * 查询项目下 用户角色
     * @param projectId
     * @param userId
     * @return
     */
    List<SysRoleVo> queryRoleListByProjectIdAndUserId(Integer projectId, Integer userId);

    /**
     * 查询施工标下的监理标
     * @param sectionId
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    PageInfo<ProjectTeamVo> selectJlSectionList(Integer sectionId, Integer pageSize, Integer currentPageNum);

    /**
     * 查询需要分配的监理标
     * @param sectionId
     * @return
     */
    List<ProjectTeamVo> selectXyJlSectionList(Integer sectionId);

    /**
     * 批量增加监理标
     * @param jlSectionAddForms
     */
    void addJlSection(List<JlSectionAddForm> jlSectionAddForms);

    /**
     * 批量删除监理标
     * @param jlSectionAddForms
     */
    void deleteJlSection(List<JlSectionAddForm> jlSectionAddForms);

    void updateOrgSectionRelation(int sectionId, OrgSectionRelation relation);
    OrgSectionRelation getOrgSectionRelation(int sectionId);
}
