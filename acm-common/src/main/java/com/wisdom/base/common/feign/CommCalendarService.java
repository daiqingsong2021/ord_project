package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.CalendarVo;
import com.wisdom.base.common.vo.OrgVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@FeignClient(value = "acm-module-base",configuration = FeignConfiguration.class)
public interface CommCalendarService {

    /**
     * 获取日历基本信息Map集合
     *
     * @param calendarIds
     * @return
     */
    default Map<Integer, CalendarVo> getCalendarVoMap(List<Integer> calendarIds){
        ApiResult<List<CalendarVo>> apiResult = this.getCalendarInfos(calendarIds);
        if(apiResult.getStatus() == 200){
            List<CalendarVo> calendarVos = apiResult.getData();
            return ListUtil.listToMap(calendarVos,"id",Integer.class);
        }
        return null;
    }

    /**
     * 获取日历
     * @return
     */
    @GetMapping(value = "/calendar/calc/pos/default/info")
    ApiResult<CalendarVo> getCalendarDefaultInfo();

    /**
     * 获取日历
     * @param id
     * @return
     */
    @GetMapping(value = "/calendar/calc/pos/{id}/info")
    ApiResult<CalendarVo> getCalendarInfo(@PathVariable("id") Integer id);

    /**
     * 获取日历
     * @param ids
     * @return
     */
    @PostMapping(value = "/calendar/calc/pos/info")
    ApiResult<List<CalendarVo>> getCalendarInfos(@RequestBody List<Integer> ids);
}
