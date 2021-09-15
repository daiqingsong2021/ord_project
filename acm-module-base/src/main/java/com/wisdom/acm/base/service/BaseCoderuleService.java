package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.coderule.BaseCoderuleAddForm;
import com.wisdom.acm.base.form.coderule.BaseCoderuleUpdateForm;
import com.wisdom.acm.base.po.BaseCoderulePo;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleVo;
import com.wisdom.acm.base.vo.coderule.ReturnMsgVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.SelectVo;

import java.util.List;

public interface BaseCoderuleService extends CommService<BaseCoderulePo> {

    /**
     * 批量删除
     * @param ids
     */
    void deleteCoderuleByIds(List<Integer> ids);

    /**
     * add
     * @param form
     * @return
     */
    BaseCoderulePo addCoderule(BaseCoderuleAddForm form);

    /**
     * byId
     * @param id
     * @return
     */
    BaseCoderuleVo getCoderuleById(Integer id);

    /**
     * List ByboId
     * @param boId
     * @return
     */
    List<BaseCoderuleVo> querryCoderuleListByboId(Integer boId);

    /**
     * update
     * @param form
     * @return
     */
    BaseCoderulePo updateCoderule(BaseCoderuleUpdateForm form);

    /**
     * 验证
     * @param ruleId
     * @return
     */
    ReturnMsgVo updateBaseCoderuleStatus(Integer ruleId);

    public List<SelectVo> queryTables();

    public List<SelectVo> queryFieldsByTableName(String tableName);

    BaseCoderuleVo getDefaultByBoId(Integer boId);
}
