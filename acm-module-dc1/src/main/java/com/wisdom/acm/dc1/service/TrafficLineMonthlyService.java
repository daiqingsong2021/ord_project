package com.wisdom.acm.dc1.service;

import com.wisdom.acm.dc1.po.TrafficLineMonthlyPo;
import com.wisdom.acm.dc1.vo.TrafficLineMonthlyVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * @date 2020/7/21/021 13:47
 * Description：<描述>
 */
public interface TrafficLineMonthlyService extends CommService<TrafficLineMonthlyPo> {
    void insertTrafficLineMonthly(List<TrafficLineMonthlyPo> list);

    //根据日期删除此日期的所有数据
    void delTrafficLineMonthlyByDates(List<String>recordTime);

    void updateTrafficLineMonthlyPos(List<TrafficLineMonthlyPo>trafficLineMonthlyPos);

    List<TrafficLineMonthlyVo> queryTrafficLineMonthlyVo(Map<String, Object> mapWhere);

    void updateTrafficLineMonthly(List<TrafficLineMonthlyVo> monthlyVoList, List<TrafficLineMonthlyPo> insertTrafficLineMonthlyPos);

    void updateTrafficLineMonthlyByMonthly(List<TrafficLineMonthlyPo>trafficLineMonthlyPos);
}
