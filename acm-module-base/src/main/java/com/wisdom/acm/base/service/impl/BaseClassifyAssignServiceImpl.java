package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.form.classify.BaseClassifyAssignForm;
import com.wisdom.acm.base.form.classify.BaseUpdateClassifyAssignForm;
import com.wisdom.acm.base.mapper.BaseClassifyAssignMapper;
import com.wisdom.acm.base.po.BaseClassifyAssignPo;
import com.wisdom.acm.base.po.BaseClassifyPo;
import com.wisdom.acm.base.service.BaseClassifyAssignService;
import com.wisdom.acm.base.service.BaseClassifyService;
import com.wisdom.acm.base.vo.classify.BaseClassifyAssignVo;
import com.wisdom.acm.base.vo.classify.BaseClassifyTreeVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BaseClassifyAssignServiceImpl extends BaseService<BaseClassifyAssignMapper, BaseClassifyAssignPo> implements BaseClassifyAssignService {

    @Autowired
    private BaseClassifyService classifyService;

    @Override
    public List<BaseClassifyAssignVo> queryClassifyAssignListByBoCodeAndBoId(String boCode, Integer bizId) {
        List<BaseClassifyAssignVo> retList = this.mapper.selectClassifyAssignDateListByBoCodeAndBoId(boCode,bizId);
        return retList;
    }

    @Override
    @AddLog(title = "分配分类码" , module = LoggerModuleEnum.NONE)
    public BaseClassifyAssignVo assignClassify(BaseClassifyAssignForm classifyAssignForm) {
        BaseClassifyAssignPo baseClassifyAssignPo = this.dozerMapper.map(classifyAssignForm, BaseClassifyAssignPo.class);
        //码值id
        Integer classifyId =  classifyAssignForm.getClassifyId();
        String name = classifyService.selectById(classifyId).getClassifyName();
        String logger = "分配分类码,分类码码值为:"+ name ;
        this.setAcmLogger(new AcmLogger(logger));
        super.insert(baseClassifyAssignPo);
        return getAssignVo(baseClassifyAssignPo);
    }

    private BaseClassifyAssignVo getAssignVo(BaseClassifyAssignPo baseClassifyAssignPo) {
        BaseClassifyPo type = this.classifyService.selectById(baseClassifyAssignPo.getClassifyTypeId());
        BaseClassifyPo classify = this.classifyService.selectById(baseClassifyAssignPo.getClassifyId());

        BaseClassifyAssignVo assignVo = new BaseClassifyAssignVo();
        assignVo.setId(baseClassifyAssignPo.getId())//
                .setBoCode(baseClassifyAssignPo.getBoCode())//
                .setBizId(baseClassifyAssignPo.getBizId())  //
                .setClassifyType(this.dozerMapper.map(type, BaseClassifyTreeVo.class))
                .setClassify(this.dozerMapper.map(classify, BaseClassifyTreeVo.class))
        ;
        return assignVo;
    }

    @Override
    @AddLog(title = "修改分类码", module = LoggerModuleEnum.NONE)
    public BaseClassifyAssignVo updateAssignClassify(BaseUpdateClassifyAssignForm updateClassifyAssignForm){
        BaseClassifyAssignPo baseClassifyAssignPo = this.selectById(updateClassifyAssignForm.getId());
        if(ObjectUtils.isEmpty(baseClassifyAssignPo)){
            throw new BaseException("修改的分类码码值不存在!");
        }
        // 添加修改日志
        this.addChangeLogger(updateClassifyAssignForm,baseClassifyAssignPo);
        baseClassifyAssignPo.setClassifyId(updateClassifyAssignForm.getClassifyId());
        super.updateById(baseClassifyAssignPo);
        return getAssignVo(baseClassifyAssignPo);
    }

    @Override
    public void deleteClassifyAssign(List<Integer> ids) {
        super.deleteByIds(ids);
    }

    @Override
    public String queryClassifyNamesByIds(List<Integer> ids){
        List<BaseClassifyAssignPo> classifyAssignList = this.selectByIds(ids);
        List<Integer> classifyIds = ListUtil.toValueList(classifyAssignList,"classifyId",Integer.class);
        List<BaseClassifyPo> classifyList =  classifyService.selectByIds(classifyIds);
        String names = ListUtil.listToNames(classifyList,"classifyName");
        return names;
    }

    @Override
    public List<BaseClassifyAssignPo> queryClassifyAssignByClassifyId(List<Integer> classifyIds){
        Example example = new Example(BaseClassifyAssignPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("classifyId",classifyIds);
        List<BaseClassifyAssignPo> list = this.selectByExample(example);
        return !ObjectUtils.isEmpty(list) ? list : null;
    }
}
