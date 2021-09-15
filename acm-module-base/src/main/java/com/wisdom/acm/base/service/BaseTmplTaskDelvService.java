package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.tmpltask.BaseTmplTaskDelvUpdateForm;
import com.wisdom.acm.base.po.BaseTmplTaskDelvPo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskDelvVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.plan.task.PlanTmplTaskDelvForm;

import java.util.List;

public interface BaseTmplTaskDelvService extends CommService<BaseTmplTaskDelvPo> {

    /**
     * 根据任务ID查询交付物清单
     * @param taskId
     * @return
     */
    List<BaseTmplTaskDelvVo> queryTmplTaskDelvListByTaskId(Integer taskId);

    /**
     * 根据任务ID查询交付物清单
     * @param tmplId
     * @return
     */
    List<BaseTmplTaskDelvVo> queryTmplTaskDelvListByTmplId(Integer tmplId);

    /**
     * 分配交付物
     * @param
     * @return
     */
    public List<BaseTmplTaskDelvVo> assignTmplTaskDelv(Integer taskId, List<Integer> delvIds);


    void addPlanTmplTaskDelv(List<BaseTmplTaskDelvPo> baseTmplTaskDelvPoList);

    /**
     * 修改交付物
     * @param baseTmplTaskDelvUpdateForm
     * @return
     */
    public BaseTmplTaskDelvPo updateTmplTaskDelv(BaseTmplTaskDelvUpdateForm baseTmplTaskDelvUpdateForm);

    /**
     * 删除交付清单
     * @param ids
     */
    public void deleteBaseTmplTaskDelv(List<Integer> ids);

    /**
     * 根据主键查询基本信息
     * @param id
     * @return
     */
    public BaseTmplTaskDelvVo queryBaseTmplTaskDelvInfo(Integer id);

    /**
     *
     * @param ids
     * @return
     */
    String queryBaseTmplTaskDelvByIds(List<Integer> ids);
}
