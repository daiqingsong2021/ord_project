package com.wisdom.acm.hbase.service;

import com.wisdom.acm.hbase.form.SysSearchAuditForm;
import com.wisdom.acm.hbase.vo.SysOperationAuditVo;
import com.wisdom.base.common.log.AcmLogger;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface AcmLogService   {

    Integer addLogger(AcmLogger acmLogger);

    List<Map<String, Object>> query();

    List<SysOperationAuditVo> queryOperationAudit(SysSearchAuditForm searchMap, Integer pageSize, Integer currentPageNum) throws ParseException;
}
