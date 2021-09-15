package com.wisdom.acm.szxm.service.sysscore;

import com.wisdom.acm.szxm.form.sysscore.ObjectTemplateAddForm;
import com.wisdom.acm.szxm.form.sysscore.ObjectTemplateUpdForm;
import com.wisdom.acm.szxm.po.sysscore.ObjectTemplatePo;
import com.wisdom.acm.szxm.vo.sysscore.ObjectScoreItemVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

/**
 * Author：wqd
 * Date：2019-12-30 16:53
 * Description：<描述>
 */
public interface ObjectTemplateService extends CommService<ObjectTemplatePo> {
    List<ObjectScoreItemVo> selectMainItemObjectTemplates();

    List<ObjectScoreItemVo> selectDetailItemObjectTemplates(Integer checkItemId);

    ObjectScoreItemVo addObjectTemplate(ObjectTemplateAddForm objectTemplateAddForm);

    ObjectScoreItemVo updObjectTemplate(ObjectTemplateUpdForm objectTemplateUpdForm);

    void deleteObjectTemplate (List<Integer> ids);
}
