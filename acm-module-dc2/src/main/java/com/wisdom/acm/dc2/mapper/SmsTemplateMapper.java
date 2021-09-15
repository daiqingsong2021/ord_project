package com.wisdom.acm.dc2.mapper;

import com.wisdom.acm.dc2.po.SmsTemplatePo;
import com.wisdom.acm.dc2.vo.SmsTemplateVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

public interface SmsTemplateMapper extends CommMapper<SmsTemplatePo> {

    List<SmsTemplateVo> querySmsTemplateList(Map<String, Object> mapWhere);
}
