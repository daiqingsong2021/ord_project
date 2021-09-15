package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.tmpltask.BaseTmplTaskAddForm;
import com.wisdom.acm.base.form.tmpltask.BaseTmplTaskUpdateForm;
import com.wisdom.acm.base.form.tmpltask.TaskTmplAddForm;
import com.wisdom.acm.base.po.BaseTmplPlanPo;
import com.wisdom.acm.base.po.BaseTmplTaskPo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskTreeVo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.UserInfo;

import java.util.List;

public interface BaseTmplTaskService extends CommService<BaseTmplTaskPo> {

    /**
     * 计划模板树形结构
     * @return
     */
    public List<BaseTmplTaskTreeVo> queryTmpltaskTreeList();

    /**
     * 增加任务
     * @param baseTmpltaskAddForm
     * @return
     */
    public BaseTmplTaskPo addTmplTask(BaseTmplTaskAddForm baseTmpltaskAddForm);

    /**
     * 增加WBS任务
     * @param baseTmpltaskAddForm
     * @return
     */
    public BaseTmplTaskPo addTmplWbs(BaseTmplTaskAddForm baseTmpltaskAddForm);

    /**
     * 查询WBS任务基本信息
     * @param TmpltaskId
     * @return
     */
    public BaseTmplTaskVo getTmplTaskById(Integer TmpltaskId);

    /**
     * 修改WBS任务
     * @param baseTmpltaskUpdateForm
     * @return
     */
    public BaseTmplTaskPo updateTmplTask(BaseTmplTaskUpdateForm baseTmpltaskUpdateForm);

    /**
     * 删除数据wbs或任务
     * @param ids
     */
    void deleteBaseTmplTask(List<Integer> ids);

    /**
     * id计划模板树形结构
     * @param id
     * @return
     */
    public List<BaseTmplTaskTreeVo> queryTmpltaskTreeListByTmplId(Integer id);

    /**
     * 通过tmplId查找
     * @param tmplId
     * @return
     */
    public List<BaseTmplTaskVo> queryTmplTaskByTmplId(Integer tmplId);

    /**
     * 通过BaseTmplTaskPoId删除
     * @param id
     */
    public void deleteByBaseTmplTaskPoId(Integer id);

    /**
     * 删除计划模板
     * @param ids
     */
    public void deleteBaseTmplPlan(List<Integer> ids);


    /**
     * 通过Id删除wbs或task
     * @param id
     */
    public void deleteTmplWbsByWbsId(Integer id);

    /**
     * 通过id删除计划模板
     * @param id
     */
    public void deleteTmplPlanById(Integer id);

    /**
     * 修改wbs
     * @param baseTmpltaskUpdateForm
     * @return
     */
    public BaseTmplTaskPo updateTmplWbs(BaseTmplTaskUpdateForm baseTmpltaskUpdateForm);

    void savePlanTaskTmplByDefineId(TaskTmplAddForm taskTmplAddForm, UserInfo userInfo);
}
