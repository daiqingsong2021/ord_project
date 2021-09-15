package com.wisdom.acm.sys.form;

import lombok.Data;

import java.util.Date;


@Data
public class SysViewSearchForm {

    //名称/代码
    private String nameOrCode;

    private Integer orgId;

    private Integer userId;

    private Date planStartTime;

    private Date planEndTime;

    private Date startTime;

    private Date endTime;

    //计划完成
    private String planEndType;

    //空值
    private String nullValue;

    //反馈状态
    private String feedbackStatus;

    //计划状态
    private String planStatus;

    //子节点
    private boolean childNode;

    //模糊查询
    private boolean fuzzySearch;
}
