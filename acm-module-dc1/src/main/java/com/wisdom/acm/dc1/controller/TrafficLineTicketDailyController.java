package com.wisdom.acm.dc1.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc1.service.TrafficLineTicketDailyService;
import com.wisdom.acm.dc1.vo.TrafficLineTicketDailyVo;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/4/004 14:09
 * Description:<描述>
 */
@Controller
@RestController
@RequestMapping("traffic")
public class TrafficLineTicketDailyController {
    @Autowired
    TrafficLineTicketDailyService trafficLineTicketDailyService;

    /**
     * 票卡客流
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/queryTicketDailyList/{pageSize}/{currentPageNum}")
    public ApiResult queryTicketDailyList(@RequestParam Map<String, Object> mapWhere,
                                           @PathVariable("pageSize")Integer pageSize,
                                           @PathVariable("currentPageNum")Integer currentPageNum)
    {
        PageInfo<TrafficLineTicketDailyVo> queryTicketDailyList= trafficLineTicketDailyService.queryStationDailyList
                (mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryTicketDailyList);
    }
    /**
     * zll for word
     * @param recordTime
     * @param line
     * @return
     */
    @GetMapping(value = "/queryTicketDailyForReport")
    public ApiResult<List<TrafficLineTicketDailyVo>> queryTicketDailyForReport(@RequestParam String recordTime,@RequestParam String line){
        List<TrafficLineTicketDailyVo> trafficLineDailyVos= trafficLineTicketDailyService.queryTicketDailyForReport(recordTime,line);
        return ApiResult.success(trafficLineDailyVos);
    }
}
