package com.wisdom.base.common.feign;


import com.github.pagehelper.PageInfo;
import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.form.*;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.ActModelVo;
import com.wisdom.base.common.vo.wf.*;
import org.assertj.core.util.Lists;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@FeignClient(value = "acm-module-activiti", configuration = FeignConfiguration.class)
public interface CommActivitiService {

    /**
     * zll
     * for activiti
     * @param activitiIds
     * @return
     */
    default Map<String, String> queryActByIds(List<String> activitiIds) {
        ApiResult<Map<String, String>>  apiResult = this.queryActivitiByIds_(activitiIds);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /*
     * zll根据流程节点id查询 for activiti
     * @return    , consumes = MediaType.APPLICATION_JSON_VALUE
     */
    @RequestMapping(value = "/activitiTemplate/queryActivitiByIds", method = RequestMethod.POST)
    public ApiResult<Map<String, String>> queryActivitiByIds_(@RequestBody List<String> activitiIds);

    /**
     * 首节点发起工作流选择后续参与者
     *
     * @param procDefKey 流程定义KEY
     * @return 后续参与者
     */
    @RequestMapping(value = "/activiti/process/start/next/{procDefKey}/candidate", method = RequestMethod.POST)
    public ApiResult<WfCandidateVo> getStartNextCandidate(@PathVariable("procDefKey") String procDefKey, @RequestBody Map<String, Object> vars);

    /**
     * 得到后续活动参与者
     *
     * @param taskId 任务ID
     * @return 后续参与者
     */
    @RequestMapping(value = "/activiti/process/next/{taskId}/candidate", method = RequestMethod.GET)
    public ApiResult<WfCandidateVo> getNextCandidate(@PathVariable("taskId") String taskId);

    /**
     * 得到能驳回的活动
     *
     * @param taskId 任务ID
     * @return 后续参与者
     */
    @RequestMapping(value = "/activiti/process/activity/{taskId}/reject", method = RequestMethod.GET)
    public ApiResult<List<WfActivityVo>> getRejectActivity(@PathVariable("taskId") String taskId);

    /**
     * 发起流程
     *
     * @param form 表单
     * @return 流程运行实例
     */
    @RequestMapping(value = "/activiti/process/start", method = RequestMethod.POST)
    public ApiResult<WfRunProcessVo> startProcess(@RequestBody WfStartProcessForm form);

    /**
     * 认领任务
     *
     * @param form 表单
     * @return 认领Vo
     */
    @RequestMapping(value = "/activiti/process/task/claim", method = RequestMethod.POST)
    public ApiResult<WfClaimVo> claimTask(@RequestBody WfClaimTaskForm form);

    /**
     * 完成任务
     *
     * @param form 表单
     * @return 流程运行Vo
     */
    @RequestMapping(value = "/activiti/process/task/complete", method = RequestMethod.POST)
    public ApiResult<WfRunProcessVo> completeTask(@RequestBody WfCompleteTaskForm form);

    /**
     * 取消任务
     *
     * @param form 表单
     * @return 流程运行Vo
     */
    @RequestMapping(value = "/activiti/process/task/cancel", method = RequestMethod.POST)
    public ApiResult<WfRunProcessVo> cancelTask(@RequestBody WfRejectTaskForm form);

    /**
     * 驳回任务
     *
     * @param form 表单
     * @return 流程运行Vo
     */
    @RequestMapping(value = "/activiti/process/task/reject", method = RequestMethod.POST)
    public ApiResult<WfRunProcessVo> rejectTask(@RequestBody WfRejectTaskForm form);

    /**
     * 终止流程
     *
     * @param form 表单
     * @return ApiResult
     */
    @RequestMapping(value = "/activiti/process/terminate", method = RequestMethod.POST)
    public ApiResult terminateProcess(@RequestBody WfTerminateTaskForm form);

    /**
     * 删除流程
     *
     * @param procInstId 流程实例id
     * @return ApiResult
     */
    @RequestMapping(value = "/activiti/process/{procInstId}/delete", method = RequestMethod.DELETE)
    public ApiResult deleteProcess(@PathVariable("procInstId") String procInstId);

    /**
     * 我的待办数量
     *
     * @param userId 用户ID
     * @return ApiResult
     */
    @RequestMapping(value = "/activiti/{userId}/unfinish/task/count", method = RequestMethod.GET)
    public ApiResult<Long> queryWfUnfinishCount(@PathVariable("userId") String userId);


    /**
     * 我的待办
     *
     * @param userId 用户ID
     * @return ApiResult
     */
    @RequestMapping(value = "/activiti/unfinish/task/{userId}/{pageSize}/{pageNum}/list", method = RequestMethod.POST)
    public ApiResult<PageInfo<MyUnFinishTaskVo>> queryMyUnfinishTasks(@PathVariable("userId") String userId, @PathVariable("pageSize") Integer pageSize,
                                                                      @PathVariable("pageNum") Integer pageNum, @RequestBody WfUnFinishSearchForm search);

    /**
     * 我的已办
     *
     * @param userId 用户ID
     * @return ApiResult
     */
    @RequestMapping(value = "/activiti/finish/task/{userId}/{pageSize}/{pageNum}/list", method = RequestMethod.POST)
    public ApiResult<PageInfo<MyFinishTaskVo>> queryMyFinishTasks(@PathVariable("userId") String userId, @PathVariable("pageSize") Integer pageSize,
                                                                  @PathVariable("pageNum") Integer pageNum, @RequestBody WfFinishSearchForm search);

    /**
     * 我发起的
     *
     * @param userId 用户ID
     * @return ApiResult
     */
    @RequestMapping(value = "/activiti/mine/task/{userId}/{pageSize}/{pageNum}/list", method = RequestMethod.POST)
    public ApiResult<PageInfo<WfMineTaskVo>> queryMineTasks(@PathVariable("userId") String userId, @PathVariable("pageSize") int pageSize,
                                                            @PathVariable("pageNum") int pageNum, @RequestBody WfMineSearchForm search);

    /**
     * 根据流程实例id获取流程处理日志信息集合
     *
     * @param procInstIds 流程实例id
     * @return ApiResult
     */
    @RequestMapping(value = "/activiti/process/handle/log/list", method = RequestMethod.POST)
    public ApiResult<List<WfProcLogVo>> queryProcessHandleLogList(@RequestBody List<String> procInstIds);

    /**
     * 流程进展日志
     *
     * @param procInstId 流程实例id
     * @return
     */
    @RequestMapping(value = "/activiti/process/handle/log/detail", method = RequestMethod.POST)
    public ApiResult<WfLogDetailVo> queryProcessHandleLogDetail(@RequestBody String procInstId);

    /**
     * 过活动定义ID查询作务信息
     *
     * @param procInstId
     * @param actDefId
     * @return ApiResult
     */
    @GetMapping(value = "/activiti/process/{procInstId}/{actDefId}/task/list")
    public ApiResult<List<WfTaskVo>> getTaskByActDefId(@PathVariable("procInstId") String procInstId, @PathVariable("actDefId") String actDefId);

    /**
     * 获取流程模版id
     *
     * @return ApiResult
     */
    @PostMapping(value = "/activiti/process/new/model")
    public ApiResult addActivityNewModel(@RequestBody ActModelAddForm actModelAddForm);

    /**
     * 获取流程模版数据
     *
     * @param modelIds
     * @return ApiResult
     */
    @PostMapping(value = "/activiti/process/assign/models")
    public ApiResult<List<ActModelVo>> queryWfAssignList(@RequestBody List<String> modelIds);

    /**
     * 发布流程模版
     *
     * @return ApiResult
     */
    @PostMapping(value = "/activiti/process/release/{modelId}/model")
    public ApiResult releaseActivityNewModel(@PathVariable("modelId") String modelId);

    /**
     * 删除流程模版
     *
     * @return ApiResult
     */
    @PostMapping(value = "/activiti/process/delete/{modelId}/model")
    public ApiResult deleteActivityNewModel(@PathVariable("modelId") String modelId);

    /**
     * 获取流程模版数据
     *
     * @param modelIds
     * @return ApiResult
     */
    @PostMapping(value = "/activiti/process/assign/procdef")
    public ApiResult<List<WfProcessDefVo>> queryProcDefByModulIdList(@RequestBody List<String> modelIds);

    /**
     * 根据流程实例Id获取流程活动实例
     *
     * @param processInstanceId 流程实例id
     * @return 流程实例
     */
    @GetMapping(value = "/activiti/process/{processInstanceId}/processInstance")
    public ApiResult<WfProcessInstVo> getProcessInstanceById(@PathVariable("processInstanceId") String processInstanceId);

    /**
     * 根据活动实例Id获取流程活动实例
     *
     * @param activityInstanceId 活动实例id
     * @return 流程活动实例
     */
    @GetMapping(value = "/activiti/process/{activityInstanceId}/activityInstance")
    public ApiResult<WfActivityInstanceVo> getActivityInstanceByActivityInstanceId(@PathVariable("activityInstanceId") String activityInstanceId);

    /**
     * 根据流程实例Id获取流程活动实例
     *
     * @param processInstanceId 流程实例id
     * @return 流程活动实例
     */
    @GetMapping(value = "/activiti/process/{processInstanceId}/activityInstance/list")
    public ApiResult<List<WfActivityInstanceVo>> getActivityInstanceByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId);

    /**
     * 根据userId和taskId获取工作项
     *
     * @return 任务
     */
    @GetMapping(value = "/activiti/process/{taskId}/task")
    public ApiResult<WfTaskVo> getTaskByTaskId(@PathVariable("taskId") String taskId);

    /**
     * 根据流程实例获取运行中的任务
     * @param processInstanceId 流程实例
     * @return
     */
    @GetMapping(value = "/activiti/process/{processInstanceId}/run/task/list")
    public ApiResult<List<WfTaskVo>> getRunTaskByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId);

    /**
     * 根据流程实例获取任务
     * @param processInstanceId 流程实例
     * @return
     */
    @GetMapping(value = "/activiti/process/{processInstanceId}/task/list")
    public ApiResult<List<WfTaskVo>> getTaskByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId);

    /**
     * 根据流程实例和taskId获取工作项
     *
     * @return 任务
     */
    @GetMapping(value = "/activiti/process/{processInstanceId}/user/{userId}/run/task/list")
    public ApiResult<List<WfRunTaskVo>> getRunTaskByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId, @PathVariable("userId") String userId);


    /**
     * 得到任务参与者
     * @param taskId
     * @return
     */
    @GetMapping(value = "/activiti/process/{taskId}/candidate/list")
    public ApiResult<List<String>> getTaskCandidates(@PathVariable("taskId") String taskId);
}
