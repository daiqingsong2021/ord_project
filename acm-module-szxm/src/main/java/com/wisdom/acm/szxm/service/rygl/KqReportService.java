package com.wisdom.acm.szxm.service.rygl;

import java.util.List;
import java.util.Map;

public interface KqReportService
{

     List<Map<String,Object>> selectGlRyReport(Map<String, Object> mapWhere);

      List<Map<String,Object>> selectLwRyReport(Map<String, Object> mapWhere);

    /**
     * 获取标段考勤数据
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    Map<String,Object>  getSectionKqRecord(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    /**
     * 获取当日考勤记录 -- 所有标段
     * @param mapWhere
     * @return
     */
    Map<String,Object>  getAttendanceRecords(Map<String, Object> mapWhere);
    /**
     * 获取某个员工的整月考勤记录
     * @param mapWhere
     * @return
     */
    Map<String,Object>  getPeopleMonthKqRecord(Map<String, Object> mapWhere);
}
