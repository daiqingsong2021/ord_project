package com.wisdom.acm.szxm.vo.wtgl;

import lombok.Data;

import java.util.List;

/**
 * Author：wqd
 * Date：2019-12-04 14:09
 * Description：<描述>
 */
@Data
/**
 * 问题分类统计对象
 */
public class FltjQuestionVo {
    /**
     * 问题--类型
     */
    private List<QuestionClassVo> questionClassVos;

    /***
     * 问题 -- 月份
     */
    private List<QuestionMonthVo> questionMonthVos;
}
