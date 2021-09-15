package com.wisdom.base.common.feign.plan.change;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.form.plan.change.pred.PlanTaskPredChangeAddForm;
import com.wisdom.base.common.form.plan.change.pred.PlanTaskPredChangeUpdateForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.plan.change.pred.PlanTaskPredChangeAssignTreeVo;
import com.wisdom.base.common.vo.plan.change.pred.PlanTaskPredChangeListVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: szc
 * @Date: 2019/7/9 10:19
 * @Version 1.0
 */
@FeignClient(value = "acm-module-plan",configuration = FeignConfiguration.class)
public interface CommPlanTaskPredChangeService {
    /**
     * 获取变更的紧前任务列表
     * @param bizId
     * @return
     */
    default List<PlanTaskPredChangeListVo> queryPlanTaskPredChangeList(Integer bizId, String bizType){
        ApiResult<List<PlanTaskPredChangeListVo>> apiResult = this.queryPlanTaskPredChangeList_(bizId,bizType);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/task/change/pred/{bizId}/{bizType}/list")
    ApiResult queryPlanTaskPredChangeList_(@PathVariable("bizId") Integer bizId, @PathVariable("bizType") String bizType);

    /**
     * 获取变更的后续任务列表
     * @param bizId
     * @param bizType
     * @return
     */
    default List<PlanTaskPredChangeListVo> queryPlanTaskFllowChangeList(Integer bizId, String bizType){
        ApiResult<List<PlanTaskPredChangeListVo>> apiResult = this.queryPlanTaskFllowChangeList_(bizId,bizType);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/task/change/fllow/{bizId}/{bizType}/list")
    ApiResult queryPlanTaskFllowChangeList_(@PathVariable("bizId") Integer bizId, @PathVariable("bizType") String bizType);


    /**
     * 根据计划定义id获取逻辑变更分配树
     * @param defineId
     * @return
     */
    default List<PlanTaskPredChangeAssignTreeVo> queryPlanTaskPredChangeAssignTree(Integer defineId, Integer taskId, String type){
        ApiResult<List<PlanTaskPredChangeAssignTreeVo>> apiResult = this.queryPlanTaskPredChangeAssignTree_(defineId,taskId,type);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/task/change/{defineId}/{taskId}/{type}/assign/tree")
    ApiResult queryPlanTaskPredChangeAssignTree_(@PathVariable("defineId") Integer defineId, @PathVariable("taskId") Integer taskId, @PathVariable("type") String type);

    /**
     * 增加变更逻辑关系分配
     *
     * @param planTaskPredChangeAddForm
     * @return
     */
    default void addPlanTaskPredChange(PlanTaskPredChangeAddForm planTaskPredChangeAddForm){
        if(ObjectUtils.isEmpty(planTaskPredChangeAddForm.getTaskId())){
            throw new BaseException("任务不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskPredChangeAddForm.getType())){
            throw new BaseException("任务类型不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskPredChangeAddForm.getPredTaskId())){
            throw new BaseException("前置任务不能为空");
        }
        if(ObjectUtils.isEmpty(planTaskPredChangeAddForm.getPredType())){
            throw new BaseException("前置任务类型不能为空");
        }
        this.addPlanTaskPredChange_(planTaskPredChangeAddForm);
    }

    @PostMapping("/task/change/pred/add")
    ApiResult addPlanTaskPredChange_(@RequestBody PlanTaskPredChangeAddForm planTaskPredChangeAddForm);


    /**
     * 修改逻辑变更
     * @param updateForm
     * @return
     */
    default void updatePlanTaskPredChange(PlanTaskPredChangeUpdateForm updateForm){
        this.updatePlanTaskPredChange_(updateForm);
    }

    @PutMapping("/task/change/pred/update")
    ApiResult updatePlanTaskPredChange_(@RequestBody PlanTaskPredChangeUpdateForm updateForm);

    /**
     *
     删除变更逻辑关系分配
     * @param logicId
     * @return
     */
    default void deletePlanTaskPredChange(Integer logicId){
        this.deletePlanTaskPredChange_(logicId);
    }

    @DeleteMapping("/task/change/pred/{logicId}/delete")
    ApiResult deletePlanTaskPredChange_(@PathVariable("logicId") Integer logicId);


    /**
     * 撤销逻辑变更
     * @param logicChangeId
     * @return
     */
    default void canclePlanTaskPredChange(Integer logicChangeId){
        this.canclePlanTaskPredChange_(logicChangeId);
    }

    @PutMapping("/task/change/pred/{logicChangeId}/cancle")
    ApiResult canclePlanTaskPredChange_(@PathVariable("logicChangeId") Integer logicChangeId);
}
