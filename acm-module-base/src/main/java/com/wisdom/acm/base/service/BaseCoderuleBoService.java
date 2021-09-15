package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.coderule.BaseCoderuleBoAddForm;
import com.wisdom.acm.base.form.coderule.BaseCoderuleBoUpdateForm;
import com.wisdom.acm.base.po.BaseCoderuleBoPo;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleBoVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface BaseCoderuleBoService extends CommService<BaseCoderuleBoPo> {

    /**
     * 编码规则-业务对象列表
     * @return
     */
    List<BaseCoderuleBoVo> querryCoderuleboList();

    /**
     * 编码规则-业务对象的基本信息
     * @param id
     * @return
     */
    BaseCoderuleBoVo getCoderuleboById(Integer id);

    BaseCoderuleBoVo getByBoCode(String boCode);

    BaseCoderuleBoPo addCoderulebo(BaseCoderuleBoAddForm form);

    BaseCoderuleBoPo updateCoderulebo(BaseCoderuleBoUpdateForm form);

    void deleteCoderulebo(Integer id);
}
