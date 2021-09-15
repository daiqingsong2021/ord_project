package com.wisdom.acm.hbase.form;

import lombok.Data;

@Data
public class SysSearchAuditForm {

    private String searcher;

    private String startTime;

    private String endTime;

    //标记日志类型（0：管理员日志 1：普通用户日志）
    private Integer typeFlag;

    // 记录 时间
    private String recordTime;
    // 线路
    private String line;
    // 记录 id
    private String recordId;
    // 记录状态  0:新建  1：已完成
    private String recordStatus;

}
