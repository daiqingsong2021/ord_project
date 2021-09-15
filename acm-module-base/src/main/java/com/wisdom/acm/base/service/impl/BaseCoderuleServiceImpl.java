package com.wisdom.acm.base.service.impl;


import com.google.common.collect.*;
import com.wisdom.acm.base.form.coderule.BaseCoderuleAddForm;
import com.wisdom.acm.base.form.coderule.BaseCoderuleUpdateForm;
import com.wisdom.acm.base.mapper.BaseCoderuleCellMapper;
import com.wisdom.acm.base.mapper.BaseCoderuleMapper;
import com.wisdom.acm.base.mapper.DBMetadataMapper;
import com.wisdom.acm.base.po.BaseCoderuleCellPo;
import com.wisdom.acm.base.po.BaseCoderulePo;
import com.wisdom.acm.base.service.BaseCoderuleService;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleBoVo;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleVo;
import com.wisdom.acm.base.vo.coderule.ReturnMsgVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.CodeRuleTypeEnum;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.util.LogUtil;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.SelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BaseCoderuleServiceImpl extends BaseService<BaseCoderuleMapper, BaseCoderulePo> implements BaseCoderuleService {

    @Autowired
    private BaseCoderuleCellMapper cellMapper;

    @Autowired
    private DBMetadataMapper metadataMapper;

    @Override
    public void deleteCoderuleByIds(List<Integer> ids) {
        this.deleteByIds(ids);
    }

    @Override
    public BaseCoderulePo addCoderule(BaseCoderuleAddForm form) {
        BaseCoderulePo po =  dozerMapper.map(form, BaseCoderulePo.class);

        //新增生成sort_num
        Example example = new Example(BaseCoderulePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ruleBoId",form.getRuleBoId());
        Integer sortNum = this.selectNextSortByExample(example);
        po.setSort(sortNum);
        this.insert(po);
        return po;
    }

    @Override
    public BaseCoderuleVo getCoderuleById(Integer id) {
        BaseCoderuleVo vo = mapper.selectCoderuleById(id);
        return vo;
    }

    @Override
    public List<BaseCoderuleVo> querryCoderuleListByboId(Integer boId) {
        List<BaseCoderuleVo> coderuleVoList = mapper.selectCoderuleListByboId(boId);
        if(!ObjectUtils.isEmpty(coderuleVoList)) {
            List<Integer> coderuleIds = coderuleVoList.stream().map(coderuleVo -> coderuleVo.getId()).collect(Collectors.toList());
            List<BaseCoderuleCellPo> coderuleCellPoList = cellMapper.selectPosByRuleIds(coderuleIds);
            ImmutableMap<String, BaseCoderuleCellPo> codeRuleIdPosition2CoderuleCell = Maps.uniqueIndex(coderuleCellPoList, coderuleCellPo -> coderuleCellPo.getRuleId() + "_" + coderuleCellPo.getPosition());
            try {
                for (BaseCoderuleVo coderuleVo : coderuleVoList) {
                    for (int i = 1; i <= 7; i++) {
                        BaseCoderuleCellPo cell = codeRuleIdPosition2CoderuleCell.get(coderuleVo.getId() + "_" + i);
                        if (!ObjectUtils.isEmpty(cell)) {
                            Field field = coderuleVo.getClass().getDeclaredField("position" + i);
                            field.setAccessible(true);
                            field.set(coderuleVo, new GeneralVo(cell.getId(), cell.getRuleCellName()));
                        }
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {

            }
        }
        return coderuleVoList;
    }

    @Override
    @AddLog(title = "修改编码规则", module = LoggerModuleEnum.BM_CODERULE)
    public BaseCoderulePo updateCoderule(BaseCoderuleUpdateForm form) {
        BaseCoderulePo po = mapper.selectByPrimaryKey(form.getId());
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
    public ReturnMsgVo updateBaseCoderuleStatus(Integer ruleId) {
        if(ObjectUtils.isEmpty(mapper.selectByPrimaryKey(ruleId))){
            throw new BaseException("数据不存在!");
        }

        ReturnMsgVo msgVo = new ReturnMsgVo();
        List<Integer> ruleIds = new ArrayList<>();
        ruleIds.add(ruleId);
        List<BaseCoderuleCellPo> cells = cellMapper.selectPosByRuleIds(ruleIds);
        boolean correct = true;
        String error = "";
        int lastPosition = 0;
        boolean hasSequence = false;
        if(!ObjectUtils.isEmpty(cells)){
            for(int i = 0,length = cells.size();i< length;i++){
                BaseCoderuleCellPo cell = cells.get(i);
                Integer position = cell.getPosition();
                if(position - lastPosition == 1){
                    lastPosition = position;
                    if(i < length -1){	//
                        if(CodeRuleTypeEnum.SEQUENCE.getCode().equals(cell.getRuleType())){
                            correct = false;
                            error = "流水号必须是最后一段规则";
                            break;
                        }
                    }
                    if(CodeRuleTypeEnum.SEQUENCE.getCode().equals(cell.getRuleType()))
                    {
                        hasSequence = true;
                    }
                }else{	//
                    correct = false;
                    error = "规则不连续";
                    break;
                }
            }
        }else{
            correct = false;
            error = "没有定义规则内容";
        }
        if(correct && !hasSequence){
            correct = false;
            error = "必须设置流水号";
        }
        BaseCoderulePo rule = mapper.selectByPrimaryKey(ruleId);
        rule.setStatus(correct ? "CORRECT" :"ERROR");
        mapper.updateByPrimaryKey(rule);
        msgVo.setStatus(correct ? "CORRECT" : "ERROR");
        msgVo.setError(error);
        return msgVo;
    }

    @Override
    public List<SelectVo> queryTables(){
        List<SelectVo> tables =  Lists.newArrayList();
        List<GeneralVo> list = this.metadataMapper.listTable();
        if(!ObjectUtils.isEmpty(list)){
            list.stream().forEach(vo -> {
                SelectVo table = new SelectVo();
                table.setValue(FormatUtil.toString(vo.getCode()));
                if(ObjectUtils.isEmpty(vo.getName())){
                    table.setTitle(FormatUtil.toString(vo.getCode()));
                }else{
                    table.setTitle(FormatUtil.toString(vo.getName()));
                }
                tables.add(table);
            });
        }
        return tables;
    }

    @Override
    public List<SelectVo> queryFieldsByTableName(String tableName){
        List<SelectVo> columns =  Lists.newArrayList();
        List<GeneralVo> list = this.metadataMapper.listTableColumn(tableName);
        if(!ObjectUtils.isEmpty(list)){
            list.stream().forEach(vo -> {
                SelectVo column = new SelectVo();
                column.setValue(FormatUtil.toString(vo.getCode()));
                if(ObjectUtils.isEmpty(vo.getName())){
                    column.setTitle(FormatUtil.toString(vo.getCode()));
                }else{
                    column.setTitle(FormatUtil.toString(vo.getName()));
                }
                columns.add(column);
            });
        }
        return columns;
    }

    @Override
    public BaseCoderuleVo getDefaultByBoId(Integer boId){
        return mapper.selectDefaultByBoId(boId);
    }
}
