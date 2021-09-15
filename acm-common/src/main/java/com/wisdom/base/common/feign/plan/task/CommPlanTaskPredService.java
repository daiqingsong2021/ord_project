package com.wisdom.base.common.feign.plan.task;

import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.form.plan.task.pred.PlanTaskPredUpdateForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.plan.task.PlanTaskTreeVo;
import com.wisdom.base.common.vo.plan.task.pred.PlanTaskFollowVo;
import com.wisdom.base.common.vo.plan.task.pred.PlanTaskPredVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: szc
 * @Date: 2019/7/9 9:09
 * @Version 1.0
 */
@FeignClient(value = "acm-module-plan",configuration = FeignConfiguration.class)
public interface CommPlanTaskPredService {

    /**
     *  逻辑关系分配弹出层树型
     * @param defineId
     * @return
     */
    default List<PlanTaskTreeVo> planTaskPredTree(Integer defineId){
        ApiResult<List<PlanTaskTreeVo>> apiResult = this.planTaskPredTree_(defineId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/task/{defineId}/pred/assgin/tree")
    ApiResult<List<PlanTaskTreeVo>> planTaskPredTree_(@PathVariable("defineId") Integer defineId);


    /**
     * 获取紧前任务列表
     * @param taskId
     * @return
     */
    default List<PlanTaskPredVo> queryPredList(Integer taskId){
        ApiResult<List<PlanTaskPredVo>> apiResult = this.queryPredList_(taskId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/task/{taskId}/pred/list")
    ApiResult queryPredList_(@PathVariable("taskId") Integer taskId);


    /**
     * 获取后续任务列表
     * @param taskId
     * @return
     */
    default List<PlanTaskFollowVo> queryFollowList(Integer taskId){
        ApiResult<List<PlanTaskFollowVo>> apiResult = this.queryFollowList_(taskId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/task/{taskId}/follow/list")
    ApiResult queryFollowList_(@PathVariable("taskId") Integer taskId);


    /**
     * 添加紧前任务/分配后继任务（反过来传参即是后继任务）
     *
     * @param taskId
     * @param predTaskId
     * @return
     */
    default void add(Integer taskId, Integer predTaskId){
        this.add_(taskId,predTaskId);
    }

    @PostMapping("/task/{taskId}/{predTaskId}/pred/add")
    ApiResult add_(@PathVariable("taskId") Integer taskId, @PathVariable("predTaskId") Integer predTaskId);

    /**
     * 修改紧前任务
     * @param taskPredUpdateForm
     * @return
     */
    default PlanTaskPredVo update(PlanTaskPredUpdateForm taskPredUpdateForm){
        ApiResult<PlanTaskPredVo> apiResult = this.update_(taskPredUpdateForm);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PutMapping("/task/pred/update")
    ApiResult<PlanTaskPredVo> update_(@RequestBody PlanTaskPredUpdateForm taskPredUpdateForm);

    /**
     * 删除紧前任务
     * @param ids
     * @return
     */
    default void deletePred(List<Integer> ids){
        this.deletePred_(ids);
    }

    @DeleteMapping("/task/pred/delete")
    @AddLog(title = "删除逻辑关系" , module = LoggerModuleEnum.PM_TASK)
    ApiResult deletePred_(@RequestBody List<Integer> ids);


}
