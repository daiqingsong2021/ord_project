package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.form.SysAuthAddForm;
import com.wisdom.acm.sys.form.SysAuthUpdateForm;
import com.wisdom.acm.sys.form.SysI18nAddForm;
import com.wisdom.acm.sys.po.SysFuncPo;
import com.wisdom.acm.sys.po.SysI18nPo;
import com.wisdom.acm.sys.vo.SysFuncVo;
import com.wisdom.acm.sys.vo.SysI18nVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface SysI18nService extends CommService<SysI18nPo> {
    //获取国际化列表
    List<SysI18nVo> querySysI18nVoList(Integer menuId);

    //获取国际化信息
    SysI18nVo getSysI18nVoInfo(Integer id);

    //新增国际化
    SysI18nPo addSysI18n(SysI18nAddForm sysI18nAddForm);

    //删除国际化
    void deleteSysI18n(List<Integer> ids);

}
