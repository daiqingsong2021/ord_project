package com.wisdom.acm.processing.service.traffic.impl;

import com.google.common.collect.Lists;
import com.wisdom.acm.processing.common.DcCommonUtil;
import com.wisdom.acm.processing.mapper.traffic.TrafficPeakLineDailyMapper;
import com.wisdom.acm.processing.po.traffic.TrafficPeakLineDailyPo;
import com.wisdom.acm.processing.service.traffic.TrafficPeakLineDailyService;
import com.wisdom.acm.processing.vo.traffic.TrafficPeakLineDailyVo;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.DictionaryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/17/017 13:52
 * Description:<描述>
 */
@Slf4j
@Service
public class TrafficPeakLineDailyServiceImpl extends BaseService<TrafficPeakLineDailyMapper, TrafficPeakLineDailyPo>
        implements TrafficPeakLineDailyService {
    @Autowired
    private CommDictService commDictService;
    @Autowired
    private DcCommonUtil dcCommonUtil;
    @Override
    public List<TrafficPeakLineDailyVo> queryPeakLineForReport(String recordTime, String line) {
        Example example=new Example(TrafficPeakLineDailyPo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("recordTime",recordTime);
        criteria.andEqualTo("line",line);
        List<TrafficPeakLineDailyPo>trafficPeakLineDailyPoList=this.selectByExample(example);
        List<TrafficPeakLineDailyVo>trafficPeakLineDailyVoList= Lists.newArrayList();
        Map<String, DictionaryVo> typeDictMap=commDictService.getDictMapByTypeCode("line.network");
        for(TrafficPeakLineDailyPo trafficPeakLineDailyPo:trafficPeakLineDailyPoList){
            TrafficPeakLineDailyVo trafficStationDailyVo=new TrafficPeakLineDailyVo();
            trafficStationDailyVo=dozerMapper.map(trafficPeakLineDailyPo,TrafficPeakLineDailyVo.class);
            trafficStationDailyVo.getLineVo().setName(dcCommonUtil.getDictionaryName(typeDictMap, trafficStationDailyVo.getLine()));;
            trafficPeakLineDailyVoList.add(trafficStationDailyVo);
        }
        return trafficPeakLineDailyVoList;
    }
}
