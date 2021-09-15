package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.coderule.BaseCoderuleCellAddForm;
import com.wisdom.acm.base.form.coderule.BaseCoderuleCellUpdateForm;
import com.wisdom.acm.base.po.BaseCoderuleCellPo;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleCellVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.cache.annotation.Cache;

import java.util.List;

public interface BaseCoderuleCellService extends CommService<BaseCoderuleCellPo> {

    /**
     * getById(understand?)
     * @param id
     * @return
     */
    BaseCoderuleCellVo getCoderuleCellById(Integer id);

    /**
     * add
     * @param form
     * @return
     */
    BaseCoderuleCellPo addCoderuleCell(BaseCoderuleCellAddForm form);

    /**
     * update
     * @param form
     * @return
     */
    BaseCoderuleCellPo updateCoderuleCell(BaseCoderuleCellUpdateForm form);

    /**
     * deleteCoderuleCellByIds
     * @param ids
     */
    void deleteCoderuleCellByIds(List<Integer> ids);

    /**
     * getByRuleIdAndPosition
     * @param ruleId
     * @param position
     * @return
     */
    BaseCoderuleCellVo getCoderuleCellByRuleIdAndPosition(Integer ruleId, Integer position);

    @Cache(key = "coderule:cell{1}")
    List<BaseCoderuleCellPo> queryByRuleId(Integer ruleId);
}
