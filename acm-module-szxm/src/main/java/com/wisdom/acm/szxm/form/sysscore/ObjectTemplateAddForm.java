package com.wisdom.acm.szxm.form.sysscore;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * Author：wqd
 * Date：2020-01-02 14:00
 * Description：<描述>
 */
@Data
public class ObjectTemplateAddForm {
    /**
     * 考核项名称
     */
    @NotBlank(message = "考核项名称不能为空")
    private String checkTitle;

    /**
     * 主项最大分
     */
    @Range(max = 100, min = 0, message = "主项最大分需在0-100之间")
    private Integer maxScore;

    /**
     * 主项最小分
     */
    @Range(max = 100, min = 0, message = "主项最小分需在0-100之间")
    private Integer minScore;

    /**
     * 考核项编码（每一项的编码）
     */
    @NotBlank(message = "考核项编码不能为空")
    private String itemCode;

    /**
     * 考核主项(主项0，细项1)
     */
    @NotBlank(message = "是否为考核主项不能为空")
    private String mainItem;

    /**
     * 细项所属考核项id 为细项时 该数据不能为空
     */
    private Integer checkItemId;
    /**
     * 扣分标准
     */
    @Range(max = 100, min = 0, message = "扣分标准需在0-100之间")
    private BigDecimal deductionStandard;
}
