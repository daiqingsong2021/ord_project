package com.wisdom.acm.szxm.po.sysscore;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Author：wqd
 * Date：2019-12-30 16:04
 * Description：<描述>
 */

/**
 * 系统评分____客观模板表
 */
@Table(name = "szxm_sys_objective_template")
@Data
public class ObjectTemplatePo extends BasePo {
    /**
     * 考核项名称
     */
    @Column(name = "check_title")
    private String checkTitle;

    /**
     * 主项最大分
     */
    @Column(name = "max_score")
    private Integer maxScore;

    /**
     * 主项最小分
     */
    @Column(name = "min_score")
    private Integer minScore;

    /**
     * 考核主项(主项0，细项1)
     */
    @Column(name = "main_item")
    private String mainItem;

    /**
     * 考核项编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 细项的主项id
     */
    @Column(name = "check_item_id")
    private Integer checkItemId;

    /**
     * 扣分标准
     */
    @Column(name = "deduction_standard")
    private BigDecimal deductionStandard;
}
