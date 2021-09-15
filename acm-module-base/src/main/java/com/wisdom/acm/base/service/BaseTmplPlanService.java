package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.tmpltask.BaseTmplPlanAddForm;
import com.wisdom.acm.base.form.tmpltask.BaseTmplPlanUpdateForm;
import com.wisdom.acm.base.po.BaseTmplPlanPo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplPlanVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.SelectVo;

import java.util.List;

public interface BaseTmplPlanService extends CommService<BaseTmplPlanPo> {

    /**
     * 获取计划模板的数据
     * @return
     */
    public List<BaseTmplPlanVo> queryTmplPlanList();

    /**
     * 增加计划模板
     * @param baseTmplPlanAddForm
     * @return
     */
    public BaseTmplPlanPo addTmplPlan(BaseTmplPlanAddForm baseTmplPlanAddForm);

    /**
     * 查询计划模板基本信息
     * @param id
     * @return
     */
    public BaseTmplPlanVo getTmplPlanInfoById(Integer id);

    /**
     * 修改计划模板
     * @param baseTmplPlanUpdateForm
     * @return
     */
    public BaseTmplPlanPo updateTmplPlan(BaseTmplPlanUpdateForm baseTmplPlanUpdateForm);

    /**
     * 根据id删除模板
     */
    public void deleteTmplPlanById(Integer id);

    /**
     * 获取计划模板的数据
     * @return
     */
    public List<SelectVo> queryTmplPlanSelectList(Integer userId);


}
