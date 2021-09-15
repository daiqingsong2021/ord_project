package com.wisdom.acm.sys.service.impl;

import com.wisdom.acm.sys.form.SysI18nAddForm;
import com.wisdom.acm.sys.mapper.FuncMapper;
import com.wisdom.acm.sys.mapper.SysI18nMapper;
import com.wisdom.acm.sys.po.SysFuncPo;
import com.wisdom.acm.sys.po.SysI18nPo;
import com.wisdom.acm.sys.po.SysI18nRelationPo;
import com.wisdom.acm.sys.service.SysI18nRelationService;
import com.wisdom.acm.sys.service.SysI18nService;
import com.wisdom.acm.sys.vo.SysFuncVo;
import com.wisdom.acm.sys.vo.SysI18nRelationVo;
import com.wisdom.acm.sys.vo.SysI18nVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SysI18nServiceImpl extends BaseService<SysI18nMapper, SysI18nPo> implements SysI18nService {

    @Autowired
    private SysI18nRelationService sysI18nRelationService;

    /**
     * 获取国际化列表
     * @param menuId
     * @return
     */
    @Override
    public List<SysI18nVo> querySysI18nVoList(Integer menuId) {
        List<SysI18nVo> sysI18nVoList = this.mapper.selectSysI18nVoByMenuId(menuId);
        List<Integer> ids = ListUtil.toIdList(sysI18nVoList);
        List<SysI18nRelationVo> sysI18nRelationVoList = null;
        if (!ObjectUtils.isEmpty(ids)){
            sysI18nRelationVoList = this.mapper.selectSysI18nRelationVoByIds(ids);
        }
        Map<Integer,List<SysI18nRelationVo>> listMap = ListUtil.listToMapList(sysI18nRelationVoList,"i18nId",Integer.class);
        for (SysI18nVo sysI18nVo : sysI18nVoList){
            List<SysI18nRelationVo> list = listMap.get(sysI18nVo.getId());
            sysI18nVo.setI18nRelationVos(list);
        }
        return this.mapper.selectSysI18nVoByMenuId(menuId);
    }

    /**
     * 获取国际化信息
     * @param id
     * @return
     */
    @Override
    public SysI18nVo getSysI18nVoInfo(Integer id) {
        SysI18nVo sysI18nVo = this.mapper.selectSysI18nVoOneById(id);
        List<SysI18nRelationVo> sysI18nRelationVoList = this.mapper.selectSysI18nRelationVoById(id);
        if (!Objects.isNull(sysI18nVo)){
            sysI18nVo.setI18nRelationVos(sysI18nRelationVoList);
        }
        return sysI18nVo;
    }

    /**
     * 增加国际化
     * @param sysI18nAddForm
     * @return
     */
    @Override
    public SysI18nPo addSysI18n(SysI18nAddForm sysI18nAddForm) {
        //判断功能代码是否重复
        Example example = new Example(SysI18nPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("code", sysI18nAddForm.getCode());
        criteria.andEqualTo("menuId", sysI18nAddForm.getMenuId());
        List<SysI18nPo> list = this.mapper.selectByExample(example);
        if (!ObjectUtils.isEmpty(list)) {
            throw new BaseException("国际化代码不能重复！");
        }
        SysI18nPo sysI18nPo = dozerMapper.map(sysI18nAddForm, SysI18nPo.class);
        super.insert(sysI18nPo);

        //增加中间关系
        sysI18nRelationService.addSysI18nRelation(sysI18nAddForm,sysI18nPo.getId());
        return sysI18nPo;
    }

    /**
     * 删除国际化
     * @param ids
     */
    @Override
    public void deleteSysI18n(List<Integer> ids) {
        if (!ObjectUtils.isEmpty(ids)){
            super.deleteByIds(ids);
            //删除中间关系
            sysI18nRelationService.deleteSysI18nRelation(ids);
        }
    }
}
