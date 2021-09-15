package com.wisdom.acm.szxm.mapper.wtgl;

import com.wisdom.acm.szxm.po.wtgl.QuestionRecordPo;
import com.wisdom.acm.szxm.vo.wtgl.QuestionRecordVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

public interface QuestionRecordMapper extends CommMapper<QuestionRecordPo> {

    /**
     * 获取处理记录
     * @param
     * @return
     */
    List<QuestionRecordVo> selectQuestionRecords(Map<String, Object> mapWhere);

    List<QuestionRecordVo> selectAppQuestionRecords(Map<String, Object> mapWhere);

}
