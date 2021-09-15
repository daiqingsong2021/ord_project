package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.dict.BaseDictTypeAddForm;
import com.wisdom.acm.base.form.dict.BaseDictTypeUpdateForm;
import com.wisdom.acm.base.po.BaseDictPo;
import com.wisdom.acm.base.po.BaseDictTypePo;
import com.wisdom.acm.base.vo.dict.BaseDictTypeInfoVo;
import com.wisdom.acm.base.vo.dict.BaseDictTypeVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface BaseDictTypeService extends CommService<BaseDictTypePo> {

    /**
     * 根据数据字典查找字典码值
     * @param dictCode
     * @return
     */
    public List<BaseDictTypeVo> queryDictTypeListByDictCode(String dictCode);

    /**
     * 查找字典码值
     * @return
     */
    public List<BaseDictTypeVo> selectDictTypeDateList();

    /**
     * 增加字典码值
     * @param baseDictTypeAddForm
     */
    public BaseDictTypePo addDictType(BaseDictTypeAddForm baseDictTypeAddForm);

    /**
     * 修改字典码值
     * @param BaseDictTypeUpdateForm
     */
    public BaseDictTypePo updateDictType(BaseDictTypeUpdateForm BaseDictTypeUpdateForm);

    /**
     * 删除字典码值
     * @param ids
     */
    public void deleteDictType(List<Integer> ids);

    /**
     * 获取字典码值基本信息
     * @param gbTypeId
     * @return
     */
    BaseDictTypeInfoVo getDictTypeInfo(Integer gbTypeId);

    /**
     * 根据数字字典删除字典码值
     * @param dictPo
     */
    void deleteDictTypeByDictPo(BaseDictPo dictPo);
}
