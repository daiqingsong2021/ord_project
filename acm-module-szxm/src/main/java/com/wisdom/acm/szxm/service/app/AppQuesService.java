package com.wisdom.acm.szxm.service.app;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.po.wtgl.QuestionPo;
import com.wisdom.acm.szxm.vo.wtgl.QuestionVo;
import com.wisdom.base.common.service.CommService;

import java.util.Map;

public interface AppQuesService extends CommService<QuestionPo>
{
    /**
     * 查询问题
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    PageInfo<QuestionVo> selectAppQuestions(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    /**
     * 查询问题详情
     * @param id
     * @return
     */
    QuestionVo selectAppQuestionsById(Integer id);
}
