package com.wisdom.acm.sys.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Mr.CP
 * @date 2019-02-26 13:56:08
 */
@Table(name = "wsd_sys_oreration_audit")
@Data
public class SysOperationAuditPo extends BasePo {

    //日志类型
    @Column(name = "logger_type")
    private String loggerType;

    //应用名称
    @Column(name = "application_name")
    private String applicationName;

    //模块名称
    @Column(name = "module_name")
    private String moduleName;

    //操作名称
    @Column(name = "operation_name")
    private String operationName;

    //操作结果
    @Column(name = "operation_result")
    private String operationResult;

    //操作人
    @Column(name = "operation_user")
    private String operationUser;

    //异常信息
    @Column(name = "exception")
    private String exception;

    //操作描述
    @Column(name = "operation_desc")
    private String operationDesc;

    //访问ip地址
    @Column(name = "ip_address")
    private String ipAddress;


}


