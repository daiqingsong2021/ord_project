package com.wisdom.acm.szxm.vo.wtgl;

import lombok.Data;

/**
 * Author：wqd
 * Date：2019-12-04 14:18
 * Description：<描述>
 */
@Data
/**
 * 问题月份对象
 */
public class QuestionMonthVo {
    /**
     *月份
     */
    private String month;

    /**
     * 问题数量
     */
    private Integer monthIssueQuantity;
}
