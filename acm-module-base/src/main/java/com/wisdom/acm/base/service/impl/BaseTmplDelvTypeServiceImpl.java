package com.wisdom.acm.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.base.form.tmpldelv.BaseTmpldelvTypeAddForm;
import com.wisdom.acm.base.form.tmpldelv.BaseTmpldelvTypeUpdateForm;
import com.wisdom.acm.base.mapper.BaseTmpldelvTypeMapper;
import com.wisdom.acm.base.po.BaseTmplTaskPo;
import com.wisdom.acm.base.po.BaseTmpldelvTypePo;
import com.wisdom.acm.base.service.BaseTmplDelvService;
import com.wisdom.acm.base.service.BaseTmplDelvTypeService;
import com.wisdom.acm.base.vo.tmpldelv.BaseTmpldelvTypeVo;
import com.wisdom.acm.base.vo.tmpldelv.BaseTmpldelvVo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskVo;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseTmplDelvTypeServiceImpl extends BaseService<BaseTmpldelvTypeMapper, BaseTmpldelvTypePo> implements BaseTmplDelvTypeService {

    @Autowired
    BaseTmplDelvService baseTmplDelvService;

    @Override
    public PageInfo<BaseTmpldelvTypeVo> querryTmpldelvTypePageList(Integer pageSize, Integer currentPageNum,String key) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<BaseTmpldelvTypeVo> list = mapper.selectTmpldelvTypeList(key);
        PageInfo<BaseTmpldelvTypeVo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public BaseTmpldelvTypeVo getTmpldelvTypeById(Integer tmpldelvTypeId) {
        BaseTmpldelvTypeVo baseTmpldelvTypeVo = this.mapper.selectTmpldelvTypeById(tmpldelvTypeId);
        return  baseTmpldelvTypeVo;
    }

    @Override
    public BaseTmpldelvTypePo addTmpldelvType(BaseTmpldelvTypeAddForm baseTmpldelvTypeAddForm) {
        List<BaseTmpldelvTypePo> list = this.queryBaseTmpldelvTypePoByTypeNum(baseTmpldelvTypeAddForm.getTypeNum());
        if (!ObjectUtils.isEmpty(list)){
            throw new BaseException("交付物模板编号不能重复!");
        }
        BaseTmpldelvTypePo baseTmpldelvTypePo = this.dozerMapper.map(baseTmpldelvTypeAddForm,BaseTmpldelvTypePo.class);
        super.insert(baseTmpldelvTypePo);
        return baseTmpldelvTypePo;
    }

    @Override
    @AddLog(title = "修改交付模板",module = LoggerModuleEnum.BM_TMPL_DELV)
    public BaseTmpldelvTypePo updateTmpldelvType(BaseTmpldelvTypeUpdateForm baseTmpldelvTypeUpdateForm) {
        BaseTmpldelvTypePo baseTmpldelvTypePo = this.selectById(baseTmpldelvTypeUpdateForm.getId());
        if(ObjectUtils.isEmpty(baseTmpldelvTypePo)){
            throw new BaseException("修改的任务不存在!");
        }
        this.addChangeLogger(baseTmpldelvTypeUpdateForm,baseTmpldelvTypePo);
        List<BaseTmpldelvTypePo> list = this.queryBaseTmpldelvTypePoByTypeNum(baseTmpldelvTypeUpdateForm.getTypeNum());
        if (!ObjectUtils.isEmpty(list) && !list.get(0).getId().equals(baseTmpldelvTypeUpdateForm.getId())){
            throw new BaseException("交付物模板编号不能重复!");
        }
        this.dozerMapper.map(baseTmpldelvTypeUpdateForm,baseTmpldelvTypePo);
        this.updateById(baseTmpldelvTypePo);
        return baseTmpldelvTypePo;
    }

    @Override
    public void deleteTmpldelvTypeById(Integer id){
        this.deleteById(id);
    }

    public List<BaseTmpldelvTypePo> queryBaseTmpldelvTypePoByTypeNum(String typeNum){
        Example example = new Example(BaseTmpldelvTypePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("typeNum",typeNum);
        List<BaseTmpldelvTypePo> list = this.selectByExample(example);
        return !ObjectUtils.isEmpty(list) ? list : null;
    }

}
