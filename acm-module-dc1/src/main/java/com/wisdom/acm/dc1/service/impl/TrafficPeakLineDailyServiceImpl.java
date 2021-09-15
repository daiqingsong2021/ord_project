package com.wisdom.acm.dc1.service.impl;

import com.google.common.collect.Lists;
import com.wisdom.acm.dc1.common.DcCommonUtil;
import com.wisdom.acm.dc1.mapper.TrafficPeakLineDailyMapper;
import com.wisdom.acm.dc1.po.TrafficPeakLineDailyPo;
import com.wisdom.acm.dc1.service.TrafficPeakLineDailyService;
import com.wisdom.acm.dc1.vo.TrafficPeakLineDailyVo;
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
 * 实现类
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
    public void delTrafficPeakLineDailyByDates(List<String> recordTime) {
        mapper.delTrafficPeakLineDailyByDates(recordTime);
    }

    @Override
    public void insertTrafficPeakLineDaily(TrafficPeakLineDailyPo trafficPeakLineDailyPo) {
        this.insert(trafficPeakLineDailyPo);
    }

    @Override
    public List<TrafficPeakLineDailyVo> trafficStationDailyVos(String recordTime) {
        Example example=new Example(TrafficPeakLineDailyPo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("recordTime",recordTime);
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

    @Override
    public void updatePeakLineDailyPos(List<TrafficPeakLineDailyPo> trafficPeakLineDailyPos) {
        for(TrafficPeakLineDailyPo po:trafficPeakLineDailyPos){
            this.updateSelectiveById(po);
        }
    }

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

    @Override
    public List<TrafficPeakLineDailyVo> queryTrafficPeakLineDailyVos(String yearMonth) {
        return mapper.queryTrafficPeakLineDailyVos(yearMonth);
    }
}
