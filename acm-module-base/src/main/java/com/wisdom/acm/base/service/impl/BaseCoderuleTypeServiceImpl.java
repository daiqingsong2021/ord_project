package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.form.coderule.BaseCoderuleTypeAddForm;
import com.wisdom.acm.base.form.coderule.BaseCoderuleTypeUpdateForm;
import com.wisdom.acm.base.mapper.BaseCoderuleTypeMapper;
import com.wisdom.acm.base.po.BaseCoderuleTypePo;
import com.wisdom.acm.base.service.BaseCoderuleService;
import com.wisdom.acm.base.service.BaseCoderuleTypeService;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleTypeVo;
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
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BaseCoderuleTypeServiceImpl extends BaseService<BaseCoderuleTypeMapper, BaseCoderuleTypePo> implements BaseCoderuleTypeService {

    @Autowired
    private BaseCoderuleService baseCoderuleService;

    @Override
    public List<BaseCoderuleTypeVo> querryCoderuleTypeListByboId(Integer boId) {
        List<BaseCoderuleTypeVo> voList = mapper.selectCoderuleTypeListByboId(boId);
        if (!ObjectUtils.isEmpty(voList)){
            //获取所有的表名下拉
            List<SelectVo> tables = baseCoderuleService.queryTables();
            //根据表名id获取所有下拉
            List<SelectVo> columns = new ArrayList<SelectVo>();
            for (BaseCoderuleTypeVo vo : voList){
                //表名
                for (SelectVo selectVo : tables){
                    if (!Objects.isNull(vo.getTableName())){
                        if (vo.getTableName().getId().equals(selectVo.getValue())){
                            vo.getTableName().setName(selectVo.getTitle());
                            //根据表名id获取所有下拉
                            columns = baseCoderuleService.queryFieldsByTableName(vo.getTableName().getId());
                        }
                    }
                }
                //字段名
                for (SelectVo selectVo : columns){
                    if (!Objects.isNull(vo.getColumnName())) {
                        if (vo.getColumnName().getId().equals(selectVo.getValue())) {
                            vo.getColumnName().setName(selectVo.getTitle());
                        }
                    }
                }
            }
        }

        return voList;
    }

    @Override
    public BaseCoderuleTypeVo getCoderuleTypeById(Integer id) {
        BaseCoderuleTypeVo vo = mapper.selectCoderuleListById(id);
        //获取所有的表名下拉
        List<SelectVo> tables = baseCoderuleService.queryTables();
        if (!ObjectUtils.isEmpty(vo)){
            for (SelectVo selectVo : tables){
                if (!ObjectUtils.isEmpty(vo) && !ObjectUtils.isEmpty(vo.getTableName())) {
                    if (vo.getTableName().getId().equals(selectVo.getValue())) {
                        vo.getTableName().setName(selectVo.getTitle());
                    }

                }
            }
            if (!ObjectUtils.isEmpty(vo.getTableName())) {
                //根据表名id获取所有下拉
                List<SelectVo> columns = baseCoderuleService.queryFieldsByTableName(vo.getTableName().getId());
                for (SelectVo selectVo : columns){
                    if (!ObjectUtils.isEmpty(vo.getColumnName())) {
                        if (vo.getColumnName().getId().equals(selectVo.getValue())) {
                            vo.getColumnName().setName(selectVo.getTitle());
                        }
                    }
                }
            }
        }
        return vo;
    }

    @Override
    public BaseCoderuleTypePo addCoderuleType(BaseCoderuleTypeAddForm form) {
        BaseCoderuleTypePo po =  dozerMapper.map(form, BaseCoderuleTypePo.class);
        po.format();

        //新增生成sort_num
        Example example = new Example(BaseCoderuleTypePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ruleBoId",form.getRuleBoId());
        Integer sortNum = this.selectNextSortByExample(example);
        po.setSort(sortNum);
        po.setRuleType("ATTRIBUTE");
        this.insert(po);
        return po;
    }

    @Override
    @AddLog(title = "修改编码规则类型", module = LoggerModuleEnum.BM_CODERULE)
    public BaseCoderuleTypePo updateCoderuleType(BaseCoderuleTypeUpdateForm form) {
        BaseCoderuleTypePo po = mapper.selectByPrimaryKey(form.getId());

        po.format();

        if(ObjectUtils.isEmpty(po)){
            throw new BaseException("修改的数据不存在!");
        }
        // 添加修改日志
        this.addChangeLogger(form,po);
        dozerMapper.map(form, po);
        mapper.updateByPrimaryKey(po);
        return po;
    }

    @Override
    public void deleteCoderuleTypeByIds(List<Integer> ids) {
        this.deleteByIds(ids);
    }
}
