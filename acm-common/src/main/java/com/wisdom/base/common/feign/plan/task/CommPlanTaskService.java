package com.wisdom.base.common.feign.plan.task;

import com.github.pagehelper.PageInfo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.form.plan.task.*;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.po.PlanTaskPo;
import com.wisdom.base.common.vo.plan.task.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: szc
 * @Date: 2019/7/8 17:31
 * @Version 1.0
 */
@FeignClient(value = "acm-module-plan",configuration = FeignConfiguration.class)
public interface CommPlanTaskService {

    /**
     * 获取计划数据
     * @param ids
     * @return
     */
    default  List<PlanTaskPo> queryPlanTask(List<Integer> ids){
        ApiResult<List<PlanTaskPo>> apiResult = null;
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 获取计划编制树
     * @param paramMap 参数
     * @return
     */
    default List<PlanTaskTreeVo> taskTree(Map<String, Object> paramMap, PlanTaskViewSearchForm search){
        ApiResult<List<PlanTaskTreeVo>> apiResult = this.taskTree_(paramMap,search.getNameOrCode(),search.getOrgId(),search.getUserId(),
                search.getStartDateLimitBefore(),search.getStartDateLimitAfter(),search.getEndDateLimitBefore(),search.getEndDateLimitAfter(),search.getPlanEndType(),search.getNullDelv(),
                search.getNullWbs(),search.getNullUser(),search.getNullRsrc(),search.getFeedbackStatus(),search.getStatus(),search.getChildren(),search.getOnlyTask(),search.getFuzzySearch(),
                search.getEndDateLimitBefore2(),search.getEndDateLimitAfter2(),search.getIsFeedback(),search.getPlanType(),
                search.getCustom01(),search.getCustom02(),search.getCustom03(),search.getCustom04(),search.getCustom05(),search.getCustom06(),
                search.getCustom07(),search.getCustom08(),search.getCustom09(),search.getCustom10());
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

  @PostMapping(value = "/task/tree")
    ApiResult<List<PlanTaskTreeVo>>  taskTree_(@RequestBody Map<String, Object> paramMap, @RequestParam("nameOrCode") String nameOrCode, @RequestParam("orgId") Integer orgId,
                        @RequestParam("userId") Integer userId, @RequestParam("startDateLimitBefore") String startDateLimitBefore, @RequestParam("startDateLimitAfter") String startDateLimitAfter,
                        @RequestParam("endDateLimitBefore") String endDateLimitBefore, @RequestParam("endDateLimitAfter") String endDateLimitAfter, @RequestParam("planEndType") String planEndType,
                        @RequestParam("nullDelv") Boolean nullDelv, @RequestParam("nullWbs") Boolean nullWbs, @RequestParam("nullUser") Boolean nullUser, @RequestParam("nullRsrc") Boolean nullRsrc,
                        @RequestParam("feedbackStatus") List<Integer> feedbackStatus, @RequestParam("status") List<String> status, @RequestParam("children") Boolean children, @RequestParam("onlyTask") Boolean onlyTask,
                        @RequestParam("fuzzySearch") Boolean fuzzySearch, @RequestParam("endDateLimitBefore2") String endDateLimitBefore2, @RequestParam("endDateLimitAfter2") String endDateLimitAfter2,
                        @RequestParam("isFeedback") Integer isFeedback,@RequestParam("planType") String planType,
                        @RequestParam("custom01") String custom01,
                        @RequestParam("custom02") String custom02,
                        @RequestParam("custom03") String custom03,
                        @RequestParam("custom04") String custom04,
                        @RequestParam("custom05") String custom05,
                        @RequestParam("custom06") String custom06,
                        @RequestParam("custom07") String custom07,
                        @RequestParam("custom08") String custom08,
                        @RequestParam("custom09") String custom09,
                        @RequestParam("custom10") String custom10
                        );


    /**
     * 增加任务F
     *
     * @param taskAddForm 任务对象
     * @return PlanTaskTreeVo
     */

    default PlanTaskTreeVo addTask(PlanTaskAddForm taskAddForm){
        if(ObjectUtils.isEmpty(taskAddForm.getTaskName())){
            throw new BaseException("名称不能为空!");
        }
        if(ObjectUtils.isEmpty(taskAddForm.getProjectId())){
            throw new BaseException("项目不能为空");
        }
        if(ObjectUtils.isEmpty(taskAddForm.getOrgId())){
            throw new BaseException("责任主体不能为空");
        }
        if(ObjectUtils.isEmpty(taskAddForm.getCalendarId())){
            throw new BaseException("日历不能为空!");
        }
        ApiResult<PlanTaskTreeVo> apiResult = this.addTask_(taskAddForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @PostMapping(value = "/task/add")
    @AddLog(title = "增加任务" , module = LoggerModuleEnum.PM_TASK,initContent = true)
    ApiResult<PlanTaskTreeVo> addTask_(@RequestBody PlanTaskAddForm taskAddForm);


    /**
     * 更新任务
     *
     * @param taskUpdateForm 任务对象
     * @return PlanTaskTreeVo
     */
    default ApiResult updateTask(PlanTaskUpdateForm taskUpdateForm){
        if(ObjectUtils.isEmpty(taskUpdateForm.getTaskCode())){
            throw new BaseException("代码不能为空!");
        }
        if(ObjectUtils.isEmpty(taskUpdateForm.getTaskName())){
            throw new BaseException("名称不能为空!");
        }
        if(ObjectUtils.isEmpty(taskUpdateForm.getOrgId())){
            throw new BaseException("组织机构不能为空");
        }
        ApiResult<PlanTaskTreeVo> apiResult = this.updateTask_(taskUpdateForm);
        if(apiResult.getStatus() == 200){
            return ApiResult.success(apiResult.getData());
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @PutMapping(value = "/task/update")
    ApiResult updateTask_(@RequestBody PlanTaskUpdateForm taskUpdateForm);


    /**
     * 增加wbs
     *
     * @param wbsAddForm 任务对象
     * @return PlanTaskTreeVo
     */
    default ApiResult addWbs(PlanWbsAddForm wbsAddForm){
        if(ObjectUtils.isEmpty(wbsAddForm.getTaskCode())){
            throw new BaseException("代码不能为空!");
        }
        if(ObjectUtils.isEmpty(wbsAddForm.getProjectId())){
            throw new BaseException("项目不能为空!");
        }
        if(ObjectUtils.isEmpty(wbsAddForm.getDefineId())){
            throw new BaseException("计划定义不能为空!");
        }
        if(ObjectUtils.isEmpty(wbsAddForm.getCalendarId())){
            throw new BaseException("日历不能为空!");
        }
        ApiResult<PlanTaskTreeVo> apiResult = this.addWbs_(wbsAddForm);
        if(apiResult.getStatus() == 200){
            return ApiResult.success(apiResult.getData());
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @PostMapping(value = "/wbs/add")
    @AddLog(title = "增加WBS" , module = LoggerModuleEnum.PM_TASK,initContent = true)
    ApiResult addWbs_(@RequestBody PlanWbsAddForm wbsAddForm);


    default ApiResult updateWbs(PlanWbsUpdateForm wbsUpdateForm){
        if(ObjectUtils.isEmpty(wbsUpdateForm.getTaskCode())){
            throw new BaseException("代码不能为空!");
        }
        if(ObjectUtils.isEmpty(wbsUpdateForm.getTaskName())){
            throw new BaseException("名称不能为空!");
        }
        ApiResult<PlanTaskTreeVo> apiResult = this.updateWbs_(wbsUpdateForm);
        if(apiResult.getStatus() == 200){
            return ApiResult.success(apiResult.getData());
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    /**
     * 更新wbs
     *
     * @param wbsUpdateForm 任务对象
     * @return PlanTaskTreeVo
     */
    @PutMapping(value = "/wbs/update")
    ApiResult updateWbs_(@RequestBody PlanWbsUpdateForm wbsUpdateForm);



    /**
     * 获取任务信息
     *
     * @param taskId taskId
     * @return PlanTaskVo
     */

    default PlanTaskVo taskInfo(String taskId){
        ApiResult<PlanTaskVo> apiResult = this.taskInfo_(taskId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping(value = "/task/{taskId}/info")
    ApiResult<PlanTaskVo> taskInfo_(@PathVariable(name = "taskId") String taskId);


    /**
     * 获取WBS信息
     *
     * @param taskId taskId
     * @return PlanTaskVo
     */
    default ApiResult wbsIfo(String taskId){
        ApiResult<PlanTaskVo> apiResult = this.wbsIfo_(taskId);
        if(apiResult.getStatus() == 200){
            return ApiResult.success(apiResult.getData());
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }


    @GetMapping(value = "/wbs/{wbsId}/info")
    ApiResult wbsIfo_(@PathVariable(name = "wbsId") String taskId);

    /**
     * 删除任务
     * @param taskId id
     * @return s
     */

    default void deleteTask(Integer taskId){
        this.deleteTask_(taskId);
    }


    @DeleteMapping(value = "/task/{taskId}/delete")
    @AddLog(title = "删除任务" , module = LoggerModuleEnum.PM_TASK)
    ApiResult deleteTask_(@PathVariable("taskId") Integer taskId);


    /**
     * 发布任务
     *
     * @param projectId
     * @param taskIds
     * @return
     */

    default void release(Integer projectId, List<Integer> taskIds){
        this.release_(projectId,taskIds);
    }

    @PutMapping(value = "/task/{projectId}/release")
    @AddLog(title = "发布任务" , module = LoggerModuleEnum.PM_TASK)
    ApiResult release_(@PathVariable(name = "projectId") Integer projectId, @RequestBody List<Integer> taskIds);


    /**
     * 取消发布任务
     *
     * @param projectId
     * @param taskIds
     * @return
     */
    default void cancelRelease(Integer projectId, List<Integer> taskIds){
        this.cancelRelease_(projectId,taskIds);
    }


    @PutMapping(value = "/task/{projectId}/cancelRelease")
    @AddLog(title = "取消发布任务" , module = LoggerModuleEnum.PM_TASK)
    ApiResult cancelRelease_(@PathVariable(name = "projectId") Integer projectId, @RequestBody List<Integer> taskIds);


    /**
     * 确认任务
     *
     * @param projectId
     * @param taskIds
     * @return
     */
    default void confirm(Integer projectId, List<Integer> taskIds){
        this.confirm_(projectId,taskIds);
    }


    @PutMapping(value = "/task/{projectId}/confirm")
    @AddLog(title = "确认任务" , module = LoggerModuleEnum.PM_TASK)
    ApiResult confirm_(@PathVariable(name = "projectId") Integer projectId, @RequestBody List<Integer> taskIds);


    /**
     * 取消确认任务
     *
     * @param projectId
     * @param taskIds
     * @return
     */
    default void cancelConfirm(Integer projectId, List<Integer> taskIds){
        this.cancelRelease_(projectId,taskIds);
    }


    @PutMapping(value = "/task/{projectId}/cancelConfirm")
    @AddLog(title = "取消确认任务" , module = LoggerModuleEnum.PM_TASK)
    ApiResult cancelConfirm_(@PathVariable(name = "projectId") Integer projectId, @RequestBody List<Integer> taskIds);


    /**
     * 发布任务树形列表
     *
     * @param defineIds
     * @return
     */
    default List<PlanTaskCollaborationVo> releaseTree(Integer defineIds){
        ApiResult<List<PlanTaskCollaborationVo>> apiResult = this.releaseTree_(defineIds);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping(value = "/task/define/{defineIds}/release/tree")
    ApiResult<List<PlanTaskCollaborationVo>> releaseTree_(@PathVariable(name = "defineIds") Integer defineIds);


    /**
     * 取消发布任务树形列表
     *
     * @param defineId
     * @return
     */
    default List<PlanTaskCollaborationVo> cancelReleaseTree(Integer defineId){
        ApiResult<List<PlanTaskCollaborationVo>> apiResult = this.cancelReleaseTree_(defineId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping(value = "/task/define/{defineId}/cancelRelease/tree")
    ApiResult<List<PlanTaskCollaborationVo>> cancelReleaseTree_(@PathVariable(name = "defineId") Integer defineId);

    /**
     * 确认任务树形列表
     *
     * @param projectId
     * @return
     */
    default List<PlanTaskCollaborationVo> confirmTree(Integer projectId){
        ApiResult<List<PlanTaskCollaborationVo>> apiResult = this.confirmTree_(projectId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping(value = "/task/{projectId}/confirm/tree")
    ApiResult confirmTree_(@PathVariable(name = "projectId") Integer projectId);


    /**
     * 取消确认任务树形列表
     *
     * @param projectId
     * @return
     */
    default List<PlanTaskCollaborationVo> cancelConfirmTree(Integer projectId){
        ApiResult<List<PlanTaskCollaborationVo>> apiResult = this.cancelConfirmTree_(projectId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping(value = "/task/{projectId}/cancelConfirm/tree")
    ApiResult cancelConfirmTree_(@PathVariable(name = "projectId") Integer projectId);

    /**
     * 选择任务弹窗页面接口
     *
     * @param projectId
     * @return
     */
    default  List<PlanTaskCheckVo> queryCheckTaskTree(Integer projectId){
        ApiResult<List<PlanTaskCheckVo>> apiResult = this.queryCheckTaskTree_(projectId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping(value = "/task/{projectId}/check/tree")
    ApiResult queryCheckTaskTree_(@PathVariable(name = "projectId") Integer projectId);


    /**
     * 流程处理（计划）
     * @param ids
     * @return
     */
    default List<PlanTaskTreeVo> queryTaskWfTree(List<Integer> ids){
        ApiResult<List<PlanTaskTreeVo>> apiResult = this.queryTaskWfTree_(ids);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping("/task/ids/{ids}/tree")
    ApiResult queryTaskWfTree_(@PathVariable("ids") List<Integer> ids);


    /**
     * 我的任务(首页)
     * @param myTaskSearchForm
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    default PageInfo<MyTaskVo> queryMyTask(MyTaskSearchForm myTaskSearchForm, Integer pageSize, Integer currentPageNum){
        ApiResult<PageInfo<MyTaskVo>> apiResult = this.queryMyTask_(myTaskSearchForm,pageSize,currentPageNum);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @PostMapping("/task/{pageSize}/{currentPageNum}/home/mytask")
    ApiResult queryMyTask_(@RequestBody MyTaskSearchForm myTaskSearchForm, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum);



    /**
     * 我的预警（超期未完成）
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    default PageInfo<MyWarningVo> queryOverDueMyWarning(MyWarningSearchForm myWarningSearchForm, Integer pageSize, Integer currentPageNum){
        ApiResult<PageInfo<MyWarningVo>> apiResult = this.queryOverDueMyWarning_(myWarningSearchForm,pageSize,currentPageNum);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @PostMapping("/task/{pageSize}/{currentPageNum}/home/mywarning/overdue")
    ApiResult queryOverDueMyWarning_(@RequestBody MyWarningSearchForm myWarningSearchForm, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum);



    /**
     * 我的预警（即将开始）
     * @param pageSize
     * @param currentPage
     * @return
     */
    default PageInfo<MyWarningVo> queryBeginMyWarning(MyWarningSearchForm myWarningSearchForm, Integer pageSize, Integer currentPage){
        ApiResult<PageInfo<MyWarningVo>> apiResult = this.queryBeginMyWarning_(myWarningSearchForm,pageSize,currentPage);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @PostMapping("/task/{pageSize}/{currentPageNum}/home/mywarning/begin")
    ApiResult queryBeginMyWarning_(@RequestBody MyWarningSearchForm myWarningSearchForm, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum);


    /**
     * 我的预警（即将完成）
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    default PageInfo<MyWarningVo> queryCompleteMyWarning(MyWarningSearchForm myWarningSearchForm, Integer pageSize, Integer currentPageNum){
        ApiResult<PageInfo<MyWarningVo>> apiResult = this.queryCompleteMyWarning_(myWarningSearchForm,pageSize,currentPageNum);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @PostMapping("/task/{pageSize}/{currentPageNum}/home/mywarning/complete")
    ApiResult queryCompleteMyWarning_(@RequestBody MyWarningSearchForm myWarningSearchForm, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum);


    default PlanTaskAddInitVo getAddInitData(Integer defineId, Integer parentId){
        ApiResult<PlanTaskAddInitVo> apiResult = this.getAddInitData_(defineId,parentId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping("/task/{defineId}/{parentId}/add/init/info")
    ApiResult getAddInitData_(@PathVariable("defineId") Integer defineId, @PathVariable("parentId") Integer parentId);


    /**
     * 计算用户的任务按钮权限
     *
     * @param defineId
     * @param taskId
     * @return
     */
    default PlanTaskAuthVo getEditAuth(Integer defineId, Integer taskId){
        ApiResult<PlanTaskAuthVo> apiResult = this.getEditAuth_(defineId,taskId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @GetMapping("/task/{defineId}/{taskId}/edit/auth")
    ApiResult getEditAuth_(@PathVariable("defineId") Integer defineId, @PathVariable("taskId") Integer taskId);


    /**
     * 模拟进度计划进度计算
     * @param calculateForm
     * @return
     */
    default List<PlanTaskTreeVo> simulationCalcCalculateList(CalculateForm calculateForm){
        ApiResult<List<PlanTaskTreeVo>> apiResult = this.simulationCalcCalculateList_(calculateForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }

    @PostMapping("/task/calc/simulation/calculate")
    @AddLog(title = "模拟计算" , module = LoggerModuleEnum.PM_TASK)
    ApiResult simulationCalcCalculateList_(@RequestBody CalculateForm calculateForm);


    /**
     * 计划进度计算
     * @param calculateForm
     * @return
     */
    default PlanTaskTreeVo saveCalcCalculateList(CalculateForm calculateForm){
        ApiResult<PlanTaskTreeVo> apiResult = this.saveCalcCalculateList_(calculateForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }else{
            throw new BaseException(apiResult.getMessage());
        }
    }


    @PostMapping("/task/calc/calculate")
    @AddLog(title = "进度计算" , module = LoggerModuleEnum.PM_TASK)
    ApiResult<PlanTaskTreeVo> saveCalcCalculateList_(@RequestBody CalculateForm calculateForm);

    /**
     *  工期计算
     * @return
     */
    default TimeDrtnVo caculateWorkHour(TimeDrtnCalcForm form){
        ApiResult<TimeDrtnVo> apiResult = this.caculateWorkHour_(form);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PostMapping("/task/caculate/workHour")
    ApiResult<TimeDrtnVo> caculateWorkHour_(@RequestBody TimeDrtnCalcForm form);


    /**
     * 根据计划id判断该计划下是否存在wbs或任务
     * @param defineId
     * @return
     */
    default boolean isHasTaskByDefineId(Integer defineId){
        ApiResult<Boolean> apiResult =  this.isHasTaskByDefineId_(defineId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return false;
    }

    @GetMapping("/task/{defineId}/hastask")
    ApiResult isHasTaskByDefineId_(@PathVariable("defineId") Integer defineId);

    /**
     * 根据月度任务的id获取总体施组任务id
     * @param taskId
     * @return
     */
    @GetMapping(value = "/task/overall/taskId/{taskId}")
    ApiResult<Integer> getOverallTaskIdByTaskIdFeign(@PathVariable(name = "taskId") Integer taskId);

    default Integer getOverallTaskIdByTaskId(Integer taskId){
        ApiResult<Integer> apiResult =  this.getOverallTaskIdByTaskIdFeign(taskId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

}
