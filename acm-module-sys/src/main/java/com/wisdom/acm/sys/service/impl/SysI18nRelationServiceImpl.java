package com.wisdom.acm.sys.service.impl;

import com.wisdom.acm.sys.form.I18nRelationForm;
import com.wisdom.acm.sys.form.SysI18nAddForm;
import com.wisdom.acm.sys.mapper.SysI18nMapper;
import com.wisdom.acm.sys.mapper.SysI18nRelationMapper;
import com.wisdom.acm.sys.po.SysI18nPo;
import com.wisdom.acm.sys.po.SysI18nRelationPo;
import com.wisdom.acm.sys.service.SysI18nRelationService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;

@Service
public class SysI18nRelationServiceImpl extends BaseService<SysI18nRelationMapper, SysI18nRelationPo> implements SysI18nRelationService {

    @Override
    public void addSysI18nRelation(SysI18nAddForm sysI18nAddForm,Integer i18nId) {
        //增加中间关系
       List<I18nRelationForm> i18nRelationForms = sysI18nAddForm.getI18nRelationForms();
       if (!ObjectUtils.isEmpty(i18nRelationForms)){
           for (I18nRelationForm i18nRelationForm : i18nRelationForms){
               SysI18nRelationPo sysI18nRelationPo = dozerMapper.map(i18nRelationForm, SysI18nRelationPo.class);
                sysI18nRelationPo.setI18nId(i18nId);
                super.insert(sysI18nRelationPo);
           }
       }


    }

    @Override
    public void deleteSysI18nRelation(List<Integer> i18nIds) {
        if (!ObjectUtils.isEmpty(i18nIds)){
            Example example = new Example(SysI18nRelationPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("i18nId",i18nIds);
            List<SysI18nRelationPo> list = this.mapper.selectByExample(example);
            List<Integer> ids = ListUtil.toIdList(list);
            super.deleteByIds(ids);
        }
    }
}
