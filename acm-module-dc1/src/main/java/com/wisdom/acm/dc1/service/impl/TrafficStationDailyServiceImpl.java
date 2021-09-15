package com.wisdom.acm.dc1.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisdom.acm.dc1.common.DateUtil;
import com.wisdom.acm.dc1.common.DcCommonUtil;
import com.wisdom.acm.dc1.mapper.TrafficStationDailyMapper;
import com.wisdom.acm.dc1.po.TrafficStationDailyPo;
import com.wisdom.acm.dc1.po.TrafficStationMonthlyPo;
import com.wisdom.acm.dc1.service.TrafficStationDailyService;
import com.wisdom.acm.dc1.service.TrafficStationMonthlyService;
import com.wisdom.acm.dc1.vo.TrafficStationDailyVo;
import com.wisdom.acm.dc1.vo.TrafficStationMonthlyVo;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.DictionaryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 实现类
 */
@Slf4j
@Service
public class TrafficStationDailyServiceImpl extends BaseService<TrafficStationDailyMapper, TrafficStationDailyPo> implements TrafficStationDailyService {
    @Autowired
    private TrafficStationMonthlyService trafficStationMonthlyService;
    @Autowired
    private CommDictService commDictService;
    @Autowired
    private DcCommonUtil dcCommonUtil;
    @Override
    public void delTrafficStationDailyByDates(List<String> recordTime) {
        mapper.delTrafficStationDailyByDates(recordTime);
    }

    @Override
    public void insertStationDaily(List<TrafficStationDailyPo> list) {
        this.insert(list);
    }

    @Override
    public List<TrafficStationDailyVo> queryTrafficStationDailys(String date) {
        return mapper.queryTrafficStationDailys(date);
    }

    @Override
    public List<TrafficStationDailyVo> trafficStationDailyVos(String recordTime) {
        Example example=new Example(TrafficStationDailyPo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("recordTime",recordTime);
        List<TrafficStationDailyPo>trafficStationDailyPoList=this.selectByExample(example);
        List<TrafficStationDailyVo>trafficStationDailyVoList= Lists.newArrayList();
        Map<String, DictionaryVo> typeDictMap=commDictService.getDictMapByTypeCode("line.network");
        for(TrafficStationDailyPo trafficStationDailyPo:trafficStationDailyPoList){
            TrafficStationDailyVo trafficStationDailyVo=new TrafficStationDailyVo();
            trafficStationDailyVo=dozerMapper.map(trafficStationDailyPo,TrafficStationDailyVo.class);
            trafficStationDailyVo.getLineVo().setName(dcCommonUtil.getDictionaryName(typeDictMap, trafficStationDailyVo.getLine()));;
            trafficStationDailyVoList.add(trafficStationDailyVo);
        }
        return trafficStationDailyVoList;
    }

    @Override
    public PageInfo<TrafficStationDailyVo> queryStationDailyList(Map<String, Object> mapWhere, Integer pageSize,Integer currentPageNum) {
        mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        mapWhere.put("endTime", StringHelper.formattString(String.valueOf(mapWhere.get("endTime"))));
        mapWhere.put("period", StringHelper.formattString(String.valueOf(mapWhere.get("period"))));//周期
        mapWhere.put("line", StringHelper.formattString(String.valueOf(mapWhere.get("line"))));
        mapWhere.put("station", StringHelper.formattString(String.valueOf(mapWhere.get("station"))));
        PageHelper.startPage(currentPageNum, pageSize);
        List<TrafficStationDailyVo> trafficStationDailyVos=mapper.queryStationDailyList(mapWhere);
        PageInfo<TrafficStationDailyVo> pageInfo = new PageInfo<TrafficStationDailyVo>(trafficStationDailyVos);
        if (!ObjectUtils.isEmpty(pageInfo.getList()))
        {

            for(TrafficStationDailyVo vo:pageInfo.getList()){
                vo.setWeek(DateUtil.getWeek(vo.getCreatTime()));
                vo.setBl(String.valueOf(vo.getRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())+"%");//取小数点后两位
            }
        }
        return pageInfo;
    }

    @Override
    public void updateStationDailyPos(List<TrafficStationDailyPo> trafficStationDailyPos,String date) {
        for(TrafficStationDailyPo po :trafficStationDailyPos){
            super.updateSelectiveById(po);
        }
        //查询出每个站对应月的汇总，累计，日均量，然后插入odr_traffic_station_monthly表中
        List<TrafficStationMonthlyPo> insertTrafficStationMonthlys = Lists.newArrayList();
        String yearMonth= DateUtil.getDateFormat(DateUtil.getDateFormat(date),"yyyy-MM");
        List<TrafficStationDailyVo> queryTrafficStationDailyVoList=
                this.queryTrafficStationDailys(yearMonth);
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
        //本月27条数据
        List<TrafficStationMonthlyVo>listVos=trafficStationMonthlyService.queryTrafficStationMonthly(yearMonth);
        trafficStationMonthlyService.updateTrafficStationMonthly(listVos,insertTrafficStationMonthlys);
    }
}
