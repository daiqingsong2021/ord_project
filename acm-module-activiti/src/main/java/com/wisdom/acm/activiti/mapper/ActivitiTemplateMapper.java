package com.wisdom.acm.activiti.mapper;

import com.wisdom.acm.activiti.po.ActivitiTemplatePo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Author：wqd
 * Date：2020-03-30 9:35
 * Description：<描述>
 */
public interface ActivitiTemplateMapper extends CommMapper<ActivitiTemplatePo> {
    ActivitiTemplatePo queryTemplateByActivitiId(@Param("activitiId") String activitiId);

    void deleteByActivitiId(@Param("activitiId") String activitiId);

    void insertTemplate(@Param("activitiTemplatePo") ActivitiTemplatePo activitiTemplatePo);

    List<ActivitiTemplatePo> queryActivitiByIds(@Param("activitiIds") List<String> activitiIds);
}
