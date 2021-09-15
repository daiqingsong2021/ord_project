package com.wisdom.acm.szxm.vo.sysscore;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Author：wqd
 * Date：2019-12-31 13:31
 * Description：<描述>
 */

/**
 * 客观 分项得分
 */
@Data
public class ObjectScoreItemVo {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 细项所属考核项id
     */
    private Integer checkItemId;

    /**
     * 考核项名称
     */
    private String checkTitle;

    /**
     * 考核主项(主项0，细项1)
     */
    private String mainItem;

    /**
     * 考核项目个数
     */
    private Integer itemCount;

    /**
     * 主项最大分
     */
    private Integer maxScore;

    /**
     * 主项最小分
     */
    private Integer minScore;

    /**
     * 违规次数
     */
    private Integer violateCount;

    /**
     * 考核项编码
     */
    private String itemCode;

    /**
     * 扣减/实得分数
     */
    private BigDecimal score;

    /**
     * 扣分标准
     */
    private BigDecimal deductionStandard;
}
