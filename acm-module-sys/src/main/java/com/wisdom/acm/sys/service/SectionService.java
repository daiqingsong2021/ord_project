package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.po.SysOrgPo;
import com.wisdom.acm.sys.po.SysUserOrgPo;
import com.wisdom.acm.sys.vo.*;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.sys.SectionProjectVo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 标段-逻辑处理
 */
public interface SectionService {



    /**
     * 获取标段项目信息
     * @param userId 用户ID
     * @return
     */
    List<SectionProjectVo> querySectionProjectListByUserId(Integer userId);

    /**
     * 初始化补充标段的施工单位信息和监理单位信息
     * @param sectionVos
     * @param projectId
     */
    void initSectionOrgInfo(List<SectionVo> sectionVos, int projectId);

    /**
     * 查询标段的详细信息
     * @param ids 标段IDS
     * @return
     */
    List<SectionVo> querySectionList(List<Integer> ids);

    /**
     * 获取标段的基本信息（ID，CODE，NAME）
     * @param projectId
     * @param pageSize
     * @param currentPageNum
     * @param key
     * @return
     */
    List<GeneralVo> querySectioinGeneralVoListByProjectId(Integer projectId, Integer pageSize, Integer currentPageNum, String key);

    /**
     * 获取项目标段信息
     * @param projectId
     * @return
     */
    List<SectionVo> querySectioinListByProjectId(Integer projectId);

    /**
     * 当删除项目团队时-告知到SGY
     * @param ids
     * @return
     */
    Integer deleteSzxmProjectTeam(List<Integer> ids);

    /**
     * 当增加项目团队时-推送到SGY
     * @param org
     * @return
     */
    Integer addSzxmProjectTeam(SysOrgPo org);

    /**
     * 当修改项目团队时-推送到SGY
     * @param org
     * @return
     */
    Integer updateSzxmProjectTeam(SysOrgPo org);

    /**
     * 获取用户
     * @param projectId
     * @param userId
     * @return
     */
    List<ProjectTeamUserVo> queryProjectTeamUser(Integer projectId, Integer userId);

    /**
     * 获取业主代表信息
     * @param projectId 项目ID
     * @param orgIds 业主部门IDS
     * @param userId 用户ID
     * @return
     */
    List<ProjectTeamUserVo> queryOwnerList(Integer projectId, List<Integer> orgIds, Integer userId);

    /**
     * 判断是否是业主代表
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return
     */
    boolean checkOwner(Integer projectId, Integer userId);

    /**
     * 获取项目团队树，顶层为项目信息
     * @param projectId
     * @return
     */
    List<ProjectTeamVo> queryProjectTeamTree(Integer projectId);

    /**
     * 获取用户组织信息
     * @param orgId
     * @param userId
     * @return
     */
    SysOrgUserVo getSysOrgUserVo(Integer orgId, Integer userId);

    /**
     * 查询与标段专业相关的业主代表
     * @param id 标段ID
     * @return
     */
    List<ProjectTeamUserVo> getOwnerBySectionId(Integer id);

    /**
     * 根据项目ID获取标段信息树形集合（且根据当前登陆用户标段权限过滤）
     * @param mapWhere 查询条件
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return List<SectionTreeVo>标段信息树形集合
     */
    List<SectionTreeVo> querySectionTreeListByProjectId(@RequestParam Map<String, Object> mapWhere,Integer projectId, Integer userId);

    /**
     * 根据项目ID获取项目和标段信息树形集合（且根据当前登陆用户标段权限过滤）
     * @param projectId 项目ID
     * @return List<SectionTreeVo>标段信息树形集合
     */
    List<SectionTreeVo> queryProjectSectionTreeListByProjectId(Integer projectId);

    /**
     * 根据项目ID获取标段信息树形集合（且根据当前登陆用户标段权限过滤）
     * @param projectId 项目ID
     * @return List<SectionTreeVo>标段信息树形集合
     */
    List<SectionTreeVo> querySectionTreeListByProjectId(@RequestParam Map<String, Object> mapWhere,Integer projectId);

    List<Integer> querySectionVosByProjectId(Integer projectId);

    /**
     *  获取项目团队VO
     * @param id 团队ID
     * @return
     */
    SectionVo getSectionById(Integer id);

    /**
     * 根据项目团队ID（施工标ID）查询项目团队VO（所属监理单位信息）
     * @param cuId 施工标ID（项目团队ID）
     * @return List<SectionVo> 监理单位集合
     */
    List<SectionTreeVo> queryCcuListByCuId(Integer cuId);

    /**
     * 根据项目团队ID（监理标ID）查询项目团队VO（所管施工单位信息）
     * @param ccuId 监理标ID（项目团队ID）
     * @return
     */
    List<SectionTreeVo> queryCuListByCcuId(Integer ccuId);

    List<GeneralVo> queryTeamRoles(Integer projectId, Integer id);
    //根据项目名称和部门查询标段ids
    List<Integer> querySectionIdsParams(int projectId,int orgId);
}
