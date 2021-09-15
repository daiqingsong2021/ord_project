package com.wisdom.acm.sys.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.sys.form.JlSectionAddForm;
import com.wisdom.acm.sys.form.ProjectTeamAddForm;
import com.wisdom.acm.sys.form.ProjectTeamUpdateForm;
import com.wisdom.acm.sys.po.OrgSectionRelation;
import com.wisdom.acm.sys.service.ProjectTeamService;
import com.wisdom.acm.sys.service.SysOrgService;
import com.wisdom.acm.sys.vo.ProjectTeamUserVo;
import com.wisdom.acm.sys.vo.ProjectTeamVo;
import com.wisdom.acm.sys.vo.SysRoleVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.vo.RoleVo;
import com.wisdom.base.common.vo.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ProjectTeamController extends BaseController {

    @Autowired
    private ProjectTeamService projectTeamService;

    @Autowired
    private SysOrgService sysOrgService;
    @Autowired
    private CommUserService commUserService;

    /**
     * 删除业务数据时删除项目团队
     * @param bizType
     * @param bizId
     * @return
     */
    @DeleteMapping("projectteam/delete/{bizType}/{bizId}")
    @AddLog(title = "删除项目团队" , module = LoggerModuleEnum.IM_PROJTEAM)
    public ApiResult deleteByBiz(@PathVariable("bizType") String bizType,@PathVariable("bizId") Integer bizId){
        List<Integer> orgIds = projectTeamService.deleteByBiz(bizType, bizId);
        String names = sysOrgService.queryNamesByIds(orgIds,"orgName");
        this.setAcmLogger(new AcmLogger("删除项目时批量删除项目团队及其子节点，团队名称为:" + names));
        return ApiResult.success();
    }

    @GetMapping("projectteam/{id}")
    public ApiResult get(@PathVariable("id")Integer id){
        return ApiResult.success(projectTeamService.get(id));
    }

    @PostMapping("projectteam")
    @AddLog(title = "新增项目团队" , module = LoggerModuleEnum.IM_PROJTEAM)
    public ApiResult add(@RequestBody @Valid ProjectTeamAddForm form){
        this.setAcmLogger(new AcmLogger("新增项目团队，团队名称为：" + form.getTeamName()));
        int id = projectTeamService.add(form);
        //如果是标段的话需要往关联关系表中塞数据
        if(id !=0 && StringUtils.equals(form.getExtendedColumn1(),"section") && StringUtils.isNotBlank(form.getOrgCode())){
            OrgSectionRelation relation = new OrgSectionRelation();
            relation.setProjectId(form.getBizId());
            relation.setOrgId(form.getOrgId());
            relation.setOrgCode(form.getOrgCode());
            relation.setOrgName(form.getOrgName());
            //新增标段的时候没有id
            relation.setSectionId(id);
            relation.setSectionCode(form.getTeamCode());
            relation.setSectionName(form.getTeamName());
            relation.setUpdateTime(new Date());
            relation.setUpdateUser(String.valueOf(commUserService.getLoginUser().getId()));
            //新增标段时，没有sectionId
            projectTeamService.updateOrgSectionRelation(0,relation);
        }
        return ApiResult.success(projectTeamService.get(id));
    }


    @PutMapping("projectteam")
    public ApiResult update(@RequestBody @Valid ProjectTeamUpdateForm form){
        int id = projectTeamService.update(form);
        return ApiResult.success(projectTeamService.get(id));
    }

    @DeleteMapping("projectteam/delete")
    @AddLog(title = "删除项目团队" , module = LoggerModuleEnum.IM_PROJTEAM)
    public ApiResult delete(@RequestBody List<Integer> ids){
        String names = sysOrgService.queryNamesByIds(ids,"orgName");
        this.setAcmLogger(new AcmLogger("批量删除项目团队及其子节点，团队名称为:" + names));
        projectTeamService.delete(ids);
        return ApiResult.success();
    }

    @GetMapping("projectteam/{bizType}/{bizId}/tree")
    public ApiResult queryProjectTeamTree(@PathVariable("bizType") String bizType,@PathVariable("bizId") Integer bizId){
        List<ProjectTeamVo> teams = this.projectTeamService.queryProjectTeamTree(bizType, bizId);
        return ApiResult.success(teams);
    }

    @GetMapping("projectteam/{bizType}/{bizId}/list")
    public ApiResult queryProjectTeamList(@PathVariable("bizType") String bizType,@PathVariable("bizId") Integer bizId){
        List<ProjectTeamVo> teams = this.projectTeamService.queryProjectTeamList(bizType, bizId);
        return ApiResult.success(teams);
    }


    /**
     * 查询团队下的用户信息
     * @param teamId
     * @return
     */
    @GetMapping("projectteam/{teamId}/user/list")
    public ApiResult queryUserListByTeamId(@PathVariable("teamId") Integer teamId){
        List list = this.projectTeamService.queryUserListByTeamId(teamId);
        return ApiResult.success(list);
    }

    /**
     * 查找项目下用户的角色
     * @param projectId
     * @param userId
     * @return
     */
    @RequestMapping(value = "projectteam/{projectId}/{userId}/info", method = RequestMethod.GET)
    ApiResult<List<RoleVo>> queryProjectRoleList(@PathVariable("projectId") Integer projectId, @PathVariable("userId") Integer userId){
        List<RoleVo> roleVos = new ArrayList<>();
        List<SysRoleVo> list = projectTeamService.queryRoleListByProjectIdAndUserId(projectId, userId);
        if (!ObjectUtils.isEmpty(list)){
            list.forEach(vo ->{
                roleVos.add(new RoleVo(vo.getId(),vo.getRoleCode(),vo.getRoleName()));
            });
        }
        return ApiResult.success(roleVos);
    }

    /**
     * 获取项目团队明细
     * @param teamId
     * @return
     */
    @GetMapping("projectteam/{teamId}/info")
    public ApiResult getProjectTeamById(@PathVariable("teamId") Integer teamId){
        ProjectTeamVo vo = projectTeamService.get(teamId);
        return ApiResult.success(vo);
    }

    /**
     * 分配用户
     * @param teamId
     * @param data
     * @return
     */
    @PostMapping("projectteam/{teamId}/user/assign")
    @AddLog(title = "分配用户" , module = LoggerModuleEnum.IM_PROJTEAM)
    public ApiResult assignUser(@PathVariable("teamId") Integer teamId, @RequestBody List<Map<String,Object>> data){
        String logger = projectTeamService.queryLoggers(teamId,data);
        this.setAcmLogger(new AcmLogger(logger));
        this.projectTeamService.assignUser(teamId, data);
        return ApiResult.success();
    }

    /**
     * 修改用户角色
     * @param teamId
     * @param userId
     * @param roleIds
     * @return
     */
    @PutMapping("projectteam/{teamId}/user/{userId}/update")
    public ApiResult updateUserRole(@PathVariable("teamId") Integer teamId, @PathVariable("userId") Integer userId,@RequestBody List<Integer> roleIds){
        this.projectTeamService.updateUserRole(teamId, userId, roleIds);
        return ApiResult.success();
    }

    /**
     * 导入团队
     * @param dataSource
     * @param bizType
     * @param bizId
     * @param parentId
     * @param data
     * @return
     */
    @PostMapping("projectteam/{dataSource}/{bizType}/{bizId}/{parentId}/import")
    public ApiResult importProjectTeam(@PathVariable("dataSource") String dataSource, @PathVariable("bizType") String bizType,@PathVariable("bizId") Integer bizId,@PathVariable("parentId") Integer parentId, @RequestBody Map<String,Object> data){
        projectTeamService.importProjectTeam(dataSource,bizType, bizId,parentId, data);
        return ApiResult.success();
    }

    /**
     * 复制团队
     * @param sourceBizType
     * @param sourceBizId
     * @param targetBizType
     * @param targetBizId
     * @return
     */
    @PostMapping("projectteam/{sourceBizType}/{sourceBizId}/{targetBizType}/{targetBizId}/copy")
    public ApiResult  copyProjectTeam(@PathVariable("sourceBizType") String sourceBizType,@PathVariable("sourceBizId") Integer sourceBizId,@PathVariable("targetBizType") String targetBizType,
                                      @PathVariable("targetBizId") Integer targetBizId){
        projectTeamService.copyProjectTeam(sourceBizType,sourceBizId,targetBizType,targetBizId,false);
        return ApiResult.success();
    }

    /**
     * 删除项目团队下用户
     * @param ids
     * @return
     */
    @DeleteMapping("projectteam/user/delete")
    @AddLog(title = "删除项目团队下用户" , module = LoggerModuleEnum.IM_PROJTEAM)
    public ApiResult deleteUsers(@RequestBody List<Integer> ids){
        String logger = projectTeamService.queryTeamUserlogger(ids);
        this.setAcmLogger(new AcmLogger(logger));
        projectTeamService.deleteUsers(ids);
        return ApiResult.success();
    }

    /**
     * 项目团队排序
     * @param id
     * @param upOrDown
     * @return
     */
    @PutMapping("/projectteam/{id}/{upOrDown}/sort")
    public ApiResult updateProjectTeamSortByIdAndUpOrDown(@PathVariable("id") Integer id,@PathVariable("upOrDown") String upOrDown){
        List<ProjectTeamVo> projectTeamVoList = projectTeamService.updateProjectTeamSortByIdAndUpOrDown(id,upOrDown);
        return ApiResult.success(projectTeamVoList);
    }

    /**
     * 用户排序
     * @param id
     * @param upOrDown
     * @return
     */
    @PutMapping("/projectteam/user/{teamId}/{id}/{upOrDown}/sort")
    public ApiResult updateUserByUserIdAndUpOrDown(@PathVariable("teamId") Integer teamId,@PathVariable("id") Integer id,@PathVariable("upOrDown") String upOrDown){
        List<ProjectTeamUserVo> projectTeamUserVoList = projectTeamService.updateProjectTeamUserSortByIdAndUpdateOrDown(teamId,id,upOrDown);
        return ApiResult.success(projectTeamUserVoList);
    }

    /**
     * 查询施工标对应的监理标（主列表）
     * @param sectionId
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/projectteam/{sectionId}/getJlSectionList/{pageSize}/{currentPageNum}")
    public ApiResult getJlSectionList(@PathVariable("sectionId") Integer sectionId, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {

        if (ObjectUtils.isEmpty(sectionId)) {
            return ApiResult.result(777, "标段ID不能为空");
        }
        PageInfo<ProjectTeamVo> projectTeamVos = projectTeamService.selectJlSectionList(sectionId, pageSize, currentPageNum);
        return new TableResultResponse(projectTeamVos);
    }

    /**
     * 查询施工标需要分配的监理标（分配界面视图）
     * @param sectionId
     * @return
     */
    @GetMapping(value = "/projectteam/{sectionId}/getXyJlSectionList")
    public ApiResult getXyJlSectionList(@PathVariable("sectionId") Integer sectionId) {

        if (ObjectUtils.isEmpty(sectionId)) {
            return ApiResult.result(777, "标段ID不能为空");
        }
        List<ProjectTeamVo> projectTeamVos = projectTeamService.selectXyJlSectionList(sectionId);
        return ApiResult.success(projectTeamVos);
    }

    /**
     * 批量插入施工监理标
     * @param jlSectionAddForms
     * @return
     */
    @PostMapping(value = "/projectteam/addJlSection")
    public ApiResult addJlSection(@RequestBody @Valid List<JlSectionAddForm> jlSectionAddForms) {
        projectTeamService.addJlSection(jlSectionAddForms);
        return ApiResult.success();
    }

    /**
     * 批量删除施工监理标
     * @param jlSectionAddForms
     * @return
     */
    @DeleteMapping(value = "/projectteam/deleteJlSection")
    public ApiResult deleteJlSection(@RequestBody @Valid List<JlSectionAddForm> jlSectionAddForms) {
        if (ObjectUtils.isEmpty(jlSectionAddForms)) {
            return ApiResult.result(1001, "数据不能为空");
        }
        projectTeamService.deleteJlSection(jlSectionAddForms);
        return ApiResult.success();
    }
    /**
     * 标段所属部门信息
     * @param sectionId
     * @return
     */
    @GetMapping("projectteam/sectionInfo/{sectionId}")
    public ApiResult getOrgSectionRelation(@PathVariable("sectionId")Integer sectionId){
        OrgSectionRelation orgSectionRelation = projectTeamService.getOrgSectionRelation(sectionId);
        return ApiResult.success(orgSectionRelation);
    }
}
