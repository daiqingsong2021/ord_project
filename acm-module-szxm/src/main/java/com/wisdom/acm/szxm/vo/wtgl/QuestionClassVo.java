package com.wisdom.acm.szxm.vo.wtgl;

import lombok.Data;

/**
 * Author：wqd
 * Date：2019-12-04 14:13
 * Description：<描述>
 */
@Data
/**
 * 问题类型对象
 */
public class QuestionClassVo {
    /**
     * 问题类型
     */
    private String issueType;

    /**
     *  问题类型编码
     */
    private String issueTypeCode;

    /**
     * 问题数量
     */
    private Integer issueQuantity;

    /**
     *  未关闭数量
     */
    private Integer unclosedQuantity;
}
