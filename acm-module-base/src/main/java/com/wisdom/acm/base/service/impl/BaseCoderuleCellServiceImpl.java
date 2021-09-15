package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.form.coderule.BaseCoderuleCellAddForm;
import com.wisdom.acm.base.form.coderule.BaseCoderuleCellUpdateForm;
import com.wisdom.acm.base.mapper.BaseCoderuleCellMapper;
import com.wisdom.acm.base.po.BaseCoderuleCellPo;
import com.wisdom.acm.base.po.BaseCoderuleTypePo;
import com.wisdom.acm.base.service.BaseCoderuleCellService;
import com.wisdom.acm.base.service.BaseCoderuleTypeService;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleCellVo;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleTypeVo;
import com.wisdom.base.common.enums.CodeRuleTypeEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BaseCoderuleCellServiceImpl extends BaseService<BaseCoderuleCellMapper, BaseCoderuleCellPo> implements BaseCoderuleCellService {

    @Autowired
    private BaseCoderuleTypeService coderuleTypeService;

    public void formatter(BaseCoderuleCellPo bean) {
        BaseCoderuleTypePo type = coderuleTypeService.selectById(bean.getRuleTypeId());
        if(!ObjectUtils.isEmpty(type)){
            if(type.getRuleType().equals(CodeRuleTypeEnum.DATE.name()) || type.getRuleType().equals(CodeRuleTypeEnum.ATTRIBUTE.name()))
            {
                bean.setCellSql(type.getTypeSql());
            }else if(type.getRuleType().equals(CodeRuleTypeEnum.FIXED_VALUE.name())){
                bean.setCellSql("select '"+ bean.getCellValue());
            }//else if(type.getRuleType().equals(CodeRuleTypeEnum.ATTRIBUTE.name())){
//            if (!ObjectUtils.isEmpty(type.getDictType())) {
//                String sql = "select t.gb_name from pmo_gbtype t where t.gb_type = '" + type.getDictType() + "' and t.gb_code in (" + type.getTypeSql() + ")";
//                bean.setCellSql(sql);
//            }

            //  }
        }
    }

    @Override
    public BaseCoderuleCellVo getCoderuleCellById(Integer id) {
        BaseCoderuleCellVo vo = mapper.selectCoderuleCellById(id);
        vo.getRuleTypeVo().setName(CodeRuleTypeEnum.getMessageByCode(vo.getRuleTypeVo().getId()));
        return vo;
    }

    @Override
    public BaseCoderuleCellVo getCoderuleCellByRuleIdAndPosition(Integer ruleId, Integer position) {
        List<BaseCoderuleCellVo> vos = mapper.selectCoderuleCellByRuleIdAndPosition(ruleId,position);
        BaseCoderuleCellVo vo = new BaseCoderuleCellVo();
        if(!ObjectUtils.isEmpty(vos)){
            vo = vos.get(0);
            vo.getRuleTypeVo().setName(CodeRuleTypeEnum.getMessageByCode(vo.getRuleTypeVo().getId()));
        }
        return vo;
    }

    @Override
    public List<BaseCoderuleCellPo> queryByRuleId(Integer ruleId){
        Example exist = new Example(BaseCoderuleCellPo.class);
        Example.Criteria excia = exist.createCriteria();
        excia.andEqualTo("ruleId", ruleId);
        exist.setOrderByClause("position asc");
        return this.selectByExample(exist);
    }

    @Override
    public BaseCoderuleCellPo addCoderuleCell(BaseCoderuleCellAddForm form) {
        BaseCoderuleCellPo po =  dozerMapper.map(form, BaseCoderuleCellPo.class);

        this.formatter(po);

        //唯一判断  rule_id +  position
        Example exist = new Example(BaseCoderuleCellPo.class);
        Example.Criteria excia = exist.createCriteria();
        excia.andEqualTo("ruleId",form.getRuleId());
        excia.andEqualTo("position",form.getPosition());
        List<BaseCoderuleCellPo> ps = this.selectByExample(exist);
        if(!ObjectUtils.isEmpty(ps)){
            throw new BaseException("已存在，请输入唯一段位!");
        }

        //新增生成sort_num
        Example example = new Example(BaseCoderuleCellPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ruleId",form.getRuleId());
        Integer sortNum = this.selectNextSortByExample(example);
        po.setSort(sortNum);
        this.insert(po);
        return po;
    }

    @Override
    public BaseCoderuleCellPo updateCoderuleCell(BaseCoderuleCellUpdateForm form) {
        BaseCoderuleCellPo po = mapper.selectByPrimaryKey(form.getId());

        this.formatter(po);

        if(ObjectUtils.isEmpty(po)){
            throw new BaseException("修改的数据不存在!");
        }
        dozerMapper.map(form, po);
        mapper.updateByPrimaryKey(po);
        return po;
    }

    @Override
    public void deleteCoderuleCellByIds(List<Integer> ids) {
        this.deleteByIds(ids);
    }
}
