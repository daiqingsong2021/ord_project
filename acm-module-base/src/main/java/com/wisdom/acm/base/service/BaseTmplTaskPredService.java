package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.tmpltask.BaseTmplTaskPredUpdateForm;
import com.wisdom.acm.base.po.BaseTmplTaskPredPo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskPredVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.plan.task.pred.PlanTmplTaskPredForm;

import java.util.List;

public interface BaseTmplTaskPredService extends CommService<BaseTmplTaskPredPo> {

    /**
     * 查询计划模板紧前逻辑关系列表
     * @param tmplId
     * @return
     */
    public List<BaseTmplTaskPredVo> queryTmplTaskPredListByTmplId(Integer tmplId);

    /**
     * 查询计划模板紧前逻辑关系列表
     * @param taskId
     * @return
     */
    public List<BaseTmplTaskPredVo> queryTmplTaskPredList(Integer taskId);

    /**
     * 查询计划模板后续逻辑关系列表
     * @param taskId
     * @return
     */
    public List<BaseTmplTaskPredVo> queryTmplTaskFllowList(Integer taskId);

    /**
     * 分配紧前/后续任务
     * @param
     * @return
     */
    public BaseTmplTaskPredPo assignBaseTmplTaskPred(Integer taskId, Integer predTaskId);


    void addPlanTmplTaskPred(List<BaseTmplTaskPredPo> baseTmplTaskPredPoList);

    /**
     * 修改逻辑关系
     * @param baseTmplTaskPredUpdateForm
     * @return
     */
    public BaseTmplTaskPredPo updateBaseTmplTaskPred(BaseTmplTaskPredUpdateForm baseTmplTaskPredUpdateForm);

    /**
     * 删除逻辑关系
     * @param ids
     */
    public void deleteBaseTmplTaskPred(List<Integer> ids);

    /**
     *
     * @param taskId
     * @param predTaskId
     * @return
     */
    public List<BaseTmplTaskPredVo> queryTmpltaskTreeListByTwoIds(Integer taskId, Integer predTaskId);

    /**
     *
     * @param taskId
     * @param predTaskId
     * @return
     */
    public List<BaseTmplTaskPredVo> queryFollowTmpltaskTreeListByTwoIds(Integer taskId, Integer predTaskId);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public BaseTmplTaskPredVo queryTmplTaskPredById(Integer id);

    String queryDeletePredTaskLogger(List<Integer> ids);
}
