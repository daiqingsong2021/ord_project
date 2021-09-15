package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.dict.BaseDictAddForm;
import com.wisdom.acm.base.form.dict.BaseDictUpdateForm;
import com.wisdom.acm.base.po.BaseDictPo;
import com.wisdom.acm.base.vo.dict.BaseDictTreeVo;
import com.wisdom.acm.base.vo.dict.BaseDictVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.DictionarysMap;
import com.wisdom.base.common.vo.SelectVo;

import java.util.List;
import java.util.Map;

public interface BaseDictService extends CommService<BaseDictPo> {

    /**
     * 根据业务对象查找数据字典
     * @param boCode
     * @return
     */
    public List<BaseDictVo> queryDictListByBoCode(String boCode);

    /**
     * 增加数据字典
     * @param baseDigitdirAddForm
     */
    public BaseDictPo addDict(BaseDictAddForm baseDigitdirAddForm);

    /**
     * 修改数据字典
     * @param baseDigitdirUpdateForm
     */
    public BaseDictPo updateDict(BaseDictUpdateForm baseDigitdirUpdateForm);

    /**
     * 删除数据字典
     * @param digitDirId
     */
    void deleteDict(List<Integer> digitDirId);

    /**
     * 获取数据字典
     * @param digitDirId
     * @return
     */
    BaseDictVo getDictInfo(int digitDirId);

    /**
     * 通过dictTypePo删除
     * @param typeCodes
     */
    public void deletedictByDictPo(List<String> typeCodes);

    /**
     * 通过typeCode查找
     * @param typeCode
     * @return
     */
    public List<BaseDictTreeVo> queryDictTreeListByTypeCode(String typeCode);


    /**
     * 根据类型代码查找字典码值--为其他模块提供接口
     * @param typeCode
     * @return
     */
    public List<SelectVo> queryDictTreeDateListByTypeCode(String typeCode);

    /**
     * 获取业务字典Map集合
     *
     * @param dictTypeCode
     * @return
     */
    Map<String, DictionaryVo> getDictMapByTypeCode(String dictTypeCode);

    /**
     * 根据多个业务字典类型获取业务字典Map集合
     *
     * @param dictTypeCode
     * @return
     */
    DictionarysMap getDictMapByTypeCode(List<String> dictTypeCode);

    /**
     * 根据业务字典类型查询PO集合
     *
     * @param dictCodes
     * @return
     */
    List<BaseDictPo> queryDictPosByDictCods(String... dictCodes);

    List<BaseDictPo> queryDictPosByTypeCods(String... typeCodes);

    /**
     * 更新字典码值排序号
     * @param upOrDown
     * @return
     */
    List<BaseDictTreeVo> updateDictSortNum(Integer id,String upOrDown);
}
