package com.wisdom.acm.processing.po.report;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author zll
 * 2020/8/17/017 13:59
 * Description:<日报数据>
 */
@Table(name = "odr_daily_report_main")
@Data
public class DailyReportPo extends BasePo {
    //'日报名称'
    @Column(name = "report_name")
    private Date reportName;

    //'日报名称'
    @Column(name = "line")
    private String line;

    //'日报类型：0：运营日报；1：指挥中心日报'
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

    //'文件id'
    @Column(name = "file_id")
    private Integer fileId;

    //'word'
    @Column(name = "word_file")
    private String wordFile;

    //'pdf'
    @Column(name = "pdf_file")
    private String pdfFile;


    @Column(name = "load_id")
    private Integer loadId;
}
