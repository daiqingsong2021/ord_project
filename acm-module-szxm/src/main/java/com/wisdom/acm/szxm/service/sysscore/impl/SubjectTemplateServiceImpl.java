package com.wisdom.acm.szxm.service.sysscore.impl;

import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.form.sysscore.SubjectTemplateAddForm;
import com.wisdom.acm.szxm.form.sysscore.SubjectTemplateUpdForm;
import com.wisdom.acm.szxm.mapper.sysscore.SubjectTemplateMapper;
import com.wisdom.acm.szxm.po.sysscore.SubjectTemplatePo;
import com.wisdom.acm.szxm.service.sysscore.SubjectTemplateService;
import com.wisdom.acm.szxm.vo.sysscore.SubjectTemplateVo;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2019-12-30 16:55
 * Description：<描述>
 */
@Service
@Slf4j
public class SubjectTemplateServiceImpl extends BaseService<SubjectTemplateMapper, SubjectTemplatePo>  implements SubjectTemplateService {

    @Override
    public List<SubjectTemplateVo> selectSubjectTemplate(Map<String, Object> mapWhere) {
        return mapper.selectSubjectTemplateList(mapWhere);
    }

    @Override
    public SubjectTemplateVo addSubjectTemplate(SubjectTemplateAddForm subjectTemplateAddForm) {
        SubjectTemplatePo subjectTemplatePo = dozerMapper.map(subjectTemplateAddForm, SubjectTemplatePo.class);
        super.insert(subjectTemplatePo);
        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("id", subjectTemplatePo.getId());
        List<SubjectTemplateVo> subjectTemplateVos = mapper.selectSubjectTemplateList(mapWhere);
        return subjectTemplateVos.get(0);
    }

    @Override
    public SubjectTemplateVo updSubjectTemplate(SubjectTemplateUpdForm subjectTemplateUpdForm) {
        SubjectTemplatePo subjectTemplatePo = dozerMapper.map(subjectTemplateUpdForm, SubjectTemplatePo.class);
        //根据ID更新po，值为null的不更新，只更新不为null的值
        super.updateSelectiveById(subjectTemplatePo);
        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("id", subjectTemplatePo.getId());
        List<SubjectTemplateVo> subjectTemplateVos = mapper.selectSubjectTemplateList(mapWhere);
        return subjectTemplateVos.get(0);
    }

    @Override
    public void deleteSubjectTemplate(List<Integer> ids) {
        this.deleteByIds(ids);
    }

    /**
     * 根据业务模块编码判断文档是否需要主观评分
     * @param moduleCode
     * @return
     */
    @Override
    public Map<String, Integer> isInSubjectTemplate(String moduleCode) {
        List<SubjectTemplateVo> inSubjectTemplate = mapper.isInSubjectTemplate(moduleCode);
        Map<String, Integer> returnMap = Maps.newHashMap();
        if(ObjectUtils.isEmpty(inSubjectTemplate)){
            //若该编码在主观模板表中不存在，不需要进行评分 返回0
            returnMap.put("isRated", 0);
            return returnMap;
        }
        //需要进行评分 返回1
        returnMap.put("isRated", 1);
        return returnMap;
    }
}
