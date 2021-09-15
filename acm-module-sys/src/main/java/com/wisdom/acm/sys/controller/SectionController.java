package com.wisdom.acm.sys.controller;

import com.wisdom.acm.sys.service.SectionService;
import com.wisdom.acm.sys.vo.ProjectTeamUserVo;
import com.wisdom.acm.sys.vo.SectionTreeVo;
import com.wisdom.acm.sys.vo.SectionVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.StringHelper;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.sys.SectionProjectVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SectionController {

    @Autowired
    private SectionService sectionService;

    /**
     * 获取标段的基本信息（ID，CODE，NAME）
     * @param pageSize
     * @param currentPageNum
     * @param projectId 项目ID
     * @return
     */
    @GetMapping(value = "section/{projectId}/{pageSize}/{currentPageNum}/list")
    public ApiResult querySectioinGeneralVoListByProjectId(@PathVariable("projectId") Integer projectId, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum,
                                                           @RequestParam(value="key") String key) {
        List<GeneralVo> list =  sectionService.querySectioinGeneralVoListByProjectId(projectId,pageSize, currentPageNum,  key);
        return ApiResult.success(list);
    }

    /**
     * 获取标段项目信息
     * @param userId 用户ID
     * @return
     */
    @GetMapping(value = "section/project/list/{userId}")
    public ApiResult querySectioinListByUserId(@PathVariable(name = "userId") Integer userId) {
        List<SectionProjectVo> list =  sectionService.querySectionProjectListByUserId(userId);
        return ApiResult.success(list);
    }

    /**
     * 获取标段数据（包含施工单位信息和监理单位信息）
     * @param projectId 项目ID
     * @return
     */
    @GetMapping(value = "section/list/{projectId}")
    public ApiResult querySectioinListByProjectId(@PathVariable(name = "projectId") Integer projectId) {
        List<SectionVo> list =  sectionService.querySectioinListByProjectId(projectId);
        return ApiResult.success(list);
    }

    /**
     * 获取标段数据
     * @param ids 标段IDS
     * @return
     */
    @PostMapping("section/list")
    public ApiResult querySectionList(@RequestBody List<Integer> ids){
        List<SectionVo> list =  sectionService.querySectionList(ids);
        return ApiResult.success(list);
    }

    /**
     * 获取用户所属项目有关的团队内的相关信息
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return
     */
    @GetMapping("section/user/list/{projectId}/{userId}")
    public ApiResult queryProjectTeamUser(@PathVariable("projectId") Integer projectId, @PathVariable("userId") Integer userId){
        List<ProjectTeamUserVo> list =this.sectionService.queryProjectTeamUser(projectId, userId);
        return ApiResult.success(list);
    }

    /**
     * 获取用户所属标段有关的专业相关的业主信息
     * @param projectId 项目ID
     * @param orgIds 团队IDS
     * @param userId 用户ID
     * @return
     */
    @PostMapping("section/owner/list/{projectId}/{userId}")
    public ApiResult queryOwnerList(@PathVariable("projectId") Integer projectId, @PathVariable("userId") Integer userId, @RequestBody List<Integer> orgIds){
        List<ProjectTeamUserVo> list =this.sectionService.queryOwnerList(projectId, orgIds, userId);
        return ApiResult.success(list);
    }

    /**
     * 判断是否是业主代表
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return
     */
    @PostMapping(value = "section/check/owner/{projectId}/{userId}")
    public ApiResult checkOwner(@PathVariable(name = "projectId") Integer projectId, @PathVariable(name = "userId") Integer userId) {
        boolean bool =  sectionService.checkOwner(projectId, userId);
        return ApiResult.success(bool);
    }

    /**
     * 查询项目团队信息
     * @param projectId 标段ID
     * @return
     */
    @GetMapping(value = "section/project/tree/{projectId}")
    public ApiResult queryProjectTeamTree(@PathVariable(name = "projectId") Integer projectId) {
        List<com.wisdom.acm.sys.vo.ProjectTeamVo>  list =  sectionService.queryProjectTeamTree(projectId);
        return ApiResult.success(list);
    }

    /**
     * 查询与标段专业相关的业主代表
     * @param id 标段ID
     * @return
     */
    @GetMapping(value = "section/owner/list/{id}")
    public ApiResult getOwnerBySectionId(@PathVariable(name = "id") Integer id) {
        List<ProjectTeamUserVo> list =  sectionService.getOwnerBySectionId(id);
        return ApiResult.success(list);
    }

    /**
     * 获取标段树形数据
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return
     */
    @GetMapping(value = "section/tree/{projectId}/{userId}")
    public ApiResult querySectionTreeList(@RequestParam Map<String, Object> mapWhere,@PathVariable(name = "projectId") Integer projectId, @PathVariable(name = "userId") Integer userId) {
        List<SectionTreeVo> list =  sectionService.querySectionTreeListByProjectId(mapWhere,projectId, userId);
        return ApiResult.success(list);
    }

    /**
     * 获取项目标段树形数据
     * @param projectId 项目ID
     * @return
     */
    @GetMapping(value = "project/section/tree/{projectId}")
    public ApiResult queryProjectSectionTreeList(@PathVariable(name = "projectId") Integer projectId) {
        List<SectionTreeVo> list =  sectionService.queryProjectSectionTreeListByProjectId(projectId);
        return ApiResult.success(list);
    }

    /**
     * 获取标段树形数据
     * @param mapWhere
     * @param projectId 项目ID
     * @return
     */
    @GetMapping(value = "section/tree/{projectId}")
    public ApiResult querySectionTreeList(@RequestParam Map<String, Object> mapWhere,@PathVariable(name = "projectId") Integer projectId) {
        List<SectionTreeVo> list =  sectionService.querySectionTreeListByProjectId(mapWhere,projectId);
        return ApiResult.success(list);
    }

    /**
     *  获取项目团队VO
     * @param id 团队ID
     * @return
     */
    @GetMapping(value = "section/info/{id}")
    public ApiResult getSectionById(@PathVariable(name = "id") Integer id) {
        SectionVo vo = this.sectionService.getSectionById(id);
        return ApiResult.success(vo);
    }

    /**
     * 根据项目团队ID（施工标ID）查询项目团队VO（所属监理单位信息）
     * @param cuId 施工标ID（项目团队ID）
     * @return
     */
    @GetMapping(value = "section/constructionControlUnit/list/{cuId}")
    public ApiResult queryConstructionControlUnitByConstructionUnitId(@PathVariable(name = "cuId") Integer cuId) {
        List<SectionTreeVo> list =  sectionService.queryCcuListByCuId(cuId);
        return ApiResult.success(list);
    }

    /**
     * 根据项目团队ID（监理标ID）查询项目团队VO（所管施工单位信息）
     * @param ccuId 监理标ID（项目团队ID）
     * @return
     */
    @GetMapping(value = "section/constructionUnit/list/{ccuId}")
    public ApiResult queryCuListByCcuId(@PathVariable(name = "ccuId") Integer ccuId) {
        List<SectionTreeVo> list =  sectionService.queryCuListByCcuId(ccuId);
        return ApiResult.success(list);
    }


    /**
     * 获取当前登陆人的标段id集合
     * @param projectId 项目ID
     * @return
     */
    @GetMapping(value = "section/idList/{projectId}")
    public ApiResult querySectionIdListList(@PathVariable(name = "projectId") Integer projectId) {
        List<Integer> sectionIds =  sectionService.querySectionVosByProjectId(projectId);
        return ApiResult.success(sectionIds);
    }
    /**
     * 通过部门获取所有标段数据
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "sectionInfoList")
    public ApiResult querySectionInfoListByOrgId(@RequestParam Map<String, Object> mapWhere) {
        String projectId = String.valueOf(mapWhere.get("projectId"));//获取项目ID
        String orgId = String.valueOf(mapWhere.get("orgId"));

        if(StringUtils.isBlank(projectId) || StringUtils.isBlank(orgId)){
            throw new BaseException("项目ID或者部门id不能为空");
        }
        List<Integer> ids =  sectionService.querySectionIdsParams(Integer.valueOf(projectId),Integer.valueOf(orgId));
        List<SectionVo> list =  sectionService.querySectionList(ids);
        return ApiResult.success(list);
    }
}
