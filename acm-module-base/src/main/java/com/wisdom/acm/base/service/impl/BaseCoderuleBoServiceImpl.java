package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.form.coderule.BaseCoderuleBoAddForm;
import com.wisdom.acm.base.form.coderule.BaseCoderuleBoUpdateForm;
import com.wisdom.acm.base.mapper.BaseCoderuleBoMapper;
import com.wisdom.acm.base.po.BaseCoderuleBoPo;
import com.wisdom.acm.base.service.BaseCoderuleBoService;
import com.wisdom.acm.base.service.BaseCoderuleService;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleBoVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.LogUtil;
import com.wisdom.base.common.vo.SelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@Service
public class BaseCoderuleBoServiceImpl extends BaseService<BaseCoderuleBoMapper, BaseCoderuleBoPo> implements BaseCoderuleBoService {

    @Autowired
    private BaseCoderuleService baseCoderuleService;

    @Override
    public List<BaseCoderuleBoVo> querryCoderuleboList() {
        return mapper.selectCoderuleboList();
    }

    @Override
    public BaseCoderuleBoVo getCoderuleboById(Integer id) {
        BaseCoderuleBoVo vo = mapper.selectCoderuleboById(id);
        //获取所有的表名下拉
        List<SelectVo> tables = baseCoderuleService.queryTables();
        if (!Objects.isNull(vo)){
            for (SelectVo selectVo : tables){
                if (!Objects.isNull(vo) && !Objects.isNull(vo.getTableName())) {
                    if (vo.getTableName().getId().equals(selectVo.getValue())) {
                        vo.getTableName().setName(selectVo.getTitle());
                    }
                }
            }
            //根据表名id获取所有下拉
            List<SelectVo> columns = baseCoderuleService.queryFieldsByTableName(vo.getTableName().getId());
            for (SelectVo selectVo : columns){
                if (!Objects.isNull(vo.getCodeColumnName())) {
                    if (vo.getCodeColumnName().getId().equals(selectVo.getValue())) {
                        vo.getCodeColumnName().setName(selectVo.getTitle());
                    }
                }
                if (!Objects.isNull(vo.getSeqScope())) {
                    if (vo.getSeqScope().getId().equals(selectVo.getValue())) {
                        vo.getSeqScope().setName(selectVo.getTitle());
                    }
                }
                if (!Objects.isNull(vo.getAssignColumnName())) {
                    if (vo.getAssignColumnName().getId().equals(selectVo.getValue())) {
                        vo.getAssignColumnName().setName(selectVo.getTitle());
                    }
                }
            }
        }
        return vo;
    }

    @Override
    public BaseCoderuleBoVo getByBoCode(String boCode){
        List<BaseCoderuleBoVo> coderuleBoVos = mapper.selectCoderuleboByBoCode(boCode);
        return !ObjectUtils.isEmpty(coderuleBoVos) ? coderuleBoVos.get(0): null;
    }

    @Override
    public BaseCoderuleBoPo addCoderulebo(BaseCoderuleBoAddForm form) {
        List<BaseCoderuleBoVo> coderuleBoVos = mapper.selectCoderuleboByBoCode(form.getBoCode());
        if(!ObjectUtils.isEmpty(coderuleBoVos)){
            throw new BaseException("代码已存在!");
        }
        BaseCoderuleBoPo po =  dozerMapper.map(form, BaseCoderuleBoPo.class);
        po.setSort(this.selectNextSort());
        this.insert(po);
        return po;
    }

    @Override
    @AddLog(title = "修改编码规则业务对象",module = LoggerModuleEnum.BM_CODERULE)
    public BaseCoderuleBoPo updateCoderulebo(BaseCoderuleBoUpdateForm form) {
        BaseCoderuleBoPo po = mapper.selectByPrimaryKey(form.getId());
        if(ObjectUtils.isEmpty(po)){
            throw new BaseException("修改的数据不存在!");
        }
        // 添加修改日志
        this.addChangeLogger(form,po);
        List<BaseCoderuleBoVo> coderuleBoVos = mapper.selectCoderuleboByBoCode(form.getBoCode());
        if(!ObjectUtils.isEmpty(coderuleBoVos)&& !coderuleBoVos.get(0).getId().equals(form.getId())){
            throw new BaseException("代码已存在!");
        }
        dozerMapper.map(form, po);
        mapper.updateByPrimaryKey(po);
        return po;
    }

    @Override
    public void deleteCoderulebo(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }
}
