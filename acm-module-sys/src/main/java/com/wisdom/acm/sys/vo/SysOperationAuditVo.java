package com.wisdom.acm.sys.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SysOperationAuditVo {

    private Integer id;

    //日志类型
    private String loggerType;

    //应用名称
    private String applicationName;

    //模块名称
    private String moduleName;

    //操作名称
    private String operationName;

    //操作结果
    private String operationResult;

    //操作时间
    private Date creatTime;

    //操作人
    private String operationUser;

    //异常信息
    private String exception;

    //操作描述
    private String operationDesc;

    //访问ip地址
    private String ipAddress;

    private String userName;

    private String actuName;
}
