package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.form.dict.BaseDictTypeAddForm;
import com.wisdom.acm.base.form.dict.BaseDictTypeUpdateForm;
import com.wisdom.acm.base.mapper.BaseDictTypeMapper;
import com.wisdom.acm.base.po.BaseDictPo;
import com.wisdom.acm.base.po.BaseDictTypePo;
import com.wisdom.acm.base.service.BaseDictService;
import com.wisdom.acm.base.service.BaseDictTypeService;
import com.wisdom.acm.base.vo.dict.BaseDictTypeInfoVo;
import com.wisdom.acm.base.vo.dict.BaseDictTypeVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BaseDictTypeServiceImpl extends BaseService<BaseDictTypeMapper, BaseDictTypePo> implements BaseDictTypeService {

    @Autowired
    private BaseDictService dictService;

    @Override
    public List<BaseDictTypeVo> queryDictTypeListByDictCode(String dictCode) {
        List<BaseDictTypeVo> gbTypeList = this.mapper.selectDictTypeDateListByDictCode(dictCode);
        return gbTypeList;
    }

    @Override
    public List<BaseDictTypeVo> selectDictTypeDateList() {
        List<BaseDictTypeVo> gbTypeList = this.mapper.selectDictTypeDateList();
        return gbTypeList;
    }

    @Override
    public BaseDictTypePo addDictType(BaseDictTypeAddForm baseDictTypeAddForm) {
        List<BaseDictTypePo> list = this.getBaseDictTypeByCode(baseDictTypeAddForm.getTypeCode());
        if (!ObjectUtils.isEmpty(list)){
            throw new BaseException("字典类型代码不能重复");
        }
        BaseDictTypePo gbTypePo = this.dozerMapper.map(baseDictTypeAddForm, BaseDictTypePo.class);
        //默认不是内置
        gbTypePo.setBuiltIn(0);

        gbTypePo.setSort(this.getDictTypeNextSort(baseDictTypeAddForm.getTypeCode()));
        super.insert(gbTypePo);
        return gbTypePo;
    }

    /**
     * 根据类型代码算取排序值
     * @param typeCode
     * @return
     */
    private int getDictTypeNextSort(String typeCode){
        Example example = new Example(BaseDictTypePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("boCode", typeCode);
        return this.selectNextSortByExample(example);
    }

    public List<BaseDictTypePo> getBaseDictTypeByCode(String code){
        Example example = new Example(BaseDictTypePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("typeCode",code);
        List<BaseDictTypePo> list = this.mapper.selectByExample(example);
        return ObjectUtils.isEmpty(list)? null : list;
    }

    @Override
    @AddLog(title = "修改数据字典" , module = LoggerModuleEnum.BM_DICT)
    public BaseDictTypePo updateDictType(BaseDictTypeUpdateForm baseDictTypeUpdateForm) {

        BaseDictTypePo baseDictTypePo = this.selectById(baseDictTypeUpdateForm.getId());
        if(ObjectUtils.isEmpty(baseDictTypePo)){
            throw new BaseException("该数据不存在");
        }

        // 添加修改日志
        this.addChangeLogger(baseDictTypeUpdateForm,baseDictTypePo);
        List<BaseDictTypePo> list = this.getBaseDictTypeByCode(baseDictTypeUpdateForm.getTypeCode());
        if (!ObjectUtils.isEmpty(list) && !list.get(0).getId().equals(baseDictTypeUpdateForm.getId())){
            throw new BaseException("字典类型代码不能重复");
        }
        this.dozerMapper.map(baseDictTypeUpdateForm,baseDictTypePo);
        super.updateById(baseDictTypePo);
        return baseDictTypePo;
    }

    @Override
    public void deleteDictType(List<Integer> ids) {
        List<String> typeCodes = mapper.selectDictTypeCodesByIds(ids);
        dictService.deletedictByDictPo(typeCodes);
        super.deleteByIds(ids);
    }

    @Override
    public BaseDictTypeInfoVo getDictTypeInfo(Integer gbTypeId) {
        BaseDictTypeInfoVo gbTypePo = mapper.selectDictTypeInfoById(gbTypeId);
        return gbTypePo;
    }

    @Override
    public void deleteDictTypeByDictPo(BaseDictPo dictPo) {
        List<BaseDictTypeVo> gbTypeList = this.queryDictTypeListByDictCode(dictPo.getDictCode());
        if (!ObjectUtils.isEmpty(gbTypeList)) {
            for (BaseDictTypeVo baseDictTypeVo : gbTypeList) {
                mapper.deleteByPrimaryKey(baseDictTypeVo.getId());
            }
        }
    }
}
