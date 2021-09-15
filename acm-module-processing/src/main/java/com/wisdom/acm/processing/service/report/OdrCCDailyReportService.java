package com.wisdom.acm.processing.service.report;

import com.wisdom.acm.processing.po.report.DailyReportPo;
import com.wisdom.acm.processing.vo.report.DailyReportVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.UserInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/15/014 16:49
 * Description:<描述>
 */
public interface OdrCCDailyReportService extends CommService<DailyReportPo> {
    DailyReportVo generateCCDailyWorkReportFile(Map<String, Object> mapWhere, UserInfo user);
    //List<DailyReportVo> generateCCDailyWorkReportFile(Map<String, Object> mapWhere) throws IOException;
    void approveDailyReportFlow(String bizType, List<Integer> ids,UserInfo user,Map<String,Object> maps);

    DailyReportVo generateCCDailyWorkReportFile1(Map<String, Object> maps);

    DailyReportVo generateCCDailyWorkReportFile3(Map<String, Object> maps);
}
