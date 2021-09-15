package com.wisdom.acm.sys.controller;

import com.wisdom.acm.sys.form.SysOrgAddForm;
import com.wisdom.acm.sys.form.SysOrgUpdateForm;
import com.wisdom.acm.sys.form.SysSearchOrgForm;
import com.wisdom.acm.sys.po.SysOrgPo;
import com.wisdom.acm.sys.service.SysOrgService;
import com.wisdom.acm.sys.service.SysUserOrgService;
import com.wisdom.acm.sys.vo.SysOrgInfoVo;
import com.wisdom.acm.sys.vo.SysOrgSelectVo;
import com.wisdom.acm.sys.vo.SysOrgUserTreeVo;
import com.wisdom.acm.sys.vo.SysOrgVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.OrgVo;
import com.wisdom.base.common.vo.SelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "org")
public class SysOrgController extends BaseController {

    @Autowired
    private SysOrgService orgService;

    @Autowired
    private SysUserOrgService userOrgService;

    @Autowired
    private CommUserService commUserService;

    /**
     * 根据项目id获取组织
     *
     * @param projectId
     * @return
     */
    @GetMapping(value = "/{projectId}/list")
    public ApiResult queryOrgPosByProject(@PathVariable("projectId") Integer projectId) {
        List<SysOrgInfoVo> list = orgService.queryOrgPosByProjectId(projectId);
        return ApiResult.success(list);
    }

    /**
     * 获取当前登陆用户所属部门（不含团队）
     * @return
     */
    @GetMapping(value = "/{userId}/orgInfo")
    public ApiResult getUserOrgInfo(@PathVariable("userId") Integer userId) {
        SysOrgInfoVo orgInfoVo = this.orgService.getUserOrgInfo(userId);
        return ApiResult.success(orgInfoVo);
    }

    /**
     * 获取当前登陆用户所属部门（不含团队）
     * @return
     */
    @GetMapping(value = "/info")
    public ApiResult getUserOrgInfo() {
        int userId = commUserService.getLoginUser().getId();
        SysOrgInfoVo orgInfoVo = this.orgService.getUserOrgInfo(userId);
        return ApiResult.success(orgInfoVo);
    }

    /**
     * 获取组织
     *
     * @return
     */
    @GetMapping(value = "/tree")
    public ApiResult queryOrgs() {
        List<SysOrgVo> list = orgService.queryOrgTree();
        return ApiResult.success(list);
    }

    /**
     * 搜索org
     *
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search")
    public ApiResult queryOrgsBySearch(SysSearchOrgForm searchMap) {
        List<SysOrgVo> list = orgService.queryOrgsBySearch(searchMap);
        return ApiResult.success(list);
    }

    /**
     * 获取组织信息
     *
     * @param orgId
     * @return
     */
    @GetMapping(value = "/{orgId}/info")
    public ApiResult queryOrgInfo(@PathVariable("orgId") Integer orgId) {
        SysOrgInfoVo sysOrgInfoVo = orgService.getOrgInfo(orgId);
        return ApiResult.success(sysOrgInfoVo);
    }

    /**
     * 获取组织信息
     *
     * @param orgIds
     * @return
     */
    @PostMapping(value = "/orgids/list")
    public ApiResult queryOrgList(@RequestBody @Valid List<Integer> orgIds) {
        List<SysOrgInfoVo> sysOrgInfoVos = orgService.queryOrgInfoVoByOrgIds(orgIds);
        return ApiResult.success(sysOrgInfoVos);
    }

    /**
     * 获取组织信息
     *
     * @param orgIds
     * @return
     */
    @PostMapping(value = "/orgpo/list")
    public ApiResult queryOrgPosByIds(@RequestBody @Valid List<Integer> orgIds) {
        List<SysOrgPo> sysOrgPos = orgService.selectByIds(orgIds);
        return ApiResult.success(sysOrgPos);
    }

    /**
     * 获取组织信息(根据编码)
     *
     * @param orgCode
     * @return
     */
    @GetMapping(value = "/{orgCode}/orginfo")
    public ApiResult<SysOrgPo> getOrgByCode(@PathVariable("orgCode") String orgCode) {
        List<SysOrgPo> sysOrgPos = orgService.getOrgPoByCode(orgCode);
        return ApiResult.success(!ObjectUtils.isEmpty(sysOrgPos) ? sysOrgPos.get(0): null);
    }


    /**
     * 增加组织
     *
     * @param sysOrgAddVo
     * @return
     */
    @AddLog(title = "增加组织" , module = LoggerModuleEnum.SM_ORG)
    @PostMapping(value = "/add")
    public ApiResult addOrg(@RequestBody @Valid SysOrgAddForm sysOrgAddVo) {
        SysOrgPo sysOrgPo = orgService.addOrg(sysOrgAddVo);
        SysOrgVo sysOrgVo = new SysOrgVo();
        sysOrgVo.setId(sysOrgPo.getId());
        sysOrgVo.setOrgName(sysOrgPo.getOrgName());
        sysOrgVo.setOrgType(sysOrgPo.getOrgType());
        sysOrgVo.setParentId(sysOrgPo.getParentId());
        sysOrgVo.setOrgCode(sysOrgPo.getOrgCode());
        return ApiResult.success(sysOrgVo);
    }

    /**
     * 修改组织
     *
     * @param sysOrgUpdateVo
     * @return
     */
    @PutMapping(value = "/update")
    public ApiResult updateOrg(@RequestBody @Valid SysOrgUpdateForm sysOrgUpdateVo) {
        SysOrgPo sysOrgPo = orgService.updateOrg(sysOrgUpdateVo);
        SysOrgVo sysOrgVo = new SysOrgVo();
        sysOrgVo.setId(sysOrgPo.getId());
        sysOrgVo.setOrgName(sysOrgPo.getOrgName());
        sysOrgVo.setOrgType(sysOrgPo.getOrgType());
        sysOrgVo.setParentId(sysOrgUpdateVo.getParentId());
        sysOrgVo.setOrgCode(sysOrgPo.getOrgCode());
        return ApiResult.success(sysOrgVo);
    }


    /**
     * 删除组织
     *
     * @param orgIds
     * @return
     */
    @DeleteMapping(value = "/delete")
    @AddLog(title = "删除组织",module = LoggerModuleEnum.SM_ORG)
    public ApiResult deleteOrg(@RequestBody List<Integer> orgIds) {
        String names = orgService.queryNamesByIds(orgIds,"orgName");
        this.setAcmLogger(new AcmLogger("批量删除组织及其子节点组织，组织名称如下：" + names));
        orgService.deleteOrg(orgIds);
        return ApiResult.success();
    }

    /**
     * 删除组织用户
     *
     * @param userIds
     * @return
     */
    @DeleteMapping(value = "/{orgId}/user/delete")
    @AddLog(title = "删除组织用户",module = LoggerModuleEnum.SM_ORG)
    public ApiResult deleteOrgUser(@RequestBody List<Integer> userIds, @PathVariable("orgId") Integer orgId) {
        orgService.deleteOrgUser(userIds, orgId);
        String logger = orgService.queryDeleteOrgUserLogger(userIds,orgId);
        this.setAcmLogger(new AcmLogger(logger));
        return ApiResult.success();
    }


    /**
     * 根据项目id获取组织
     *
     * @param projectId
     * @return
     */
    @GetMapping(value = "/{projectId}/select/tree")
    public ApiResult queryOrgByProject(@PathVariable("projectId") Integer projectId) {
        List<SelectVo> list = orgService.queryOrgSelectVosByProjectId(projectId);
        return ApiResult.success(list);
    }

    /**
     * 问题管理，根据项目ID获取组织
     * @param projectId
     * @return
     */
    @GetMapping(value = "/ques/{projectId}/select/tree")
    public ApiResult queryQuesOrgByProject(@PathVariable("projectId") Integer projectId) {
        List<SelectVo> list = orgService.queryQuesOrgSelectVosByProjectId(projectId);
        return ApiResult.success(list);
    }

    /**
     * 获取全局ORG的树形集合
     *
     * @return
     */
    @GetMapping(value = "/select/tree")
    public ApiResult queryGlobalOrgSelectVos() {
        List<SelectVo> list = orgService.queryGlobalOrgSelectVos();
        return ApiResult.success(list);
    }

    @GetMapping(value = "/ques/select/tree")
    public ApiResult queryQuesGlobalOrgSelectVos() {
        List<SelectVo> list = orgService.queryQuesGlobalOrgSelectVos();
        return ApiResult.success(list);
    }

    /**
     * 获取全组织的树形集合
     *
     * @return
     */
    @GetMapping(value = "orgName/select/tree")
    public ApiResult queryOrgNameSelectVos() {
        List<SelectVo> list = orgService.queryOrgNameSelectVos();
        return ApiResult.success(list);
    }

    /**
     * 根据组织id获取组织信息
     *
     * @return
     */
    @PostMapping(value = "/orgvo/maps")
    public ApiResult queryOrgVoByOrgIds(@RequestBody List<Integer> orgIds) {
        Map<Integer, OrgVo> orgVoMap = new HashMap<>();
        orgVoMap = orgService.queryOrgVoByOrgIds(orgIds);
        return ApiResult.success(orgVoMap);
    }

    /**
     * 获取组织用户列表树形
     *
     * @return
     */
    @GetMapping(value = "/user/tree")
    public ApiResult queryOrgUserTree() {
        List<SysOrgUserTreeVo> list = orgService.queryOrgUserTree();
        return ApiResult.success(list);
    }


    /**
     * 获取组织用户列表树形
     *
     * @return
     */
    @GetMapping(value = "/select/list")
    public ApiResult queryOrgSelectVo() {
        List<SysOrgSelectVo> list = orgService.queryOrgSelectVo();
        return ApiResult.success(list);
    }

    @GetMapping(value = "/{userId}/mainorg")
    public ApiResult getMainOrgByUserId(@PathVariable("userId") Integer userId){
        SysOrgInfoVo sysOrgInfoVo = orgService.querySysUserOrgPoByUserId(userId);
        return ApiResult.success(sysOrgInfoVo);
    }

}
