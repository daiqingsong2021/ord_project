package com.wisdom.acm.processing.service.report;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.processing.form.FineDailyReportAddForm;
import com.wisdom.acm.processing.po.report.FineDailyReportPo;
import com.wisdom.acm.processing.vo.report.FineDailyReportVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/12/24/024 10:05
 * Description:<描述>
 */
public interface FineDailyReportService extends CommService<FineDailyReportPo> {
    FineDailyReportVo addFineDailyReport1(FineDailyReportAddForm fineDailyReportAddForm);
    FineDailyReportVo addFineDailyReport3(FineDailyReportAddForm fineDailyReportAddForm);
    FineDailyReportVo addFineDailyReport(FineDailyReportAddForm fineDailyReportAddForm);
    PageInfo<FineDailyReportVo> getFineDailyReportList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);
    void deleteFineReport(List<Integer> ids);
    void approveFineDailyReportFlow(String bizType, List<Integer> ids);
    List<FineDailyReportVo> getFlowFineDailyReportList(Map<String, Object> mapWhere);
    List<FineDailyReportVo> getFlowFineDailyReports(Map<String, Object> mapWhere);
}
