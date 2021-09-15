package com.wisdom.acm.szxm.mapper.rygl;

import com.wisdom.acm.szxm.po.rygl.AllKqRecordPo;
import com.wisdom.acm.szxm.po.rygl.GlryKqRecordPo;
import com.wisdom.acm.szxm.po.rygl.LwryKqRecordPo;
import com.wisdom.acm.szxm.vo.rygl.HolidayVo;
import com.wisdom.acm.szxm.vo.rygl.KqDayReportDetailVo;
import com.wisdom.base.common.vo.CalendarVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 考勤记录RecordMapper
 */
@Repository
public interface KqRecordMapper
{
    HolidayVo queryHolidayByDate(Map<String, Object> mapWhere);

    void addGlryKqRecord(GlryKqRecordPo glryKqRecordPo);

    void addlwryKqRecord(LwryKqRecordPo lwryKqRecordPo);

    void addGlryKqRecords(List<GlryKqRecordPo> glryKqRecordPos);

    void addlwryKqRecords(List<LwryKqRecordPo> lwryKqRecordPos);

    void delGlryKqRecordByjlrq(@Param("jlrqs") List<String> jlrqs);

    void delLwryKqRecordByjlrq(@Param("jlrqs") List<String> jlrqs);

    void updateGlryKqRecordByjlrq(@Param("jlrqs") List<String> jlrqs);

    void updateLwryKqRecordByjlrq(@Param("jlrqs") List<String> jlrqs);

    List<Map<String, Object>> selectGlRyReport(Map<String, Object> mapWhere);

    List<Map<String, Object>> selectLwRyReport(Map<String, Object> mapWhere);

    CalendarVo getPmCalendar(String calendarId);

    CalendarVo getDefaultPmCalendar();

    List<CalendarVo> getAllPmCalendar();

    List<Map<String,Object>> getDictonary(String typeCode);

    void addAllKqRecord(List<AllKqRecordPo> insertAllRecordList);

    List<KqDayReportDetailVo> selectGlRyReportDetail(Map<String, Object> mapWhere);
    List<KqDayReportDetailVo> selectLwRyReportDetail(Map<String, Object> mapWhere);
}
