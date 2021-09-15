package com.wisdom.acm.hbase.service.impl;

import com.wisdom.acm.hbase.form.SysSearchAuditForm;
import com.wisdom.acm.hbase.service.AcmLogService;
import com.wisdom.acm.hbase.vo.SysOperationAuditVo;
import com.wisdom.base.common.log.AcmLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AcmLogServiceImpl implements AcmLogService {

    @Autowired
    @Qualifier("phoenixJdbcTemplate")
    JdbcTemplate phoenixJdbcTemplate;

    @Async
    public Integer addLogger(AcmLogger acmLogger) {

        int res = phoenixJdbcTemplate.update("upsert into acm_cloud.wsd_logger(id,module,title,content,creat_time,creator,ipaddress,user_type,status,error,record_time,line,record_id,record_status) " +
                "values('" + acmLogger.getId() + "','" + acmLogger.getModule() + "','" + acmLogger.getTitle() + "','" + acmLogger.getContent() + "',TO_DATE('" + acmLogger.getCreatTime() + "'),'" +
                acmLogger.getCreator() + "','" + acmLogger.getIpaddress() + "','" + acmLogger.getUserType() + "','" + acmLogger.getStatus() + "','"  +
                acmLogger.getError() +  "',TO_DATE('"  + acmLogger.getRecordTime() +  "'),'" + acmLogger.getLine() + "','"+ acmLogger.getRecordId() + "','" + acmLogger.getRecordStatus() + "')");
        return Integer.valueOf(res);
    }


    public List<Map<String, Object>> query() {
        return phoenixJdbcTemplate.queryForList("select * from acm_cloud.wsd_logger");
    }

    @Override
    public List<SysOperationAuditVo> queryOperationAudit(SysSearchAuditForm searchMap, Integer pageSize, Integer currentPageNum) throws ParseException {

        String sql = " where 1=1 ";
        if (!ObjectUtils.isEmpty(searchMap.getStartTime()) && !ObjectUtils.isEmpty(searchMap.getEndTime())) {
            sql = sql + "and  TO_CHAR(CREAT_TIME,'yyyy-MM-dd HH:mm:ss') BETWEEN '" + searchMap.getStartTime() + "' and '" + searchMap.getEndTime() + "'";
        }
        if (!ObjectUtils.isEmpty(searchMap.getRecordTime())) {
            sql = sql + "and TO_CHAR(RECORD_TIME,'yyyy-MM-dd')='" + searchMap.getRecordTime()+ "'";
        }
        if (!ObjectUtils.isEmpty(searchMap.getLine()) ) {
            sql = sql + "and LINE='" +searchMap.getLine() + "'";
        }
        if (!ObjectUtils.isEmpty(searchMap.getRecordStatus()) ) {
            sql = sql + "and RECORD_STATUS='" +searchMap.getRecordStatus()+ "'";
        }
        if (!ObjectUtils.isEmpty(searchMap.getRecordId()) ) {
            sql = sql + "and RECORD_ID='" +searchMap.getRecordId()+ "'";
        }
        if (!ObjectUtils.isEmpty(searchMap.getSearcher())) {
            sql = sql + " and (title like '%" + searchMap.getSearcher() + "%' or module like '%" + searchMap.getSearcher() + "%' or creator like '%" + searchMap.getSearcher() + "%' or content like '%" + searchMap.getSearcher() + "%') ";
        }
        if (!ObjectUtils.isEmpty(searchMap.getTypeFlag())) {
            if (searchMap.getTypeFlag() == 0) {
                sql = sql + " and user_type != '1'";
            }
            if (searchMap.getTypeFlag() == 1) {
                sql = sql + " and user_type = '1' ";
            }
        }
        Integer startkey = pageSize * (currentPageNum - 1);
        sql = sql + " order by CREAT_TIME desc limit " + pageSize + " offset " + startkey + "";

        List<Map<String, Object>> list = phoenixJdbcTemplate.queryForList("select TITLE,MODULE,CONTENT,TO_CHAR(CREAT_TIME,'yyyy-MM-dd HH:mm:ss') CREATTIME," +
                "CREATOR,IPADDRESS IPADDRESS,STATUS,ERROR,TO_CHAR(RECORD_TIME,'yyyy-MM-dd') RECORDTIME,LINE,RECORD_ID,RECORD_STATUS from acm_cloud.wsd_logger" + sql);
        List<SysOperationAuditVo> retList = new ArrayList<>();
        SysOperationAuditVo sysOperationAuditVo = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (!ObjectUtils.isEmpty(list)) {
            for (Map<String, Object> map : list) {
                sysOperationAuditVo = new SysOperationAuditVo();
                sysOperationAuditVo.setIpAddress(String.valueOf(map.get("IPADDRESS")));
                sysOperationAuditVo.setOperationUser(String.valueOf(map.get("CREATOR")));
                sysOperationAuditVo.setModuleName(ObjectUtils.isEmpty(map.get("MODULE")) ? "" : String.valueOf(map.get("MODULE")).equals("null") ? "" : String.valueOf(map.get("MODULE")));
                sysOperationAuditVo.setOperationName(String.valueOf(map.get("TITLE")));
                sysOperationAuditVo.setOperationDesc(String.valueOf(map.get("CONTENT")));
                sysOperationAuditVo.setCreatTime(format.parse(String.valueOf(map.get("CREATTIME"))));
                sysOperationAuditVo.setException(ObjectUtils.isEmpty(map.get("ERROR")) ? "" : String.valueOf(map.get("ERROR")).equals("null") ? "" : String.valueOf(map.get("ERROR")));
                sysOperationAuditVo.setOperationResult(ObjectUtils.isEmpty(map.get("STATUS")) ? "" : String.valueOf(map.get("STATUS")));
                sysOperationAuditVo.setRecordTime(String.valueOf(map.get("RECORDTIME")));
                sysOperationAuditVo.setLine(String.valueOf(map.get("LINE")));
                sysOperationAuditVo.setRecordId(String.valueOf(map.get("RECORD_ID")));
                sysOperationAuditVo.setRecordStatus(String.valueOf(map.get("RECORD_STATUS")));
                if("INIT".equals(map.get("RECORD_STATUS")))
                {
                    sysOperationAuditVo.getRecordStatusVo().setName("新建");
                    sysOperationAuditVo.getRecordStatusVo().setCode("INIT");
                }
                else if("APPROVED".equals(map.get("RECORD_STATUS")))
                {
                    sysOperationAuditVo.getRecordStatusVo().setName("已完成");
                    sysOperationAuditVo.getRecordStatusVo().setCode("APPROVED");
                }
                else
                {
                    sysOperationAuditVo.getRecordStatusVo().setName("");
                    sysOperationAuditVo.getRecordStatusVo().setCode(String.valueOf(map.get("RECORD_STATUS")));
                }
                retList.add(sysOperationAuditVo);
            }
        }
        return retList;
    }
}
