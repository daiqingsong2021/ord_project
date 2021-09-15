package com.wisdom.acm.szxm.mapper.wf;

import com.wisdom.acm.szxm.po.wf.ActivitiTemplatePo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Author：wqd
 * Date：2020-03-30 9:35
 * Description：<描述>
 */
public interface ActivitiTemplateMapper extends CommMapper<ActivitiTemplatePo> {
    List<ActivitiTemplatePo> queryActivitiByIds(@Param("activitiIds") List<String> activitiIds);
}
