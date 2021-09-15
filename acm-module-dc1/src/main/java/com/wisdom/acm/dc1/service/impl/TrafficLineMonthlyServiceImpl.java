package com.wisdom.acm.dc1.service.impl;

import com.google.common.collect.Maps;
import com.wisdom.acm.dc1.common.DateUtil;
import com.wisdom.acm.dc1.mapper.TrafficLineMonthlyMapper;
import com.wisdom.acm.dc1.po.TrafficLineMonthlyPo;
import com.wisdom.acm.dc1.service.TrafficLineDailyService;
import com.wisdom.acm.dc1.service.TrafficLineMonthlyService;
import com.wisdom.acm.dc1.vo.TrafficLineDailyVo;
import com.wisdom.acm.dc1.vo.TrafficLineMonthlyVo;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zll
 * @date 2020/7/21/021 13:48
 * Description：<描述>
 */
@Slf4j
@Service
public class TrafficLineMonthlyServiceImpl extends BaseService<TrafficLineMonthlyMapper,TrafficLineMonthlyPo> implements TrafficLineMonthlyService
{
    @Autowired
    TrafficLineDailyService trafficLineDailyService;
    @Override
    public void insertTrafficLineMonthly(List<TrafficLineMonthlyPo> list) {
        this.insert(list);
    }

    @Override
    public void delTrafficLineMonthlyByDates(List<String> recordTimes) {
        for(String recordTime:recordTimes){
            Map<String,Object>map= Maps.newHashMap();
            String yearMonth= DateUtil.getDateFormat(DateUtil.getDateFormat(recordTime),"yyyy-MM");
            map.put("yearMonth",yearMonth);
            //查出月度数据
            List<TrafficLineMonthlyPo>insertTrafficLineMonthlyPos=this.queryTrafficLineMonthlyVo(map).stream().
                    map(e->dozerMapper.map(e,TrafficLineMonthlyPo.class)).collect(Collectors.toList());
            List<TrafficLineDailyVo> vos=trafficLineDailyService.queryMaxTrafficVolumeList(map);
            //若日表数据已经是空，则月表直接清除
            if(ObjectUtils.isEmpty(vos)){
                mapper.delTrafficLineMonthlyByDates(yearMonth);
                return;
            }
            Map<String ,TrafficLineDailyVo>mapList= ListUtil.listToMap(vos,"line",String.class);
            for(TrafficLineMonthlyPo po:insertTrafficLineMonthlyPos){
                if(po.getLine().equals("1")){
                    po.setLine("1");
                    po.setMaxTrafficVolume(mapList.get("1").getMaxVolume());
                    po.setMaxTrafficDate(mapList.get("1").getRecordTime());
                }
                if(po.getLine().equals("3")){
                    po.setLine("3");
                    po.setMaxTrafficVolume(mapList.get("3").getMaxVolume());
                    po.setMaxTrafficDate(mapList.get("3").getRecordTime());
                }
                if(po.getLine().equals("0")){
                    po.setLine("0");
                    po.setMaxTrafficVolume(mapList.get("0").getMaxVolume());
                    po.setMaxTrafficDate(mapList.get("0").getRecordTime());
                }
            }
            //获取线网客运中月数据
            List<TrafficLineMonthlyVo> monthlyVoList=this.queryTrafficLineMonthlyVo(map);
            this.updateTrafficLineMonthly(monthlyVoList,insertTrafficLineMonthlyPos);
        }
    }

    @Override
    public void updateTrafficLineMonthlyPos(List<TrafficLineMonthlyPo> trafficLineMonthlyPos) {
        for(TrafficLineMonthlyPo po:trafficLineMonthlyPos){
            mapper.updateTrafficLineMonthlyPo(po.getLine(), DateUtil.getDateFormat(po.getRecordTime()),
                                              po.getTrafficVolumeMonth(),po.getTrafficVolumeMonthAverage());
        }
    }

    @Override
    public List<TrafficLineMonthlyVo> queryTrafficLineMonthlyVo(Map<String, Object> mapWhere) {
        return  mapper.queryTrafficLineMonthlyVo(mapWhere);
    }

    @Override
    public void updateTrafficLineMonthly(List<TrafficLineMonthlyVo> monthlyVoList, List<TrafficLineMonthlyPo> insertTrafficLineMonthlyPos) {
        Map<String , TrafficLineMonthlyVo>mapList= ListUtil.listToMap(monthlyVoList,"line",String.class);
        //把今日的月数据更新
        for(TrafficLineMonthlyPo po:insertTrafficLineMonthlyPos){//vo是查出来的，po是最新的
            if(po.getLine().equals(mapList.get("1").getLine())){//1号线
                po.setId(mapList.get("1").getId());
            }
            if(po.getLine().equals(mapList.get("3").getLine())){//3号线
                po.setId(mapList.get("3").getId());
            }
            if(po.getLine().equals(mapList.get("0").getLine())){//线网
                po.setId(mapList.get("0").getId());
            }
        }
        this.updateTrafficLineMonthlyByMonthly(insertTrafficLineMonthlyPos);
    }

    @Override
    public void updateTrafficLineMonthlyByMonthly(List<TrafficLineMonthlyPo> trafficLineMonthlyPos) {
        for(TrafficLineMonthlyPo po:trafficLineMonthlyPos){
            String yearMonth= DateUtil.getDateFormat(po.getRecordTime(),"yyyy-MM");
            mapper.updateTrafficLineMonthlyByMonthly(po.getLine(), yearMonth,
                    po.getTrafficVolumeMonth(),po.getTrafficVolumeMonthAverage(),po.getMaxTrafficDate()
                    ,po.getMaxTrafficVolume());
        }
    }
}
