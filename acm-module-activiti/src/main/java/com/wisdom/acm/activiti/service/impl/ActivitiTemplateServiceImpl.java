package com.wisdom.acm.activiti.service.impl;

import com.google.common.collect.Maps;
import com.wisdom.acm.activiti.form.ActivitiTemplateAddForm;
import com.wisdom.acm.activiti.mapper.ActivitiTemplateMapper;
import com.wisdom.acm.activiti.po.ActivitiTemplatePo;
import com.wisdom.acm.activiti.service.ActivitiTemplateService;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2020-03-30 9:30
 * Description：<描述>
 */
@Service
@Slf4j
public class ActivitiTemplateServiceImpl  implements ActivitiTemplateService {
    @Autowired
    private LeafService leafService;
    @Autowired
    private ActivitiTemplateMapper mapper;

    @Override
    public void addActivitiTemplate(ActivitiTemplateAddForm activitiTemplateAddForm) {
        if (ObjectUtils.isEmpty(activitiTemplateAddForm.getScreenCode())) {
            //code为空时 不存入模板表
            return;
        }

        ActivitiTemplatePo templatePo = mapper.queryTemplateByActivitiId(activitiTemplateAddForm.getActivitiId());
        if (!ObjectUtils.isEmpty(templatePo) && !ObjectUtils.isEmpty(templatePo.getActivitiId())) {
            mapper.deleteByActivitiId(templatePo.getActivitiId());
        }
        ActivitiTemplatePo activitiTemplatePo = new  ActivitiTemplatePo();
        activitiTemplatePo.setActivitiId(activitiTemplateAddForm.getActivitiId());
        activitiTemplatePo.setScreenCode(activitiTemplateAddForm.getScreenCode());
        if(activitiTemplatePo.getId() == null){
            activitiTemplatePo.setId(leafService.getId());
        }
        mapper.insertTemplate(activitiTemplatePo);
    }

    @Override
    public String getActivitiTemplate(String activitiId) {
        if(ObjectUtils.isEmpty(activitiId)){
            return null;
        }
        ActivitiTemplatePo activitiTemplatePo = mapper.queryTemplateByActivitiId(activitiId);
        if (!ObjectUtils.isEmpty(activitiTemplatePo)){
            return activitiTemplatePo.getScreenCode();
        }
        return null;
    }

    /**
     * 根据流程节点id查询
     * @param activitiIds
     */
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
