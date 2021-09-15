package com.wisdom.acm.dc5.service.wf.impl;

import com.google.common.collect.Maps;
import com.wisdom.acm.dc5.mapper.wf.ActivitiTemplateMapper;
import com.wisdom.acm.dc5.po.wf.ActivitiTemplatePo;
import com.wisdom.acm.dc5.service.wf.ActivitiTemplateService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2020-03-30 9:30
 * Description：<描述>
 */
@Service
@Slf4j
public class ActivitiTemplateServiceImpl extends BaseService<ActivitiTemplateMapper, ActivitiTemplatePo> implements ActivitiTemplateService {
    @Override
    public String queryActiviti(String activitiId) {
        Example example = new Example(ActivitiTemplatePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activitiId", activitiId);
        ActivitiTemplatePo templatePo = mapper.selectOneByExample(example);
        if (ObjectUtils.isEmpty(templatePo)) {
            return null;
        }
        return templatePo.getScreenCode();
    }

    @Override
    public Map<String, String> queryActivitiByIds(List<String> activitiIds) {
        Map<String, String> activitiIdCodeMap = Maps.newHashMap();
        if (!ObjectUtils.isEmpty(activitiIds)) {
            List<ActivitiTemplatePo> activitiTemplatePos = mapper.queryActivitiByIds(activitiIds);
            if(!ObjectUtils.isEmpty(activitiTemplatePos)){
                for (ActivitiTemplatePo templatePo:activitiTemplatePos) {
                    activitiIdCodeMap.put(templatePo.getActivitiId(), templatePo.getScreenCode());
                }
            }
        }
        return activitiIdCodeMap;
    }
}
