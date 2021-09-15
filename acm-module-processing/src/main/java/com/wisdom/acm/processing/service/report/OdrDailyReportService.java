package com.wisdom.acm.processing.service.report;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.processing.po.report.DailyReportPo;
import com.wisdom.acm.processing.vo.report.DailyReportVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.UserInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/11/011 14:46
 * Description:<描述>
 */
public interface OdrDailyReportService extends CommService<DailyReportPo> {
    DailyReportVo  generateDailyWorkReportFile(Map<String, Object> maps,UserInfo user);

    //List<DailyReportVo>  generateDailyWorkReportFile(Map<String, Object> maps)throws IOException;

    void deleteReport(List<Map<String,Integer>> mapIds);

    PageInfo<DailyReportVo> queryDailyReportList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    List<DailyReportVo> queryFlowDailyReportList(Map<String, Object> mapWhere);

    List<DailyReportVo> getFlowDailyReportList(Map<String, Object> mapWhere);

    DailyReportVo generateDailyWorkReportFile1(Map<String, Object> maps);

    DailyReportVo generateDailyWorkReportFile3(Map<String, Object> maps);
}
