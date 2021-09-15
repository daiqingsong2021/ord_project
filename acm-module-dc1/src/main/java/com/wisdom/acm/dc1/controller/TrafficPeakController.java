package com.wisdom.acm.dc1.controller;

import com.wisdom.acm.dc1.service.TrafficPeakLineDailyService;
import com.wisdom.acm.dc1.vo.TrafficPeakLineDailyVo;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author zll
 * 2020/8/13/013 16:25
 * Description:<描述>
 */
@Controller
@RestController
@RequestMapping("traffic")
public class TrafficPeakController {
    @Autowired
    TrafficPeakLineDailyService trafficPeakLineDailyService;

    /**
     * zll for word
     * @param recordTime
     * @return
     */
    @GetMapping(value = "/queryPeakLineForReport")
    public ApiResult<List<TrafficPeakLineDailyVo>> queryTrafficLineForReport(@RequestParam String recordTime,@RequestParam String line){
        List<TrafficPeakLineDailyVo> trafficLineDailyVos= trafficPeakLineDailyService.queryPeakLineForReport(recordTime,line);
        return ApiResult.success(trafficLineDailyVos);
    }

}
