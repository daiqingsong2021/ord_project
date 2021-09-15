package com.wisdom.acm.szxm.service.wtgl.impl;

import com.google.common.collect.Lists;
import com.wisdom.acm.szxm.mapper.wtgl.QuestionRecordMapper;
import com.wisdom.acm.szxm.po.rygl.PeoplePo;
import com.wisdom.acm.szxm.po.wtgl.QuestionRecordPo;
import com.wisdom.acm.szxm.service.wtgl.QuestionRecordService;
import com.wisdom.acm.szxm.vo.wtgl.QuestionRecordVo;
import com.wisdom.base.common.feign.CommFileService;
import com.wisdom.base.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class QuestionRecordServiceImpl extends BaseService<QuestionRecordMapper, QuestionRecordPo> implements QuestionRecordService
{

    @Autowired
    private CommFileService commFileService;

    @Override public List<QuestionRecordVo> selectQuestionRecords(Map<String, Object> mapWhere)
    {
        return mapper.selectQuestionRecords(mapWhere);
    }

    @Override public void deleteByQuestionId(List<Integer> quesIds)
    {
        Example example = new Example(QuestionRecordPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("questionId",quesIds);
        List<QuestionRecordPo> questionRecordPos = this.selectByExample(example);
        List<Integer> delIds = Lists.newArrayList();
        for (QuestionRecordPo questionRecordPo : questionRecordPos)
        {
            delIds.add(questionRecordPo.getId());
        }
        if (!ObjectUtils.isEmpty(delIds))
            this.deleteQuestionRecord(delIds);
    }

    @Override public List<QuestionRecordVo> selectAppQuestionRecords(Map<String, Object> mapWhere)
    {
        return mapper.selectAppQuestionRecords(mapWhere);
    }

    private void deleteQuestionRecord(List<Integer> delIds)
    {
        //删除关联文件信息
        commFileService.deleteDocFileRelationByBiz(delIds,"question-record");
        //删除本身
        this.deleteByIds(delIds);
    }
}
