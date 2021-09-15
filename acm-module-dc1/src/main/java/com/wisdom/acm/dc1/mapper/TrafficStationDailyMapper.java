package com.wisdom.acm.dc1.mapper;

import com.wisdom.acm.dc1.po.TrafficStationDailyPo;
import com.wisdom.acm.dc1.vo.TrafficStationDailyVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * @date 2020/7/20/020 13:22
 * Description：<描述>
 */
public interface TrafficStationDailyMapper extends CommMapper<TrafficStationDailyPo> {
    void delTrafficStationDailyByDates(@Param("recordTimes") List<String> recordTimes);

    List<TrafficStationDailyVo> queryTrafficStationDailys(@Param("yearMonth") String yearMonth);

    List<TrafficStationDailyVo> queryStationDailyList(Map<String, Object> mapWhere);
}
