//package com.wisdom.acm.sys.po;
//
//import com.wisdom.base.common.util.UUIdGenId;
//import lombok.Data;
//import tk.mybatis.mapper.annotation.KeySql;
//
//import javax.persistence.Column;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import java.util.Date;
//
//@Table(name = "wsd_sys_logger_auth")
//@Data
//public class ApiLoggerAuth {
//
//    @Column(name = "id")
//    @KeySql(genId = UUIdGenId.class)
//    private Long id;
//
//    @Column(name = "operation_time")
//    private Date creatTime;
//
//    @Column(name = "operation_user")
//    private String wsdCreator;
//
//    @Column(name = "operation_name")
//    private String operaName;
//
//    @Column(name = "ip_address")
//    private String lastUpdIp;
//
//    @Column(name = "logger_type")
//    private String loggerType;
//
//    @Column(name = "module_name")
//    private String moduleName;
//
//    @Column(name = "operation_result")
//    private String operaResult;
//}
