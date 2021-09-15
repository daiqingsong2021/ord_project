package com.wisdom.acm.hbase.vo;

import com.wisdom.base.common.vo.GeneralVo;
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

    // 记录 时间
    private String recordTime;
    // 线路
    private String line;
    // 记录 id
    private String recordId;
    // 记录状态  0:新建  1：已完成
    private String recordStatus;

    /**
     * 状态
     */
    private GeneralVo recordStatusVo = new GeneralVo();
}
