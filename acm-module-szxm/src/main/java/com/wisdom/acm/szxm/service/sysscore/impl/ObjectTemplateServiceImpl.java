package com.wisdom.acm.szxm.service.sysscore.impl;

import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.form.sysscore.ObjectTemplateAddForm;
import com.wisdom.acm.szxm.form.sysscore.ObjectTemplateUpdForm;
import com.wisdom.acm.szxm.mapper.sysscore.ObjectTemplateMapper;
import com.wisdom.acm.szxm.po.sysscore.ObjectTemplatePo;
import com.wisdom.acm.szxm.service.sysscore.ObjectTemplateService;
import com.wisdom.acm.szxm.vo.sysscore.ObjectScoreItemVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2019-12-30 16:55
 * Description：<描述>
 */
@Service
@Slf4j
public class ObjectTemplateServiceImpl extends BaseService<ObjectTemplateMapper, ObjectTemplatePo> implements ObjectTemplateService {
    @Override
    public List<ObjectScoreItemVo> selectMainItemObjectTemplates() {
        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("mainItem", "0");
        List<ObjectScoreItemVo> objectScoreItemVos = mapper.selectObjectTemplate(mapWhere);
        if (ObjectUtils.isEmpty(objectScoreItemVos)) {
            return null;
        }
        for (ObjectScoreItemVo objectScoreItemVo : objectScoreItemVos) {
            List<ObjectScoreItemVo> scoreItemVos = selectDetailItemObjectTemplates(objectScoreItemVo.getId());
            objectScoreItemVo.setItemCount(scoreItemVos.size());
        }
        return objectScoreItemVos;
    }

    @Override
    public List<ObjectScoreItemVo> selectDetailItemObjectTemplates(Integer checkItemId) {
        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("checkItemId", checkItemId);
        List<ObjectScoreItemVo> objectScoreItemVos = mapper.selectObjectTemplate(mapWhere);
        return objectScoreItemVos;
    }

    @Override
    public ObjectScoreItemVo addObjectTemplate(ObjectTemplateAddForm objectTemplateAddForm) {
        ObjectTemplatePo objectTemplatePo = dozerMapper.map(objectTemplateAddForm, ObjectTemplatePo.class);
        if ("0".equals(objectTemplatePo.getMainItem())) {
            if (ObjectUtils.isEmpty(objectTemplatePo.getMaxScore()) || ObjectUtils.isEmpty(objectTemplatePo.getMinScore())) {
                throw new BaseException("考核主项需指定打分范围");
            }
            if (objectTemplatePo.getMaxScore() <= objectTemplatePo.getMinScore()) {
                throw new BaseException("考核主项打分范围最大分需大于最小分");
            }
        }
        //当插入项为细项时 细项所属考核项id 不能为空
        if ("1".equals(objectTemplatePo.getMainItem()) && ObjectUtils.isEmpty(objectTemplatePo.getCheckItemId())) {
            throw new BaseException("考核细项需指定所在主项");
        }
        super.insert(objectTemplatePo);
        ObjectScoreItemVo objectScoreItemVo = dozerMapper.map(objectTemplatePo, ObjectScoreItemVo.class);
        return objectScoreItemVo;
    }

    @Override
    public ObjectScoreItemVo updObjectTemplate(ObjectTemplateUpdForm objectTemplateUpdForm) {
        ObjectTemplatePo objectTemplatePo = dozerMapper.map(objectTemplateUpdForm, ObjectTemplatePo.class);
        //根据ID更新po，值为null的不更新，只更新不为null的值
        super.updateSelectiveById(objectTemplatePo);
        ObjectScoreItemVo objectScoreItemVo = dozerMapper.map(objectTemplatePo, ObjectScoreItemVo.class);
        return objectScoreItemVo;
    }

    @Override
    public void deleteObjectTemplate(List<Integer> ids) {
        Example example = new Example(ObjectTemplatePo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("checkItemId", ids);
        List<ObjectTemplatePo> templatePos = mapper.selectByExample(example);
        if (!ObjectUtils.isEmpty(templatePos)) {
            throw new BaseException("无法删除该考核主项，其下有考核细项存在");
        }
        this.deleteByIds(ids);
    }
}
