package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.coderule.BaseCoderuleTypeAddForm;
import com.wisdom.acm.base.form.coderule.BaseCoderuleTypeUpdateForm;
import com.wisdom.acm.base.po.BaseCoderuleTypePo;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleTypeVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface BaseCoderuleTypeService extends CommService<BaseCoderuleTypePo> {

    /**
     * ByboId
     * @param boId
     * @return
     */
    List<BaseCoderuleTypeVo> querryCoderuleTypeListByboId(Integer boId);

    /**
     * ById
     * @param id
     * @return
     */
    BaseCoderuleTypeVo getCoderuleTypeById(Integer id);

    /**
     * add
     * @param form
     * @return
     */
    BaseCoderuleTypePo addCoderuleType(BaseCoderuleTypeAddForm form);

    /**
     * updateCoderuleType
     * @param form
     * @return
     */
    BaseCoderuleTypePo updateCoderuleType(BaseCoderuleTypeUpdateForm form);

    /**
     * 批量删除
     * @param ids
     */
    void deleteCoderuleTypeByIds(List<Integer> ids);
}
