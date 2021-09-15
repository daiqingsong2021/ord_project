package com.wisdom.acm.szxm.mapper.sysscore;

import com.wisdom.acm.szxm.po.sysscore.SubjectTemplatePo;
import com.wisdom.acm.szxm.vo.sysscore.SubjectTemplateVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2019-12-30 16:51
 * Description：<描述>
 */
public interface SubjectTemplateMapper extends CommMapper<SubjectTemplatePo> {
    List<SubjectTemplateVo> selectSubjectTemplateList(Map<String, Object> mapWhere);

    List<SubjectTemplateVo> isInSubjectTemplate(@Param("moduleCode") String moduleCode);
}
