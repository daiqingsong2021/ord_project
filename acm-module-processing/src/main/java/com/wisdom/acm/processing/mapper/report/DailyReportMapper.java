package com.wisdom.acm.processing.mapper.report;

import com.wisdom.acm.processing.po.report.DailyReportPo;
import com.wisdom.acm.processing.vo.report.DailyReportVo;
import com.wisdom.acm.processing.vo.report.DocFileVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/18/018 15:44
 * Description:<描述>
 */
public interface DailyReportMapper extends CommMapper<DailyReportPo> {
    List<DailyReportVo> queryDailyReportList(Map<String, Object> mapWhere);
    List<DailyReportVo> getFlowDailyReportList(Map<String, Object> mapWhere);
    List<DocFileVo> getDocFileNames(@Param("today")String today,@Param("tomorrow")String tomorrow);
}
