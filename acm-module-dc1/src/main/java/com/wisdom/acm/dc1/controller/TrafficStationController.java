package com.wisdom.acm.dc1.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc1.service.TrafficStationDailyService;
import com.wisdom.acm.dc1.vo.TrafficStationDailyVo;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * @date 2020/7/21/021 19:19
 * Description：<描述>
 */
@Controller
@RestController
@RequestMapping("traffic")
public class TrafficStationController {
    @Autowired
    TrafficStationDailyService trafficStationDailyService;

    /**
     * 各车站客流
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/queryStationDailyList/{pageSize}/{currentPageNum}")
    public ApiResult queryStationDailyList(@RequestParam Map<String, Object> mapWhere,
                                          @PathVariable("pageSize")Integer pageSize,
                                          @PathVariable("currentPageNum")Integer currentPageNum)
    {
        PageInfo<TrafficStationDailyVo> queryStationDailyList= trafficStationDailyService.queryStationDailyList
                (mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryStationDailyList);
    }
}
