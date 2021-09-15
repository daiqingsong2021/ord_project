package com.wisdom.acm.base.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.base.form.tmpldelv.BaseTmpldelvTypeAddForm;
import com.wisdom.acm.base.form.tmpldelv.BaseTmpldelvTypeUpdateForm;
import com.wisdom.acm.base.po.BaseTmpldelvTypePo;
import com.wisdom.acm.base.vo.tmpldelv.BaseTmpldelvTypeVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface BaseTmplDelvTypeService extends CommService<BaseTmpldelvTypePo>{
    /**
     * 交付物模板分页列表
     * @return
     */
    public PageInfo<BaseTmpldelvTypeVo> querryTmpldelvTypePageList(Integer pageSize, Integer currentPageNum,String key);

    /**
     * 交付物模板基本信息
     * @return
     */
    public BaseTmpldelvTypeVo getTmpldelvTypeById(Integer tmpldelvTypeId);

    /**
     * 新增交付物模板
     * @param baseTmpldelvTypeAddForm
     * @return
     */
    public BaseTmpldelvTypePo addTmpldelvType(BaseTmpldelvTypeAddForm baseTmpldelvTypeAddForm);

    /**
     * 删除交付物模板
     * @param baseTmpldelvTypeUpdateForm
     * @return
     */
    public BaseTmpldelvTypePo updateTmpldelvType(BaseTmpldelvTypeUpdateForm baseTmpldelvTypeUpdateForm);

    /**
     * 根据id删除交付物模板
     * @param id
     */
    public void deleteTmpldelvTypeById(Integer id);

}
