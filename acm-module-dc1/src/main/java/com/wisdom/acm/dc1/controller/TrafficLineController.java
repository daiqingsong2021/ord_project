package com.wisdom.acm.dc1.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc1.service.TrafficLineDailyService;
import com.wisdom.acm.dc1.vo.TrafficLineDailyVo;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zll
 * @date 2020/7/20/021 19:19
 * Description：<线路客运量控制器>
 */
@Controller
@RestController
@RequestMapping("traffic")
public class TrafficLineController {
    @Autowired
    private TrafficLineDailyService trafficLineDailyService;
    /**
     * 查询线路客运数据
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/queryTrafficLineList/{pageSize}/{currentPageNum}")
    public ApiResult queryTrafficLineList(@RequestParam Map<String, Object> mapWhere,
                                          @PathVariable("pageSize")Integer pageSize,
                                          @PathVariable("currentPageNum")Integer currentPageNum)
    {
        PageInfo<TrafficLineDailyVo> queryTrafficLineList= trafficLineDailyService.queryTrafficLineList
                (mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryTrafficLineList);
    }

    /**
     * 查询线网客运数据
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/queryTrafficLineNetWorkList/{pageSize}/{currentPageNum}")
    public ApiResult queryTrafficLineNetWorkList(@RequestParam Map<String, Object> mapWhere,
                                          @PathVariable("pageSize")Integer pageSize,
                                          @PathVariable("currentPageNum")Integer currentPageNum)
    {
        PageInfo<TrafficLineDailyVo> queryTrafficLineNetWorkList= trafficLineDailyService.queryTrafficLineNetWorkList
                (mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryTrafficLineNetWorkList);
    }

    /**
     * zll for word
     * @param recordTime
     * @return
     */
    @GetMapping(value = "/queryTrafficLineForReport")
    public ApiResult<List<TrafficLineDailyVo>> queryTrafficLineForReport(@RequestParam String recordTime){
        List<TrafficLineDailyVo> trafficLineDailyVos= trafficLineDailyService.trafficLineDailyVoList(recordTime);
        return ApiResult.success(trafficLineDailyVos);
    }
}
