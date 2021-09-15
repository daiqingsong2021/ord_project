package com.wisdom.acm.dc1.mapper;

import com.wisdom.acm.dc1.po.TrafficLineDailyPo;
import com.wisdom.acm.dc1.vo.TrafficLineDailyVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * @date 2020/7/20/020 13:22
 * Description：<描述>
 */
public interface TrafficLineDailyMapper extends CommMapper<TrafficLineDailyPo> {
    List<TrafficLineDailyVo> queryTrafficLineList(Map<String,Object> mapWhere);

    void delTrafficLineByDates(@Param("recordTimes") List<String> recordTimes);

    List<TrafficLineDailyVo> queryTrafficLineNetWorkList(Map<String, Object> mapWhere);

    List<TrafficLineDailyVo> queryMaxTrafficVolume(Map<String, Object> mapWhere);
}
