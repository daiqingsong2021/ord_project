package com.wisdom.base.common.po;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "wsd_logger_auth")
@Data
public class ApiLoggerAuth {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;

    @Column(name = "operation_time")
    private Date creatTime;

    @Column(name = "operation_user")
    private String wsdCreator;

    @Column(name = "operation_name")
    private String operaName;

    @Column(name = "ip_address")
    private String lastUpdIp;

    @Column(name = "logger_type")
    private String loggerType;

    @Column(name = "module_name")
    private String moduleName;

    @Column(name = "operation_result")
    private String operaResult;
}
