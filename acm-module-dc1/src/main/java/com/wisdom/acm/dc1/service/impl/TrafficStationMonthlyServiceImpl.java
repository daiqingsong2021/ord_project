package com.wisdom.acm.dc1.service.impl;

import com.google.common.collect.Lists;
import com.wisdom.acm.dc1.common.DateUtil;
import com.wisdom.acm.dc1.mapper.TrafficStationMonthlyMapper;
import com.wisdom.acm.dc1.po.TrafficStationMonthlyPo;
import com.wisdom.acm.dc1.service.TrafficStationDailyService;
import com.wisdom.acm.dc1.service.TrafficStationMonthlyService;
import com.wisdom.acm.dc1.vo.TrafficStationDailyVo;
import com.wisdom.acm.dc1.vo.TrafficStationMonthlyVo;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * 实现类
 */
@Slf4j
@Service
public class TrafficStationMonthlyServiceImpl extends BaseService<TrafficStationMonthlyMapper, TrafficStationMonthlyPo>
        implements TrafficStationMonthlyService {
    @Autowired
    TrafficStationDailyService trafficStationDailyService;
    @Override
    public void delTrafficStationMonthlyByDates(List<String> recordTimes) {
        List<TrafficStationMonthlyPo> insertTrafficStationMonthlys = Lists.newArrayList();
        //删除日站点表之后月度表数据相应更新
        //若日表数据已经为空了则表数据直接删除
        for(String recordTime:recordTimes){
            //查询出每个站对应月的汇总，累计，日均量，然后插入odr_traffic_station_monthly表中
            String yearMonth= DateUtil.getDateFormat(DateUtil.getDateFormat(recordTime),"yyyy-MM");
            List<TrafficStationDailyVo> queryTrafficStationDailyVoList=
                    trafficStationDailyService.queryTrafficStationDailys(yearMonth);
            if(ObjectUtils.isEmpty(queryTrafficStationDailyVoList)){
                mapper.delTrafficStationMonthlyByDates(yearMonth);
                return;
            }
            for(TrafficStationDailyVo vo:queryTrafficStationDailyVoList){
                TrafficStationMonthlyPo trafficStationMonthlyPo=new TrafficStationMonthlyPo();
                trafficStationMonthlyPo.setArrivalVolume(vo.getArrivalVolume());
                trafficStationMonthlyPo.setArrivalVolumeAverage(vo.getArrivalVolumeAverage());
                trafficStationMonthlyPo.setLine(vo.getLine());
                trafficStationMonthlyPo.setLinePeriod(vo.getLinePeriod());
                trafficStationMonthlyPo.setOutboundVolume(vo.getOutboundVolume());
                trafficStationMonthlyPo.setOutboundVolumeAverage(vo.getOutboundVolumeAverage());
                trafficStationMonthlyPo.setTotalVolume(vo.getTotalVolume());
                trafficStationMonthlyPo.setStation(vo.getStation());
                trafficStationMonthlyPo.setRecordTime(vo.getRecordTime());
                trafficStationMonthlyPo.setStationNum(vo.getStationNum());
                insertTrafficStationMonthlys.add(trafficStationMonthlyPo);
            }
            //查询本月累计数据
            //本月27条数据更新
            List<TrafficStationMonthlyVo>listVos=this.queryTrafficStationMonthly(yearMonth);
            this.updateTrafficStationMonthly(listVos,insertTrafficStationMonthlys);
        }
    }

    @Override
    public void insertStationMonthly(List<TrafficStationMonthlyPo> list) {
        this.insert(list);
    }

    @Override
    public void updateTrafficStationMonthlyPoList(List<TrafficStationMonthlyPo> trafficStationMonthlyPoList) {
        for(TrafficStationMonthlyPo po :trafficStationMonthlyPoList){
            mapper.updateTrafficStationMonthlyPoList(po.getLine(), DateUtil.getDateFormat(po.getRecordTime())
                    ,po.getStation(),po.getArrivalVolume(),po.getArrivalVolumeAverage(),
                    po.getOutboundVolume(),po.getOutboundVolumeAverage(),po.getTotalVolume());
        }
    }

    @Override
    public List<TrafficStationMonthlyVo> queryTrafficStationMonthly(String yearMonth) {
        return mapper.queryTrafficStationMonthly(yearMonth);
    }

    @Override
    public void updateTrafficStationMonthly(List<TrafficStationMonthlyVo> listVos, List<TrafficStationMonthlyPo> insertTrafficStationMonthlys) {
        //思路先考虑几号线数据，再根据站点一一对应
        List<TrafficStationMonthlyPo>lineAll= Lists.newArrayList();
        List<TrafficStationMonthlyVo>Line1Vo= Lists.newArrayList();
        List<TrafficStationMonthlyVo>Line3Vo= Lists.newArrayList();
        List<TrafficStationMonthlyPo>line1Po= Lists.newArrayList();
        List<TrafficStationMonthlyPo>line3Po= Lists.newArrayList();
        for(TrafficStationMonthlyVo vo:listVos){
            if("1".equals(vo.getLine())){
                Line1Vo.add(vo);
            }
            if("3".equals(vo.getLine())){
                Line3Vo.add(vo);
            }
        }
        for(TrafficStationMonthlyPo po:insertTrafficStationMonthlys){
            if("1".equals(po.getLine())){
                line1Po.add(po);
            }
            if("3".equals(po.getLine())){
                line3Po.add(po);
            }
        }
        //1号线
        Map<String , TrafficStationMonthlyVo> mapList1= ListUtil.listToMap(Line1Vo,"station",String.class);
        for(TrafficStationMonthlyPo po1:line1Po){
            if(po1.getStation().equals(mapList1.get(po1.getStation()).getStation())){
                po1.setId(mapList1.get(po1.getStation()).getId());
            }
        }
        //3号线
        Map<String , TrafficStationMonthlyVo> mapList3= ListUtil.listToMap(Line3Vo,"station",String.class);
        for(TrafficStationMonthlyPo po3:line3Po){
            if(po3.getStation().equals(mapList3.get(po3.getStation()).getStation())){
                po3.setId(mapList3.get(po3.getStation()).getId());
            }
        }
        lineAll.addAll(line1Po);
        lineAll.addAll(line3Po);
        for(TrafficStationMonthlyPo po:lineAll){
            this.updateSelectiveById(po);
        }
    }
}
