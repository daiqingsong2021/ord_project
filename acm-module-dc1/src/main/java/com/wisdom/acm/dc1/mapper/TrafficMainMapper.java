package com.wisdom.acm.dc1.mapper;

import com.wisdom.acm.dc1.po.TrafficMainPo;
import com.wisdom.acm.dc1.vo.TrafficMainVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * @date 2020/7/20/020 15:18
 * Description：<描述>
 */
public interface TrafficMainMapper extends CommMapper<TrafficMainPo> {
    List<TrafficMainVo> queryTrafficMainList(Map<String,Object> mapWhere);
    void delTrafficMainByDates(@Param("recordTimes") List<String> recordTimes);
    List<TrafficMainVo> queryTrafficMainByDate(@Param("recordTime")String recordTime);
    List<TrafficMainVo> getFlowTrafficMainList(Map<String,Object> mapWhere);
}
