package com.wisdom.base.common.feign.plan.define;

import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.form.plan.define.PlanDefineAddForm;
import com.wisdom.base.common.form.plan.define.PlanDefineUpdateForm;
import com.wisdom.base.common.form.plan.define.PlanVariableUpdateForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.plan.define.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: szc
 * @Date: 2019/7/8 15:22
 * @Version 1.0
 */
@FeignClient(value = "acm-module-plan",configuration = FeignConfiguration.class)
public interface CommPlanDefineService {

    /**
     * 获取项目的全部计划定义树型
     * @param projectIds
     * @return
     */
    default List<PlanDefineTreeVo> queryPlans(List<Integer> projectIds){

        ApiResult<List<PlanDefineTreeVo>> apiResult = this.queryPlans_(projectIds);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @PostMapping(value="/define/tree")
    ApiResult<List<PlanDefineTreeVo>> queryPlans_(@RequestBody List<Integer> projectIds);


    /**
     *  根据id查找计划定义
     * @param id
     * @return
     */
    default PlanDefineVo getPlanDefine(Integer id){
        ApiResult<PlanDefineVo> apiResult = this.getPlanDefine_(id);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping(value = "/define/{id}/info")
    ApiResult<PlanDefineVo> getPlanDefine_(@PathVariable("id") Integer id);

    /**
     *  增加计划定义
     * @param planDefineAddForm
     * @return
     */
    default PlanDefineTreeVo addPlanDefine(PlanDefineAddForm planDefineAddForm){
        if(ObjectUtils.isEmpty(planDefineAddForm.getSection())){
            throw new BaseException("计划标段不能为空");
        }
        if(ObjectUtils.isEmpty(planDefineAddForm.getPlanName())){
            throw new BaseException("计划名称不能为空");
        }
        if(ObjectUtils.isEmpty(planDefineAddForm.getPlanCode())){
            throw new BaseException("计划编码不能为空");
        }
        if(ObjectUtils.isEmpty(planDefineAddForm.getProjectId())){
            throw new BaseException("项目不能为空");
        }
        if(ObjectUtils.isEmpty(planDefineAddForm.getOrgId())){
            throw new BaseException("责任主体不能为空");
        }
        if(ObjectUtils.isEmpty(planDefineAddForm.getCalendarId())){
            throw new BaseException("日历不能为空");
        }
        if(ObjectUtils.isEmpty(planDefineAddForm.getPlanStartTime())){
            throw new BaseException("计划开始时间不能为空");
        }
        if(ObjectUtils.isEmpty(planDefineAddForm.getPlanEndTime())){
            throw new BaseException("计划完成时间不能为空");
        }
        ApiResult<PlanDefineTreeVo> apiResult = this.addPlanDefine_(planDefineAddForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @AddLog(title = "增加计划定义",module = LoggerModuleEnum.PM_DEFINE, initContent = true)
    @PostMapping(value = "/define/add")
    ApiResult<PlanDefineTreeVo> addPlanDefine_(@RequestBody PlanDefineAddForm planDefineAddForm);


    /**
     * 修改计划定义
     * @param planDefineUpdateForm
     * @return
     */
    default  PlanDefineTreeVo updatePlanDefine(PlanDefineUpdateForm planDefineUpdateForm){
        if(ObjectUtils.isEmpty(planDefineUpdateForm.getSection())){
            throw new BaseException("计划标段不能为空");
        }
        if(ObjectUtils.isEmpty(planDefineUpdateForm.getPlanCode())){
            throw new BaseException("计划编码不能为空");
        }
        if(ObjectUtils.isEmpty(planDefineUpdateForm.getPlanName())){
            throw new BaseException("计划名称不能为空");
        }
        if(ObjectUtils.isEmpty(planDefineUpdateForm.getOrgId())){
            throw new BaseException("责任主体不能为空");
        }
        if(ObjectUtils.isEmpty(planDefineUpdateForm.getPlanStartTime())){
            throw new BaseException("计划开始时间不能为空");
        }
        if(ObjectUtils.isEmpty(planDefineUpdateForm.getPlanEndTime())){
            throw new BaseException("计划完成时间不能为空");
        }
        if(ObjectUtils.isEmpty(planDefineUpdateForm.getCalendarId())){
            throw new BaseException("日历不能为空");
        }
        ApiResult<PlanDefineTreeVo> apiResult = this.updatePlanDefine_(planDefineUpdateForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @PutMapping(value = "/define/update")
    ApiResult<PlanDefineTreeVo> updatePlanDefine_(@RequestBody PlanDefineUpdateForm planDefineUpdateForm);

    /**
     * 删除计划定义
     * @param id
     * @return
     */
    default void deletePlanDefine(Integer id){
        this.deletePlanDefine_(id);
    }

    @DeleteMapping(value = "/define/{id}/delete")
    @AddLog(title = "删除计划定义",module = LoggerModuleEnum.PM_DEFINE)
    ApiResult deletePlanDefine_(@PathVariable("id") Integer id);


    /**
     *  获取变量设置信息
     * @param id
     * @return
     */
    default PlanVariableSetVo getVariableSetInfo(Integer id){
        ApiResult<PlanVariableSetVo> apiResult = this.getVariableSetInfo_(id);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping(value = "/define/variable/{id}/info")
    ApiResult getVariableSetInfo_(@PathVariable("id") Integer id);


    /**
     * 变量设置
     * @param planVariableUpdateForm
     * @return
     */
    default PlanVariableSetVo updateVariableSet(PlanVariableUpdateForm planVariableUpdateForm){
        ApiResult<PlanVariableSetVo> apiResult = this.updateVariableSet_(planVariableUpdateForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @PutMapping(value = "/define/variable/update")
    ApiResult updateVariableSet_(@RequestBody PlanVariableUpdateForm planVariableUpdateForm);

    /**
     * 获取用户权限内的计划定义集合，包括（参与任务的也要）
     *
     * @return
     */
    default List<PlanDefineAuthTreeVo> queryDefineTreeByUser(){
        ApiResult<List<PlanDefineAuthTreeVo>> apiResult = this.queryDefineTreeByUser_();
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping(value = "/define/user/auth/tree")
    ApiResult<List<PlanDefineAuthTreeVo>> queryDefineTreeByUser_();


    /**
     * 根据项目id获取计划
     * @param projectId
     * @return
     */
    default List<PlanDefineSearchVo> quereyDefineByProjectId(Integer projectId){
        ApiResult<List<PlanDefineSearchVo>> apiResult = this.quereyDefineByProjectId_(projectId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping(value = "/define/{projectId}/list")
    ApiResult<List<PlanDefineSearchVo>> quereyDefineByProjectId_(@PathVariable("projectId") Integer projectId);


    /**
     * 根据项目id获取用户权限内的计划定义集合
     *
     * @param projectId
     * @return
     */
    default List<PlanDefineAuthTreeVo> quereyDefineTreeByUserAuthAndProjectId(Integer projectId){
        ApiResult<List<PlanDefineAuthTreeVo>> apiResult = this.quereyDefineTreeByUserAuthAndProjectId_(projectId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping(value = "/define/{projectId}/auth/list")
    ApiResult<List<PlanDefineAuthTreeVo>> quereyDefineTreeByUserAuthAndProjectId_(@PathVariable("projectId") Integer projectId);


    /**
     * 根据项目id获取所有计划定义id
     * @param projectId
     * @return
     */
    default List<Integer> queryDefineIdsByProjId(Integer projectId){
        ApiResult<List<Integer>> apiResult = this.queryDefineIdsByProjId_(projectId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping("/define/{projectId}/defineIdList")
    ApiResult<List<Integer>> queryDefineIdsByProjId_(@PathVariable("projectId") Integer projectId);
}
