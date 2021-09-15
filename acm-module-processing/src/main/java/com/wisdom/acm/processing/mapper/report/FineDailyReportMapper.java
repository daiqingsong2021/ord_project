package com.wisdom.acm.processing.mapper.report;

import com.wisdom.acm.processing.po.report.FineDailyReportPo;
import com.wisdom.acm.processing.vo.report.FineDailyReportVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/18/018 15:44
 * Description:<描述>
 */
public interface FineDailyReportMapper extends CommMapper<FineDailyReportPo> {
    List<FineDailyReportVo> getFineDailyReportList(Map<String, Object> mapWhere);
    List<FineDailyReportVo> getFlowFineDailyReports(Map<String, Object> mapWhere);
}
