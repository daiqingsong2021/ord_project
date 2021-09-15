package com.wisdom.acm.sys.form;

import lombok.Data;

@Data
public class SysSearchAuditForm {

    private String searcher;

    private Integer thisTime;

    private String thisStartTime;

    private String startTime;

    private String endTime;

    private String thisEndTime;
}
