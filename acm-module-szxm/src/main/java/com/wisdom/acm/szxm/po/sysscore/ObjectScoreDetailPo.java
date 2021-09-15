package com.wisdom.acm.szxm.po.sysscore;

/**
 * Author：wqd
 * Date：2019-12-30 16:19
 * Description：<描述>
 */

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 系统评分____客观评分明细表
 */
@Table(name = "szxm_sys_objectivescore_detail")
@Data
public class ObjectScoreDetailPo extends BasePo {
    /**
     * 项目ID
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 标段ID
     */
    @Column(name = "section_id")
    private Integer sectionId;

    /**
     * 评分所属年份
     */
    @Column(name = "year")
    private Integer year;

    /**
     * 评分所属月份
     */
    @Column(name = "month")
    private Integer month;

    /**
     * 考核项编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 扣分次数
     */
    @Column(name = "deduction_count")
    private Integer deductionCount;

    /**
     * 扣分值
     */
    @Column(name = "deduction")
    private BigDecimal deduction;
}
