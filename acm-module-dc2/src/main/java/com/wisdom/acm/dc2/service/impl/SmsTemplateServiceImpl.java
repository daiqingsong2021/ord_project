package com.wisdom.acm.dc2.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.SmsTemplateAddForm;
import com.wisdom.acm.dc2.mapper.SmsTemplateMapper;
import com.wisdom.acm.dc2.po.SmsTemplatePo;
import com.wisdom.acm.dc2.service.SmsTemplateService;
import com.wisdom.acm.dc2.vo.SmsTemplateVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SmsTemplateServiceImpl extends BaseService<SmsTemplateMapper, SmsTemplatePo> implements SmsTemplateService {

    @Override
    public PageInfo<SmsTemplateVo> querySmsTemplatePageList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<SmsTemplateVo> voList = querySmsTemplateList(mapWhere);
        PageInfo<SmsTemplateVo> pageInfo = new PageInfo<SmsTemplateVo>(voList);
        return pageInfo;
    }

    @Override
    public List<SmsTemplateVo> querySmsTemplateList(Map<String, Object> mapWhere) {
        return mapper.querySmsTemplateList(mapWhere);
    }

    @Override
    public void insertSmsTemplate(SmsTemplateAddForm form) {
        SmsTemplatePo smsTemplatePo = dozerMapper.map(form, SmsTemplatePo.class);
        String templateTitle = smsTemplatePo.getTemplateTitle();
        Map<String,Object> mapWhere = new HashMap<>();
        mapWhere.put("templateTitle", templateTitle);
        List<SmsTemplateVo> smsTemplateVoList = querySmsTemplateList(mapWhere);
        if(!ObjectUtils.isEmpty(smsTemplateVoList)){
            throw new BaseException("重复的模板标题");
        }
        super.insert(smsTemplatePo);
    }

    @Override
    public void delSmsTemplate(List<Integer> ids) {
        super.deleteByIds(ids);
    }

    @Override
    public SmsTemplateVo getSmsTemplateDetail(Integer id) {
        Map<String,Object> mapWhere = new HashMap<>();
        mapWhere.put("id", id);
        List<SmsTemplateVo> smsTemplateVoList = querySmsTemplateList(mapWhere);
        if(!ObjectUtils.isEmpty(smsTemplateVoList)){
            return smsTemplateVoList.get(0);
        }
        return null;
    }
}
