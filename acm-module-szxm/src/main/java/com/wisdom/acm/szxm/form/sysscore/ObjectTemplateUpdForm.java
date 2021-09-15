package com.wisdom.acm.szxm.form.sysscore;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Author：wqd
 * Date：2020-01-02 14:00
 * Description：<描述>
 */
@Data
public class ObjectTemplateUpdForm {
    @NotNull(message = "主键ID不能为空")
    private Integer id;

    /**
     * 考核项名称
     */
    private String checkTitle;

    /**
     * 主项最大分
     */
    @Range(max = 100, min = 0)
    private Integer maxScore;

    /**
     * 主项最小分
     */
    @Range(max = 100, min = 0)
    private Integer minScore;

    /**
     * 扣分标准
     */
    @Range(max = 100, min = 0)
    private BigDecimal deductionStandard;
}
