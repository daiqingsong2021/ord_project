package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.form.SysI18nAddForm;
import com.wisdom.acm.sys.po.SysFuncPo;
import com.wisdom.acm.sys.po.SysI18nRelationPo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface SysI18nRelationService extends CommService<SysI18nRelationPo> {


    void addSysI18nRelation(SysI18nAddForm sysI18nAddForm,Integer i18nId);

    //删除
    void deleteSysI18nRelation(List<Integer> ids);

}
