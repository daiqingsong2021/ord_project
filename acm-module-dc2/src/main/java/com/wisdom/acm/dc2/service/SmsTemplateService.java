package com.wisdom.acm.dc2.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.SmsTemplateAddForm;
import com.wisdom.acm.dc2.po.SmsTemplatePo;
import com.wisdom.acm.dc2.vo.SmsTemplateVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface SmsTemplateService extends CommService<SmsTemplatePo> {

    //分页查询
    PageInfo<SmsTemplateVo> querySmsTemplatePageList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    //不分页查询
    List<SmsTemplateVo> querySmsTemplateList(Map<String, Object> mapWhere);

    //查询详情
    SmsTemplateVo getSmsTemplateDetail(Integer id);

    //新增
    void insertSmsTemplate(SmsTemplateAddForm form);

    //删除
    void delSmsTemplate(List<Integer> ids);
}
