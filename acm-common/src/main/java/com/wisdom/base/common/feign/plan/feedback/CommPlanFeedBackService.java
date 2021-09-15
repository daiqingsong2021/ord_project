package com.wisdom.base.common.feign.plan.feedback;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.form.plan.feedback.PlanFeedBackForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.plan.feedback.PlanFeedBackVo;
import com.wisdom.base.common.vo.plan.feedback.PlanTaskFeedBackTreeVo;
import com.wisdom.base.common.vo.plan.feedback.PlanTaskFeedbackListVo;
import com.wisdom.base.common.vo.plan.feedback.PlanTaskFeedbackReleaseTreeVo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: szc
 * @Date: 2019/7/8 20:53
 * @Version 1.0
 */
@FeignClient(value = "acm-module-plan",configuration = FeignConfiguration.class)
public interface CommPlanFeedBackService {

    /**
     *  根据任务id查找所有进展日志
     * @param taskId
     * @return
     */
    default  List<PlanFeedBackVo> queryPlanFeedBack(Integer taskId){
        ApiResult< List<PlanFeedBackVo>> apiResult = this.queryPlanFeedBack_(taskId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping(value="/feedback/task/{taskId}/list")
    ApiResult queryPlanFeedBack_(@PathVariable("taskId") Integer taskId);


    /**
     *  根据任务id查找已发布的进展日志
     * @param taskId
     * @return
     */
    default List<PlanFeedBackVo> queryReleasePlanFeedBack(Integer taskId){
        ApiResult<List<PlanFeedBackVo>> apiResult = this.queryReleasePlanFeedBack_(taskId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping(value="/feedback/task/{taskId}/release/list")
    ApiResult queryReleasePlanFeedBack_(@PathVariable("taskId") Integer taskId);


    /**
     * 反馈页签
     * @param taskId
     * @return
     */
    default PlanFeedBackVo queryPlanFeedBackBackByTaskId(Integer taskId){
        ApiResult<PlanFeedBackVo> apiResult = this.queryPlanFeedBackBackByTaskId_(taskId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("feedback/task/{taskId}/info")
    ApiResult queryPlanFeedBackBackByTaskId_(@PathVariable("taskId") Integer taskId);


    /**
     * 反馈流程页签
     * @param feedbackId
     * @return
     */
    default PlanFeedBackVo queryPlanProcessFeedBackBackByTaskId(Integer feedbackId){
        ApiResult<PlanFeedBackVo> apiResult = this.queryPlanProcessFeedBackBackByTaskId_(feedbackId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("feedback/process/task/{feedbackId}/info")
    ApiResult<PlanFeedBackVo> queryPlanProcessFeedBackBackByTaskId_(@PathVariable("feedbackId") Integer feedbackId);


    /**
     *  保存或增加进展日志
     * @param planFeedBackForm
     * @return
     */
    default PlanTaskFeedBackTreeVo addPlanFeedBack(PlanFeedBackForm planFeedBackForm){
        ApiResult apiResult = this.addPlanFeedBack_(planFeedBackForm);
        if(apiResult.getStatus() == 200){
            PlanTaskFeedBackTreeVo ret = (PlanTaskFeedBackTreeVo)apiResult.getData();
            return ret;
        }
        return null;
    }

    @PostMapping(value = "/feedback/add")
    ApiResult addPlanFeedBack_(@RequestBody PlanFeedBackForm planFeedBackForm);


    /**
     *  删除进展日志
     * @param
     * @return
     */
    default PlanTaskFeedBackTreeVo deletePlanFeedBack(Integer id){
        ApiResult<PlanTaskFeedBackTreeVo> apiResult = this.deletePlanFeedBack_(id);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @DeleteMapping(value = "/feedback/{id}/delete")
    ApiResult deletePlanFeedBack_(@PathVariable("id") Integer id);


    /**
     * 根据 计划id获取计划反馈树
     * @return
     */
    default List<PlanTaskFeedBackTreeVo> queryPlanFeedBackTree(List<Integer> defineIds){
        ApiResult<List<PlanTaskFeedBackTreeVo>> apiResult = this.queryPlanFeedBackTree_(defineIds);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/feedback/{defineIds}/tree")
    ApiResult queryPlanFeedBackTree_(@PathVariable(value = "defineIds") List<Integer> defineIds);


    /**
     * 根据计划id获取计划反馈列表
     * @param defineIds
     * @return
     */
    default List<PlanTaskFeedbackListVo> queryPlanFeedBackList(List<Integer> defineIds){
        ApiResult<List<PlanTaskFeedbackListVo>> apiResult = this.queryPlanFeedBackList_(defineIds);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/feedback/{defineIds}/list")
    ApiResult queryPlanFeedBackList_(@PathVariable(value = "defineIds") List<Integer> defineIds);


    /**
     * 根据计划id获取进展审批树
     * @param defineIds
     * @return
     */
    default List<PlanTaskFeedbackReleaseTreeVo> queryPlanFeedbackReleaseTree(List<Integer> defineIds){
        ApiResult<List<PlanTaskFeedbackReleaseTreeVo>> apiResult = this.queryPlanFeedbackReleaseTree_(defineIds);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/feedback/{defineIds}/release/tree")
    ApiResult queryPlanFeedbackReleaseTree_(@PathVariable(value = "defineIds") List<Integer> defineIds);


    /**
     * 反馈表批准
     *
     * @param feedbackIds
     * @return
     */
    default void releasePlanFeedback(List<Integer> feedbackIds){
        this.releasePlanFeedback_(feedbackIds);
    }

    @PutMapping("/feedback/release")
    ApiResult<List<Integer>> releasePlanFeedback_(@RequestBody List<Integer> feedbackIds);

    /**
     * 汇总
     * @param defineIds
     * @return
     */
    default void planSummaryByDefineIds(List<Integer> defineIds){
        ApiResult apiResult = this.planSummaryByDefineIds_(defineIds);
        if(apiResult.getStatus() != 200){
            throw new BaseException(apiResult.getMessage());
        }
    }

    @PutMapping("/feedback/summary")
    ApiResult planSummaryByDefineIds_(@RequestBody List<Integer> defineIds);


    /**
     * 根据任务id取消反馈批准
     * @param taskId
     * @return
     */
    default PlanTaskFeedBackTreeVo cancleReleasePlanFeedback(Integer taskId){
        ApiResult<PlanTaskFeedBackTreeVo> apiResult = this.cancleReleasePlanFeedback_(taskId);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @PostMapping("/feedback/{taskId}/cancleRelease")
    ApiResult<PlanTaskFeedBackTreeVo> cancleReleasePlanFeedback_(@PathVariable("taskId") Integer taskId);


    /**
     * 根据进展反馈ID集合获取计划反馈树形
     *
     * @param feedbackIds
     * @return
     */
    default List<PlanTaskFeedBackTreeVo> queryWfPlanFeedbackTreeListVo(List<Integer> feedbackIds){
        ApiResult<List<PlanTaskFeedBackTreeVo>> apiResult = this.queryWfPlanFeedbackTreeListVo_(feedbackIds);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/feedback/ids/{ids}/tree")
    ApiResult queryWfPlanFeedbackTreeListVo_(@PathVariable("ids") List<Integer> feedbackIds);


    /**
     * 根据taskid获取反馈id集合
     * @param id
     * @return
     */
    default List<PlanFeedBackVo> getFeedbackWorkflowByIds(Integer id){
        ApiResult<List<PlanFeedBackVo>> apiResult = this.getFeedbackWorkflowByIds_(id);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    @GetMapping("/feedback/workflow/{id}/list")
    ApiResult getFeedbackWorkflowByIds_(@PathVariable("id") Integer id);


    /**
     * 导入计划
     * @param request
     * @param defineId 定义ID
     * @return
     */
//    default void importPlan(HttpServletRequest request, Integer defineId){
//        this.importPlan_(request,defineId);
//    }
//
//    @PostMapping("/feedback/file/import")
//    ApiResult importPlan_(HttpServletRequest request, Integer defineId);
}
