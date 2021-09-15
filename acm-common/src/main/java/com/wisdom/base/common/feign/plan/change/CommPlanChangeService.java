package com.wisdom.base.common.feign.plan.change;

import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.form.plan.change.*;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.po.PlanTaskChangeApplyPo;
import com.wisdom.base.common.vo.plan.change.PlanTaskChangeGeneralVo;
import com.wisdom.base.common.vo.plan.change.PlanTaskChangeRecordVo;
import com.wisdom.base.common.vo.plan.change.PlanTaskChangeReleasaeTreeVo;
import com.wisdom.base.common.vo.plan.change.PlanTaskChangeTreeVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: szc
 * @Date: 2019/7/9 9:34
 * @Version 1.0
 */
@FeignClient(value = "acm-module-plan",configuration = FeignConfiguration.class)
public interface CommPlanChangeService {

    /**
     * 获取变更树
     * @param defineIds
     * @return
     */
    default List<PlanTaskChangeTreeVo> queryPlanTaskChangTree(List<Integer> defineIds){
        ApiResult<List<PlanTaskChangeTreeVo>> apiResult = this.queryPlanTaskChangTree_(defineIds);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PostMapping("task/change/tree")
    ApiResult queryPlanTaskChangTree_(@RequestBody List<Integer> defineIds);


    /**
     *
     * 获取WBS变更信息
     * @param wbsId
     * @return
     */
    default PlanTaskChangeGeneralVo queryWbsChangeInfo(Integer wbsId, String type){
        ApiResult<PlanTaskChangeGeneralVo> apiResult = this.queryWbsChangeInfo_(wbsId,type);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/wbs/change/{type}/{wbsId}/info")
    ApiResult queryWbsChangeInfo_(@PathVariable("wbsId") Integer wbsId, @PathVariable("type") String type);

    /**
     * 获取task变更信息
     * @param taskId
     * @return
     */
    default PlanTaskChangeGeneralVo queryTaskChangeInfo(Integer taskId, String type){
        ApiResult<PlanTaskChangeGeneralVo> apiResult = this.queryTaskChangeInfo_(taskId,type);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/task/change/{type}/{taskId}/info")
    ApiResult queryTaskChangeInfo_(@PathVariable("taskId") Integer taskId, @PathVariable("type") String type);


    /**
     * 增加WBS变更
     * @param planTaskChangeWbsAddForm
     * @return
     */
    default PlanTaskChangeTreeVo addWbsPlanTaskChange(PlanTaskChangeWbsAddForm planTaskChangeWbsAddForm){
        if(ObjectUtils.isEmpty(planTaskChangeWbsAddForm.getProjectId())){
            throw new BaseException("项目id不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeWbsAddForm.getDefineId())){
            throw new BaseException("计划定义不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeWbsAddForm.getNewCode())){
            throw new BaseException("代码不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeWbsAddForm.getNewName())){
            throw new BaseException("名称不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeWbsAddForm.getNewOrgId())){
            throw new BaseException("责任主体不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeWbsAddForm.getNewCalendarId())){
            throw new BaseException("日历不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeWbsAddForm.getNewPlanStartTime())){
            throw new BaseException("计划开始时间不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeWbsAddForm.getNewPlanEndTime())){
            throw new BaseException("计划完成时间不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeWbsAddForm.getNewPlanDrtn())){
            throw new BaseException("计划工期不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeWbsAddForm.getNewPlanQty())){
            throw new BaseException("计划工时不能为空");
        }
        ApiResult<PlanTaskChangeTreeVo> apiResult = this.addWbsPlanTaskChange_(planTaskChangeWbsAddForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PostMapping("/wbs/change/add")
    @AddLog(title = "增加WBS变更",module = LoggerModuleEnum.PM_CHANGE,initContent = true)
    ApiResult addWbsPlanTaskChange_(@RequestBody PlanTaskChangeWbsAddForm planTaskChangeWbsAddForm);


    /**
     * 新增任务变更
     * @param planTaskChangeTaskAddForm
     * @return
     */
    default PlanTaskChangeTreeVo addTaskPlanTaskChange(PlanTaskChangeTaskAddForm planTaskChangeTaskAddForm){
        if(ObjectUtils.isEmpty(planTaskChangeTaskAddForm.getNewCode())){
            throw new BaseException("代码不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeTaskAddForm.getNewName())){
            throw new BaseException("名称不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeTaskAddForm.getNewOrgId())){
            throw new BaseException("责任主体不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeTaskAddForm.getNewCalendarId())){
            throw new BaseException("日历不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeTaskAddForm.getNewPlanStartTime())){
            throw new BaseException("计划开始时间不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeTaskAddForm.getNewPlanEndTime())){
            throw new BaseException("计划完成时间不能为空");
        }
        ApiResult<PlanTaskChangeTreeVo> apiResult = this.addTaskPlanTaskChange_(planTaskChangeTaskAddForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PostMapping("/task/change/add")
    @AddLog(title = "增加任务变更",module = LoggerModuleEnum.PM_CHANGE,initContent = true)
    ApiResult addTaskPlanTaskChange_(@RequestBody PlanTaskChangeTaskAddForm planTaskChangeTaskAddForm);


    /**
     *  删除wbs变更
     * @param wbsId
     * @return
     */
    default PlanTaskChangeTreeVo deletePlanWbsChange(Integer wbsId){
        ApiResult<PlanTaskChangeTreeVo> apiResult = this.deletePlanWbsChange_(wbsId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @DeleteMapping("/wbs/change/{wbsId}/delete")
    @AddLog(title = "删除wbs变更",module = LoggerModuleEnum.PM_CHANGE)
    ApiResult deletePlanWbsChange_(@PathVariable("wbsId") Integer wbsId);


    /**
     * 删除任务变更
     * @param taskId
     * @return
     */
    default PlanTaskChangeTreeVo deletePlanTaskChange(Integer taskId){
        ApiResult<PlanTaskChangeTreeVo> apiResult = this.deletePlanTaskChange_(taskId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @DeleteMapping("/task/change/{taskId}/delete")
    @AddLog(title = "删除任务变更",module = LoggerModuleEnum.PM_CHANGE)
    ApiResult deletePlanTaskChange_(@PathVariable("taskId") Integer taskId);

    /**
     * wbs变更页签保存
     * @param planWbsChangeInfoForm
     * @return
     */
    default PlanTaskChangeTreeVo updatePlanWbsChange(PlanWbsChangeInfoForm planWbsChangeInfoForm){
        if(ObjectUtils.isEmpty(planWbsChangeInfoForm.getNewCalendarId())){
            throw new BaseException("日历不能为空");
        }
        ApiResult<PlanTaskChangeTreeVo> apiResult = this.updatePlanWbsChange_(planWbsChangeInfoForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PutMapping("/wbs/change/update")
    ApiResult updatePlanWbsChange_(@RequestBody PlanWbsChangeInfoForm planWbsChangeInfoForm);


    /**
     * 任务变更页签保存
     * @return
     */
    default PlanTaskChangeTreeVo updatePlanTaskChange(PlanTaskChangeInfoForm planTaskChangeInfoForm){
        if(ObjectUtils.isEmpty(planTaskChangeInfoForm.getNewName())){
            throw new BaseException("名称不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeInfoForm.getNewOrgId())){
            throw new BaseException("责任主体不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskChangeInfoForm.getNewCalendarId())){
            throw new BaseException("日历不能为空");
        }
        ApiResult<PlanTaskChangeTreeVo> apiResult = this.updatePlanTaskChange_(planTaskChangeInfoForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PutMapping("/task/change/update")
    ApiResult updatePlanTaskChange_(@RequestBody PlanTaskChangeInfoForm planTaskChangeInfoForm);


    /**
     * 开始里程碑页签保存
     * @param planTaskChangeInfoForm
     * @return
     */
    default PlanTaskChangeTreeVo updatePlanStartMileStoneChange(PlanTaskChangeInfoForm planTaskChangeInfoForm){
        ApiResult<PlanTaskChangeTreeVo> apiResult = this.updatePlanStartMileStoneChange_(planTaskChangeInfoForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PutMapping("/start/change/update")
    ApiResult updatePlanStartMileStoneChange_(@RequestBody PlanTaskChangeInfoForm planTaskChangeInfoForm);


    /**
     * 完成里程碑页签保存
     * @param planTaskChangeInfoForm
     * @return
     */
    default PlanTaskChangeTreeVo UpdatePlanEndMileStoneChange(PlanTaskChangeInfoForm planTaskChangeInfoForm){
        ApiResult<PlanTaskChangeTreeVo> apiResult = this.UpdatePlanEndMileStoneChange_(planTaskChangeInfoForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PutMapping("/end/changer/update")
    ApiResult UpdatePlanEndMileStoneChange_(@RequestBody PlanTaskChangeInfoForm planTaskChangeInfoForm);

    /**
     * 计划编制变更页签
     * @param taskId
     * @return
     */
    default List<PlanTaskChangeRecordVo> queryPlanTaskChangeInfoByTaskId(Integer taskId){
        ApiResult<List<PlanTaskChangeRecordVo>> apiResult = this.queryPlanTaskChangeInfoByTaskId_(taskId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PostMapping("/task/{taskId}/change/list")
    ApiResult queryPlanTaskChangeInfoByTaskId_(@PathVariable("taskId") Integer taskId);

    /**
     * 撤销变更
     * @param taskId
     * @return
     */
    default PlanTaskChangeTreeVo cancelChgWBS(Integer taskId, String type){
        ApiResult<PlanTaskChangeTreeVo> apiResult = this.cancelChgWBS_(taskId,type);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PutMapping("/task/change/{taskId}/{type}/cancel")
    ApiResult cancelChgWBS_(@PathVariable("taskId") Integer taskId, @PathVariable("type") String type);

    /**
     * 获取变更审批树
     * @param defineId
     * @return
     */
    default List<PlanTaskChangeReleasaeTreeVo> queryPlanTaskChangeReleaseTree(List<Integer> defineId){
        ApiResult<List<PlanTaskChangeReleasaeTreeVo>> apiResult = this.queryPlanTaskChangeReleaseTree_(defineId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PostMapping("/task/change/release/tree")
    ApiResult queryPlanTaskChangeReleaseTree_(@RequestBody List<Integer> defineIds);

    /**
     * 批准变更
     * @param planTaskChangeReleaseForm
     * @return
     */
    default PlanTaskChangeApplyPo releasePlanTaskChange(PlanTaskChangeReleaseForm planTaskChangeReleaseForm){
        ApiResult<PlanTaskChangeApplyPo> apiResult = this.releasePlanTaskChange_(planTaskChangeReleaseForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PutMapping("/task/change/release")
    ApiResult releasePlanTaskChange_(@RequestBody PlanTaskChangeReleaseForm planTaskChangeReleaseForm);

    /**
     * 保存变更至变更单
     *
     * @param planTaskChangeReleaseForm
     * @return
     */
    default PlanTaskChangeApplyPo saveAsPlanTaskChange(PlanTaskChangeReleaseForm planTaskChangeReleaseForm){
        ApiResult<PlanTaskChangeApplyPo> apiResult = this.saveAsPlanTaskChange_(planTaskChangeReleaseForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PutMapping("/task/change/saveas/apply")
    ApiResult saveAsPlanTaskChange_(@RequestBody PlanTaskChangeReleaseForm planTaskChangeReleaseForm);

    /**
     * 获取变更记事
     * @param taskId
     * @return
     */
    default List<PlanTaskChangeRecordVo> queryPlanTaskChangeRecord(Integer taskId){
       ApiResult<List<PlanTaskChangeRecordVo>> apiResult = this.queryPlanTaskChangeRecord_(taskId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/task/change/{taskId}/record")
    ApiResult queryPlanTaskChangeRecord_(@PathVariable("taskId") Integer taskId);


    /**
     * 流程处理（变更）
     * @return
     */
    default List<PlanTaskChangeTreeVo> queryTaskChangeWfTree(Integer applyId){
        ApiResult<List<PlanTaskChangeTreeVo>> apiResult = this.queryTaskChangeWfTree_(applyId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PostMapping("/task/change/{applyId}/tree")
    ApiResult queryTaskChangeWfTree_(@PathVariable("applyId") Integer applyId);

    /**
     * 根据任务id获取所有的变更表单id集合
     * @param taskId
     * @return
     */
    default List<Integer> queryApplyIdsByTaskId(Integer taskId, String type){
        ApiResult<List<Integer>> apiResult = this.queryApplyIdsByTaskId_(taskId,type);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/task/change/{taskId}/{type}/applyId")
    ApiResult queryApplyIdsByTaskId_(@PathVariable("taskId") Integer taskId, @PathVariable("type") String type);
}
