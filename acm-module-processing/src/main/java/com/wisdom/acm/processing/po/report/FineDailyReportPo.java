package com.wisdom.acm.processing.po.report;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author zll
 * 2020/12/24/024 9:55
 * Description:<描述>
 */
@Table(name = "fine_daily_report")
@Data
public class FineDailyReportPo extends BasePo {
    //'日报名称'
    @Column(name = "report_name")
    private Date reportName;
    @Column(name = "line")
    private String line;

    //'日报类型：0：定时器生成；1：人员生成'
    @Column(name = "report_type")
    private String reportType;

    //'审批人'
    @Column(name = "reviewer")
    private Integer reviewer;

    //'审批状态'
    @Column(name = "review_status")
    private String reviewStatus;

    //'备注'
    @Column(name = "description")
    private String description;
}
