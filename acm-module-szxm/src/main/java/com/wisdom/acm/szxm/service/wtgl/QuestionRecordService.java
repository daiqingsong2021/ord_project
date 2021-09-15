package com.wisdom.acm.szxm.service.wtgl;

import com.wisdom.acm.szxm.po.wtgl.QuestionRecordPo;
import com.wisdom.acm.szxm.vo.wtgl.QuestionRecordVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface QuestionRecordService extends CommService<QuestionRecordPo>
{
    /**
     * 获取问题记录列表,包含fileIds
     * @param mapWhere
     * @return
     */
    List<QuestionRecordVo> selectQuestionRecords(Map<String, Object> mapWhere);

    /**
     * 删除问题
     * @param quesIds
     */
    void deleteByQuestionId(List<Integer> quesIds);

    /**
     * 查询问题记录列表
     * @param mapWhere
     * @return
     */
    List<QuestionRecordVo> selectAppQuestionRecords(Map<String, Object> mapWhere);
}
