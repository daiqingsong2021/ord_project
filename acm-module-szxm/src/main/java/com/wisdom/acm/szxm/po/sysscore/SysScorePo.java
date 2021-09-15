package com.wisdom.acm.szxm.po.sysscore;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Author：wqd
 * Date：2019-12-31 16:42
 * Description：<描述>
 */
/**
 * 系统评分____考评得分
 */
@Table(name = "szxm_sys_score")
@Data
public class SysScorePo extends BasePo {
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
     * 总分
     */
    @Column(name = "total_score")
    private BigDecimal totalScore;

    /**
     * 客观得分
     */
    @Column(name = "objective_score")
    private BigDecimal objectiveScore;

    /**
     * 主观得分
     */
    @Column(name = "subjective_score")
    private BigDecimal subjectiveScore;

    /**
     * 是否超过80
     * 0通过；1未通过
     */
    @Column(name = "is_pass")
    private String isPass;

    /**
     * 状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 发起人ID
     */
    @Column(name = "initiator_id")
    private Integer initiatorId;

    /**
     * 发起时间
     */
    @Column(name = "init_time")
    private Date initTime;
}
