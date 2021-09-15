package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.po.SysOrgPo;
import com.wisdom.acm.sys.vo.ProjectTeamUserVo;
import com.wisdom.acm.sys.vo.SectionTreeVo;
import com.wisdom.acm.sys.vo.SectionVo;
import com.wisdom.acm.sys.vo.SysOrgUserVo;
import com.wisdom.base.common.mapper.CommMapper;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.sys.SectionProjectVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 标段-数据处理
 */
public interface SectionMapper extends CommMapper<SysOrgPo> {

    /**
     * 获取标段项目信息
     * @param type org/section
     * @param projectIds 项目IDS
     * @return
     */
    List<SectionProjectVo> selectSectionProjectVos(@Param("type") String type, @Param("projectIds") List<Integer> projectIds);

    /**
     * 根据项目ID查询出顶层业主单位集合
     * @param projectId 项目ID
     * @return
     */
    List<SectionTreeVo> selectOwnerOrgListByProjectId(@Param("projectId") Integer projectId);

    /**
     * 获取项目某类型团队下的团队成员
     * @param projectId
     * @param orgType
     * @param userId
     * @return
     */
    List<ProjectTeamUserVo> selectProjectTreamUserList(@Param("projectId")int projectId, @Param("orgType")String orgType, @Param("userId")Integer userId);

    /**
     * 查询组织用户信息
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return
     */
    List<SysOrgUserVo> selectSysOrgUserList(@Param("projectId") Integer projectId, @Param("userId") Integer userId);

    /**
     * 查询组织用户信息
     * @param orgId
     * @return
     */
    List<SysOrgUserVo> selectSysOrgUserVoList(@Param("orgId") Integer orgId);

    /**
     * 查询组织用户信息
     * @param orgId
     * @param userId
     * @return
     */
    SysOrgUserVo selectSysOrgUserVo(@Param("orgId") Integer orgId, @Param("userId") Integer userId);

    /**
     * 根据当前用户的ID和项目ID，查询该用户能直接看到的该项目下的标段信息(业主可以看所有的标段，非业主只能看到用户相关的标段)
     * @param userId 用户ID
     * @param projectId  项目ID
     * @return List<SectionTreeVo> 标段信息集合
     */
    List<SectionTreeVo> selectSectionByUserIdAndProjectId(@Param("userId") Integer userId, @Param("projectId") Integer projectId);

    /**
     * 获取团队下的子级团队信息
     * @param parentIds 父级IDS
     * @param type org/单位 section/标段
     * @return List<SectionTreeVo> 信息集合
     */
    List<SectionTreeVo> selectSubSectionByParentIds(@Param("parentIds") List<Integer> parentIds, @Param("type") String type);

    /**
     * 获取标段信息
     * @param ids 标段IDS
     * @return
     */
    List<SectionVo> selectSectionByIds(@Param("ids") List<Integer> ids);

    /**
     * 获取标段信息
     * @param projectId 项目ID
     * @param key 关键字
     * @return
     */
    List<SectionVo> selectSectionByProjectId(@Param("projectId") Integer projectId, @Param("key") String key);

    /**
     * 根据条件查询标段
     * @param mapWhere
     * @return
     */
    List<SectionVo> selectSections(Map<String,Object> mapWhere);

    /**
     * 获取项目团队VO
     * @param id 团队ID
     * @return
     */
    SectionVo selectSectionById(@Param("id") Integer id);

    /**
     * 查询项目团队下的角色
     * @param projectId
     * @param userId
     * @return
     */
    List<GeneralVo> queryTeamRoles(@Param("projectId") Integer projectId, @Param("userId") Integer userId);

    /**
     * 根据项目ID ，用户ID查询该用户参与的标段IDS
     * @param userId
     * @return
     */
    List<Integer> selectSectUserOrgIdsByUId(@Param("projectId") Integer projectId,@Param("userId") Integer userId);

    /**
     * 查询与监理标段有关的施工标
     * @param sectionTreeVoList
     * @return
     */
    List<SectionTreeVo> selectSgSectionByJls(@Param("jlbList") List<SectionTreeVo> sectionTreeVoList);

    /**
     * 查询施工监理对应表数据
     * @param mapWhere
     * @return
     */
    List<Map<String,Object>> selectSgJlRelat(Map<String,Object> mapWhere);
    List<Integer> querySectionIdsParams(int projectId, int orgId);
}
