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

/**
 * 主观评分对象
 */
@Data
public class SubjectScoreUpdForm {
    @NotNull(message = "文件ID不能为空")
    private Integer fileId;

    /**
     * 分值
     */
    @Range(max = 5, min = 0, message = "主观评分需在0-5 星之间")
    private BigDecimal score;
}
