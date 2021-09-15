package com.wisdom.acm.dc1.service.impl;

import com.google.common.collect.Lists;
import com.wisdom.acm.dc1.common.DateUtil;
import com.wisdom.acm.dc1.mapper.TrafficLineTicketMonthlyMapper;
import com.wisdom.acm.dc1.po.TrafficLineTicketMonthlyPo;
import com.wisdom.acm.dc1.service.TrafficLineTicketDailyService;
import com.wisdom.acm.dc1.service.TrafficLineTicketMonthlyService;
import com.wisdom.acm.dc1.vo.TrafficLineTicketDailyVo;
import com.wisdom.acm.dc1.vo.TrafficLineTicketMonthlyVo;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * @date 2020/7/21/021 13:48
 * Description：<描述>
 */
@Slf4j
@Service
public class TrafficLineTicketServiceMonthlyImpl extends BaseService<TrafficLineTicketMonthlyMapper,
        TrafficLineTicketMonthlyPo> implements TrafficLineTicketMonthlyService
{
    @Autowired
    TrafficLineTicketDailyService trafficLineTicketDailyService;
    @Override
    public void delTrafficLineTicketMonthlyByDates(List<String> recordTimes) {
        for(String recordTime:recordTimes){
            //月累计统计
            String yearMonth= DateUtil.getDateFormat(DateUtil.getDateFormat(recordTime),"yyyy-MM");
            List<TrafficLineTicketDailyVo> trafficLineTicketDailyVoList=
                    trafficLineTicketDailyService.queryTrafficLineTicketDailys(yearMonth,null);
            if(ObjectUtils.isEmpty(trafficLineTicketDailyVoList)){
                mapper.delTrafficLineTicketMonthlyByDates(yearMonth);
                return;
            }
            List<TrafficLineTicketMonthlyPo> trafficLineTicketMonthlyPos=Lists.newArrayList();
            for(TrafficLineTicketDailyVo vo :trafficLineTicketDailyVoList){
                TrafficLineTicketMonthlyPo trafficLineTicketMonthlyPo=new TrafficLineTicketMonthlyPo();
                trafficLineTicketMonthlyPo.setRecordTime(DateUtil.getDateFormat(recordTime));
                trafficLineTicketMonthlyPo.setLine(vo.getLine());
                trafficLineTicketMonthlyPo.setLinePeriod(vo.getLinePeriod());
                trafficLineTicketMonthlyPo.setRate(vo.getRate());
                trafficLineTicketMonthlyPo.setTicketType(vo.getTicketType());
                trafficLineTicketMonthlyPo.setTotalTrafficVolume(vo.getTotalTrafficVolume());
                trafficLineTicketMonthlyPo.setTrafficVolume(vo.getTrafficVolume());
                trafficLineTicketMonthlyPos.add(trafficLineTicketMonthlyPo);
            }
            //重做逻辑
            List<TrafficLineTicketMonthlyVo>listVos=this.queryTrafficLineTicketMonthlyVo(yearMonth);
            if(!ObjectUtils.isEmpty(listVos))
            {
                this.updateLineTicketMonthly(listVos,trafficLineTicketMonthlyPos);
            }
        }
    }

    @Override
    public void insertTrafficLineTicketMonthlys(List<TrafficLineTicketMonthlyPo> trafficLineTicketMonthlyPos) {
        this.insert(trafficLineTicketMonthlyPos);
    }

    @Override
    public void updateLineTicketMonthlyPoList(List<TrafficLineTicketMonthlyPo> trafficLineTicketMonthlyPos) {
        for(TrafficLineTicketMonthlyPo po : trafficLineTicketMonthlyPos ){
            mapper.updateLineTicketMonthlyPo(po.getLine(), DateUtil.getDateFormat(po.getRecordTime()),po.getTicketType(),
                    po.getRate(),po.getTrafficVolume(),po.getTotalTrafficVolume());
        }
    }

    @Override
    public List<TrafficLineTicketMonthlyVo> queryTrafficLineTicketMonthlyVo(String yearMonth) {
        return mapper.queryTrafficLineTicketMonthlyVo(yearMonth);
    }

    @Override
    public void updateLineTicketMonthly(List<TrafficLineTicketMonthlyVo> listVos, List<TrafficLineTicketMonthlyPo> trafficLineTicketMonthlyPos) {
        //每月共计18条数据,先按照线路分再按照票卡类型
        List<TrafficLineTicketMonthlyPo>lineAll= Lists.newArrayList();
        List<TrafficLineTicketMonthlyVo>Line1Vo= Lists.newArrayList();
        List<TrafficLineTicketMonthlyVo>Line3Vo= Lists.newArrayList();
        List<TrafficLineTicketMonthlyVo>Line0Vo= Lists.newArrayList();
        List<TrafficLineTicketMonthlyPo>line1Po= Lists.newArrayList();
        List<TrafficLineTicketMonthlyPo>line3Po= Lists.newArrayList();
        List<TrafficLineTicketMonthlyPo>line0Po= Lists.newArrayList();
        for(TrafficLineTicketMonthlyVo vo:listVos){
            if("1".equals(vo.getLine())){
                Line1Vo.add(vo);
            }
            if("3".equals(vo.getLine())){
                Line3Vo.add(vo);
            }
            if("0".equals(vo.getLine())){
                Line0Vo.add(vo);
            }
        }
        for(TrafficLineTicketMonthlyPo po:trafficLineTicketMonthlyPos){
            if("1".equals(po.getLine())){
                line1Po.add(po);
            }
            if("3".equals(po.getLine())){
                line3Po.add(po);
            }
            if("0".equals(po.getLine())){
                line0Po.add(po);
            }
        }
        //1号线
        Map<String , TrafficLineTicketMonthlyVo> mapList1= ListUtil.listToMap(Line1Vo,"ticketType",String.class);
        for(TrafficLineTicketMonthlyPo po1:line1Po){
            if(po1.getTicketType().equals(mapList1.get(po1.getTicketType()).getTicketType())){
                po1.setId(mapList1.get(po1.getTicketType()).getId());
            }
        }
        //3号线
        Map<String , TrafficLineTicketMonthlyVo> mapList3= ListUtil.listToMap(Line3Vo,"ticketType",String.class);
        for(TrafficLineTicketMonthlyPo po3:line3Po){
            if(po3.getTicketType().equals(mapList3.get(po3.getTicketType()).getTicketType())){
                po3.setId(mapList3.get(po3.getTicketType()).getId());
            }
        }
        //线网
        Map<String , TrafficLineTicketMonthlyVo> mapList0= ListUtil.listToMap(Line0Vo,"ticketType",String.class);
        for(TrafficLineTicketMonthlyPo po0:line0Po){
            if(po0.getTicketType().equals(mapList0.get(po0.getTicketType()).getTicketType())){
                po0.setId(mapList0.get(po0.getTicketType()).getId());
            }
        }
        lineAll.addAll(line1Po);
        lineAll.addAll(line3Po);
        lineAll.addAll(line0Po);
        for(TrafficLineTicketMonthlyPo po:lineAll){
            this.updateSelectiveById(po);
        }
    }
}
