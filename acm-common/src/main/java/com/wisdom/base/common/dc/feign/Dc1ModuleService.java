package com.wisdom.base.common.dc.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.dc.vo.traffic.TrafficLineDailyVo;
import com.wisdom.base.common.dc.vo.traffic.TrafficLineTicketDailyVo;
import com.wisdom.base.common.dc.vo.traffic.TrafficPeakLineDailyVo;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * @author zll
 * 2020/8/3/003 13:51
 * Description:<用于dc1微服务需要被调用的接口>
 */
@FeignClient(value = "acm-module-dc1", configuration = FeignConfiguration.class)
public interface Dc1ModuleService {
    /**
     * zll for processing
     * @param recordTime
     * @return
     */
    default List<TrafficLineTicketDailyVo> queryTicketDailyForReport(String recordTime, String line) {
        ApiResult<List<TrafficLineTicketDailyVo>>  apiResult = this.queryTicketDailyForReport_(recordTime,line);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * zll for processing
     * @param recordTime
     * @return
     */
    default List<TrafficLineDailyVo> queryTrafficLineForReport(String recordTime) {
        ApiResult<List<TrafficLineDailyVo>>  apiResult = this.queryTrafficLineForReport_(recordTime);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * zll for processing
     * @param recordTime
     * @param line
     * @return
     */
    default List<TrafficPeakLineDailyVo> queryPeakLineForReport(String recordTime, String line) {
        ApiResult<List<TrafficPeakLineDailyVo>>  apiResult = this.queryPeakLineForReport_(recordTime,line);
        if(apiResult.getStatus() == 200){
            return apiResult.getData();
        }
        return null;
    }

    /**
     * for processing word
     * @param recordTime
     * @return
     */
    @GetMapping(value = "/traffic/queryTrafficLineForReport")
    public ApiResult<List<TrafficLineDailyVo>> queryTrafficLineForReport_(@RequestParam String recordTime);

    /**
     * for processing word
     * @param recordTime
     * @param line
     * @return
     */
    @GetMapping(value = "/traffic/queryPeakLineForReport")
    public ApiResult<List<TrafficPeakLineDailyVo>> queryPeakLineForReport_(@RequestParam String recordTime,@RequestParam String line);

    /**
     * for processing word
     * @param recordTime
     * @param line
     * @return
     */
    @GetMapping(value = "/traffic/queryTicketDailyForReport")
    public ApiResult<List<TrafficLineTicketDailyVo>> queryTicketDailyForReport_(@RequestParam String recordTime,@RequestParam String line);
}
