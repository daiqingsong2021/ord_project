package com.wisdom.base.common.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 实体对象的父类，含有扩展字段
 * @author SQ
 */
@MappedSuperclass
@Data
public class ExtendedColumnPo extends BasePo {
    /**
     * 扩展字段1
     */
    @Column(name="extended_column_1")
    private String extendedColumn1;

    /**
     * 扩展字段2
     */
    @Column(name="extended_column_2")
    private String extendedColumn2;

    /**
     * 扩展字段3
     */
    @Column(name="extended_column_3")
    private String extendedColumn3;

    /**
     * 扩展字段4
     */
    @Column(name="extended_column_4")
    private String extendedColumn4;

    /**
     * 扩展字段5
     */
    @Column(name="extended_column_5")
    private String extendedColumn5;

    /**
     * sectionStatus 标段状态
     *
     */
    @Column(name="section_status")
    private String sectionStatus;

    /**
     * 标段开工日期
     *
     */
    @Column(name="start_date")
    private Date startDate;

    /**
     * 标段完工日期
     *
     */
    @Column(name="end_date")
    private Date endDate;

    /**
     * 是否启用派工单
     * 1 是 0 否
     */
    @Column(name="open_pgd")
    private String openPgd;

    /**
     * 派工单开始日期
     */
    @Column(name="pgd_start_date")
    private Date pgdStartDate;

    /**
     * 派工单结束日期
     */
    @Column(name="pgd_end_date")
    private Date pgdEndDate;

    /**
     * 是否启用信息化考核
     * 1 是 0 否
     */
    @Column(name="open_exam")
    private String openExam;

    /**
     * 信息化考核开始日期
     */
    @Column(name="exam_start_date")
    private Date examStartDate;

    /**
     * 信息化考核结束日期
     */
    @Column(name="exam_end_date")
    private Date examEndDate;

    /**
     * 品高 标段ID
     */
    @Column(name = "pg_section_id")
    private String pgSectionId;
}
