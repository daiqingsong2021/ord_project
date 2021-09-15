package com.wisdom.acm.szxm.mapper.sysscore;

import com.wisdom.acm.szxm.po.sysscore.ObjectTemplatePo;
import com.wisdom.acm.szxm.vo.sysscore.ObjectScoreItemVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2019-12-30 16:51
 * Description：<描述>
 */
public interface ObjectTemplateMapper extends CommMapper<ObjectTemplatePo> {
    List<ObjectScoreItemVo> selectObjectTemplate(Map<String, Object> mapWhere);
}
