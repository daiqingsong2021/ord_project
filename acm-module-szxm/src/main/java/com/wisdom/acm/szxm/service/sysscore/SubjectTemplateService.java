package com.wisdom.acm.szxm.service.sysscore;

import com.wisdom.acm.szxm.form.sysscore.SubjectTemplateAddForm;
import com.wisdom.acm.szxm.form.sysscore.SubjectTemplateUpdForm;
import com.wisdom.acm.szxm.po.sysscore.SubjectTemplatePo;
import com.wisdom.acm.szxm.vo.sysscore.SubjectTemplateVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2019-12-30 16:53
 * Description：<描述>
 */
public interface SubjectTemplateService extends CommService<SubjectTemplatePo> {

    List<SubjectTemplateVo> selectSubjectTemplate(Map<String, Object> mapWhere);

    SubjectTemplateVo addSubjectTemplate(SubjectTemplateAddForm subjectTemplateAddForm);

    SubjectTemplateVo updSubjectTemplate(SubjectTemplateUpdForm subjectTemplateUpdForm);

    void deleteSubjectTemplate (List<Integer> ids);

    Map<String, Integer> isInSubjectTemplate (String moduleCode);
}
