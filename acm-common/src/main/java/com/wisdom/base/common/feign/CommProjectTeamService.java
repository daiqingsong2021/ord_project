package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.ProjectTeamTreeVo;
import com.wisdom.base.common.vo.ProjectTeamUserVo;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.SectionTreeVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 项目团队
 */
@FeignClient(value = "acm-module-sys",configuration = FeignConfiguration.class)
public interface CommProjectTeamService {

    /**
     * 获取标段数据（包含施工单位信息和监理单位信息）
     * @param projectId 项目ID
     * @return
     */
    default List<ProjectTeamVo> querySectioinListByProjectId(Integer projectId){
        ApiResult<List<ProjectTeamVo>>  apiResult = this.querySectioinListByProjectId_(projectId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 获取标段数据
     * @param ids 标段IDS
     * @return
     */
    default List<ProjectTeamVo> querySectionList(List<Integer> ids){
        ApiResult<List<ProjectTeamVo>>  apiResult = this.querySectionList_(ids);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 获取用户参与的项目团队
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return
     */
    default List<ProjectTeamUserVo> queryProjectTeamUser(Integer projectId, Integer userId){
        ApiResult<List<ProjectTeamUserVo>>  apiResult = this.queryProjectTeamUser_(projectId, userId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 获取用户所属标段有关的专业相关的业主信息
     * @param projectId 项目ID
     * @param userId 用户ID
     * @param orgIds 团队IDS
     * @return
     */
    default List<ProjectTeamUserVo> queryOwnerList(Integer projectId,Integer userId, List<Integer> orgIds){
        ApiResult<List<ProjectTeamUserVo>>  apiResult = this.queryOwnerList_(projectId,userId,orgIds);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 判断是否是业主代表
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return
     */
    default Boolean checkOwner(Integer projectId, Integer userId){
        ApiResult<Boolean> apiResult  = this.checkOwner_(projectId, userId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 获取团队树形数据
     * @param projectId  项目ID
     * @return
     */
    default List<ProjectTeamTreeVo> queryProjectTeamTree(Integer projectId){
        ApiResult<List<ProjectTeamTreeVo>>  apiResult = this.queryProjectTeamTree_(projectId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 获取标段树形数据
     * @param projectId 项目ID
     * @param userId 项目ID
     * @return
     */
    default List<SectionTreeVo> querySectionTreeList(Integer projectId, Integer userId){
        ApiResult<List<SectionTreeVo>>  apiResult = this.querySectionTreeList_(projectId, userId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 获取标段树形数据
     * @param projectId 项目ID
     * @return
     */
    default List<SectionTreeVo> querySectionTreeList(Integer projectId){
        ApiResult<List<SectionTreeVo>>  apiResult = this.querySectionTreeList_(projectId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 查询与标段专业相关的业主代表
     * @param id 标段ID
     * @return
     */
    default List<ProjectTeamUserVo> getOwnerBySectionId(Integer id){
        ApiResult<List<ProjectTeamUserVo>>  apiResult = this.getOwnerBySectionId_(id);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 查询团队下的团队成员信息
     * @param teamId
     * @return
     */
    default List<ProjectTeamUserVo> queryUserListByProjectTeamId(Integer teamId){
        ApiResult<List<ProjectTeamUserVo>>  apiResult = this.queryUserListByTeamId(teamId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     *  获取项目团队VO(包含施工单位和监理单位信息)
     * @param id 团队ID
     * @return
     */
    default ProjectTeamVo getProjectTeamById(Integer id){
        ApiResult<ProjectTeamVo>  apiResult = this.getSectionById(id);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 根据项目团队ID（监理标ID）查询项目团队VO（所管施工单位信息）
     * @param ccuId 监理单位ID（项目团队ID）
     * @return
     */
    default List<ProjectTeamVo> queryCuListByCcuId(Integer ccuId){
       ApiResult<List<ProjectTeamVo>>  apiResult = this.queryConstructionUnitListByCcuId(ccuId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 根据项目团队ID（施工标ID）查询项目团队VO（所属监理单位信息）
     * @param cuId 施工单位ID（项目团队ID）
     * @return
     */
    default List<ProjectTeamVo> queryConstructionControlUnitByCUnitId(Integer cuId){
        ApiResult<List<ProjectTeamVo>>  apiResult = this.queryConstructionControlUnitByConstructionUnitId(cuId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * 获取标段数据（包含施工单位信息和监理单位信息）
     * @param projectId 项目ID
     * @return
     */
    @GetMapping(value = "/section/list/{projectId}")
    ApiResult<List<ProjectTeamVo>> querySectioinListByProjectId_(@PathVariable(name = "projectId") Integer projectId);

    /**
     * 获取标段数据
     * @param ids 标段IDS
     * @return
     */
    @PostMapping(value = "/section/list")
    ApiResult<List<ProjectTeamVo>> querySectionList_(@RequestBody List<Integer> ids);


    /**
     * 获取用户所属项目有关的团队内的相关信息
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return
     */
    @GetMapping(value = "/section/user/list/{projectId}/{userId}")
    ApiResult<List<ProjectTeamUserVo>> queryProjectTeamUser_(@PathVariable(name = "projectId") Integer projectId, @PathVariable(name = "userId") Integer userId);

    /**
     * 获取用户所属标段有关的专业相关的业主信息
     * @param projectId 项目ID
     * @param userId 用户ID
     * @param orgIds 团队IDS
     * @return
     */
    @PostMapping(value = "/section/owner/list/{projectId}/{userId}")
    ApiResult<List<ProjectTeamUserVo>> queryOwnerList_(@PathVariable(name = "projectId") Integer projectId,@PathVariable(name = "userId") Integer userId, @RequestBody List<Integer> orgIds);

    /**
     * 判断是否是业主代表
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return
     */
    @PostMapping(value = "/section/check/owner/{projectId}/{userId}")
    ApiResult<Boolean> checkOwner_(@PathVariable(name = "projectId") Integer projectId,@PathVariable(name = "userId") Integer userId);

    /**
     * 获取团队树形数据
     * @param projectId  项目ID
     * @return
     */
    @GetMapping(value = "/section/project/tree/{projectId}")
    ApiResult<List<ProjectTeamTreeVo>> queryProjectTeamTree_(@PathVariable(name = "projectId") Integer projectId);

    /**
     * 获取标段树形数据
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return
     */
    @GetMapping(value = "/section/tree/{projectId}/{userId}")
    ApiResult<List<SectionTreeVo>> querySectionTreeList_(@PathVariable(name = "projectId") Integer projectId, @PathVariable(name = "userId") Integer userId);

    /**
     * 获取标段树形数据
     * @param projectId 项目ID
     * @return
     */
    @GetMapping(value = "/section/tree/{projectId}")
    ApiResult<List<SectionTreeVo>> querySectionTreeList_(@PathVariable(name = "projectId") Integer projectId);

    /**
     * 查询与标段专业相关的业主代表
     * @param id 标段ID
     * @return
     */
    @GetMapping(value = "/section/owner/list/{id}")
    ApiResult<List<ProjectTeamUserVo>> getOwnerBySectionId_(@PathVariable(name = "id") Integer id);

    /**
     * 查询团队下的用户信息
     * @param teamId
     * @return
     */
    @GetMapping("/projectteam/{teamId}/user/list")
    ApiResult<List<ProjectTeamUserVo>> queryUserListByTeamId(@PathVariable("teamId") Integer teamId);

    /**
     *  获取项目团队VO
     * @param id 团队ID
     * @return
     */
    @GetMapping(value = "/section/info/{id}")
    ApiResult<ProjectTeamVo> getSectionById(@PathVariable("id") Integer id);

    /**
     * 根据项目团队ID（施工标ID）查询项目团队VO（所属监理单位信息）
     * @param cuId 施工标ID（项目团队ID）
     * @return
     */
    @GetMapping(value = "/section/constructionControlUnit/list/{cuId}")
    ApiResult<List<ProjectTeamVo>> queryConstructionControlUnitByConstructionUnitId(@PathVariable("cuId") Integer cuId);

    /**
     * 根据项目团队ID（监理标ID）查询项目团队VO（所管施工单位信息）
     * @param ccuId 监理标ID（项目团队ID）
     * @return
     */
    @GetMapping(value = "/section/constructionUnit/list/{ccuId}")
    ApiResult<List<ProjectTeamVo>> queryConstructionUnitListByCcuId(@PathVariable("ccuId") Integer ccuId);
}
