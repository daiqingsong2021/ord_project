package com.wisdom.acm.processing.form;

import lombok.Data;

/**
 * @author zll
 * 2020/12/24/024 10:13
 * Description:<描述>
 */
@Data
public class FineDailyReportAddForm {
    private String reportDay;
    private String line;
    //'日报类型：0：定时器生成；1：人员生成'
    private String reportType;
    //'审批状态'
    private String reviewStatus;
    //'备注'
    private String description;
}
