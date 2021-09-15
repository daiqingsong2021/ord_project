package com.wisdom.acm.dc1.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.dc1.common.DateUtil;
import com.wisdom.acm.dc1.common.DcCommonUtil;
import com.wisdom.acm.dc1.mapper.TrafficLineDailyMapper;
import com.wisdom.acm.dc1.po.TrafficLineDailyPo;
import com.wisdom.acm.dc1.po.TrafficLineMonthlyPo;
import com.wisdom.acm.dc1.po.TrafficMainPo;
import com.wisdom.acm.dc1.service.TrafficLineDailyService;
import com.wisdom.acm.dc1.service.TrafficLineMonthlyService;
import com.wisdom.acm.dc1.service.TrafficMainService;
import com.wisdom.acm.dc1.vo.TrafficLineDailyVo;
import com.wisdom.acm.dc1.vo.TrafficLineMonthlyVo;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.DictionaryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 线路客运量实现类
 */
@Slf4j
@Service
public class  TrafficLineDailyServiceImpl extends BaseService<TrafficLineDailyMapper, TrafficLineDailyPo> implements TrafficLineDailyService {
    @Autowired
    private TrafficMainService trafficMainService;
    @Autowired
    private TrafficLineMonthlyService trafficLineMonthlyService;
    @Autowired
    private CommDictService commDictService;
    @Autowired
    private DcCommonUtil dcCommonUtil;
    @Override
    public PageInfo<TrafficLineDailyVo> queryTrafficLineList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        mapWhere.put("line", StringHelper.formattString(String.valueOf(mapWhere.get("line"))));
        mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        mapWhere.put("endTime", StringHelper.formattString(String.valueOf(mapWhere.get("endTime"))));
        PageHelper.startPage(currentPageNum, pageSize);
        List<TrafficLineDailyVo> trafficLineDailyVoList = mapper.queryTrafficLineList(mapWhere);
        PageInfo<TrafficLineDailyVo> pageInfo = new PageInfo<TrafficLineDailyVo>(trafficLineDailyVoList);
        if (!ObjectUtils.isEmpty(pageInfo.getList()))
        {
            for(TrafficLineDailyVo trafficLineDailyVo : pageInfo.getList()){
                trafficLineDailyVo.setLine(trafficLineDailyVo.getLine()+"号线");
                trafficLineDailyVo.setWeek(DateUtil.getWeek(trafficLineDailyVo.getCreatTime()));
            }
        }
        return pageInfo;
    }

    @Override
    public void insertTrafficLines(List<TrafficLineDailyPo> list) {
        this.insert(list);
    }

    @Override
    public void delTrafficLineByDates(List<String> recordTime) {
        mapper.delTrafficLineByDates(recordTime);
    }

    @Override
    public List<TrafficLineDailyVo> trafficLineDailyVoList(String recordTime) {
        Example example=new Example(TrafficLineDailyPo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("recordTime",recordTime);
        List<TrafficLineDailyPo> trafficLineDailyPoList=this.selectByExample(example);
        //三行数据
        Map<String, DictionaryVo> map=commDictService.getDictMapByTypeCode("line.network");
        List<TrafficLineDailyVo>listAll=trafficLineDailyPoList.stream().
                map(e->dozerMapper.map(e,TrafficLineDailyVo.class)).collect(Collectors.toList());
        for(TrafficLineDailyVo vo :listAll){
            vo.getLineVo().setName(dcCommonUtil.getDictionaryName(map, vo.getLine()));;
        }
        return listAll;
    }

    @Override
    public PageInfo<TrafficLineDailyVo> queryTrafficLineNetWorkList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        mapWhere.put("endTime", StringHelper.formattString(String.valueOf(mapWhere.get("endTime"))));
        PageHelper.startPage(currentPageNum, pageSize);
        List<TrafficLineDailyVo> trafficLineDailyVoList = mapper.queryTrafficLineNetWorkList(mapWhere);
        PageInfo<TrafficLineDailyVo> pageInfo = new PageInfo<TrafficLineDailyVo>(trafficLineDailyVoList);
        if (!ObjectUtils.isEmpty(pageInfo.getList()))
        {
            for(TrafficLineDailyVo trafficLineDailyVo : pageInfo.getList()){
                trafficLineDailyVo.setWeek(DateUtil.getWeek(trafficLineDailyVo.getCreatTime()));
            }
        }
        return pageInfo;
    }

    @Override
    public void updateTrafficLineDailyPos(List<TrafficLineDailyPo> list,Integer id) {
        List<TrafficLineMonthlyPo>trafficLineMonthlyPos=Lists.newArrayList();
        //'本日客运量（人次）'
        BigDecimal trafficVolumeToday=new BigDecimal("0");
        //'月累计客运量（万人次）'
        BigDecimal trafficVolumeMonth=new BigDecimal("0");
        //'本月日均客运量（万人次）'
        BigDecimal trafficVolumeMonthAverage=new BigDecimal("0");
        //'年累计客运量（万人次）'
        BigDecimal trafficVolumeYear=new BigDecimal("0");
        //'年累计日均客运量（万人次）'
        BigDecimal trafficVolumeYearAverage=new BigDecimal("0");
        //'开通累计客运量（万人次）'
        BigDecimal trafficVolumeOpen=new BigDecimal("0");
        //'开通累计日均客运量（万人次）'
        BigDecimal trafficVolumeOpenAverage=new BigDecimal("0");
        for(TrafficLineDailyPo po:list){
            if(!"0".equals(po.getLine())){
                trafficVolumeToday=trafficVolumeToday.add(po.getTrafficVolume());
                trafficVolumeMonth=trafficVolumeMonth.add(po.getTrafficVolumeMonth());
                trafficVolumeMonthAverage=trafficVolumeMonthAverage.add(po.getTrafficVolumeMonthAverage());
                trafficVolumeYear=trafficVolumeYear.add(po.getTrafficVolumeYear());
                trafficVolumeYearAverage=trafficVolumeYearAverage.add(po.getTrafficVolumeYear());
                trafficVolumeOpen=trafficVolumeOpen.add(po.getTrafficVolumeOpen());
                trafficVolumeOpenAverage=trafficVolumeOpenAverage.add(po.getTrafficVolumeOpenAverage());
                //修改月累计的数据
            }
            TrafficLineMonthlyPo p=new TrafficLineMonthlyPo();
            p.setLine(po.getLine());
            p.setRecordTime(po.getRecordTime());
            p.setTrafficVolumeMonth(po.getTrafficVolumeMonth());
            p.setTrafficVolumeMonthAverage(po.getTrafficVolumeMonthAverage());
            trafficLineMonthlyPos.add(p);
            super.updateSelectiveById(po);
        }
        TrafficMainPo trafficMainPo=new TrafficMainPo();
        trafficMainPo.setId(id);
        trafficMainPo.setTrafficVolumeToday(trafficVolumeToday);
        trafficMainPo.setTrafficVolumeMonth(trafficVolumeMonth);
        trafficMainPo.setTrafficVolumeMonthAverage(trafficVolumeMonthAverage);
        trafficMainPo.setTrafficVolumeYear(trafficVolumeYear);
        trafficMainPo.setTrafficVolumeYearAverage(trafficVolumeYearAverage);
        trafficMainPo.setTrafficVolumeOpen(trafficVolumeOpen);
        trafficMainPo.setTrafficVolumeYearAverage(trafficVolumeOpenAverage);
        //月数据修改
        Map<String,Object>map= Maps.newHashMap();
        String yearMonth= DateUtil.getDateFormat(trafficLineMonthlyPos.get(0).getRecordTime(),"yyyy-MM");
        map.put("yearMonth",yearMonth);
        map.put("lines", Arrays.asList("1","3","0"));
        List<TrafficLineDailyVo> vos=this.queryMaxTrafficVolumeList(map);
        Map<String ,TrafficLineDailyVo>mapList= ListUtil.listToMap(vos,"line",String.class);
        for(TrafficLineMonthlyPo po:trafficLineMonthlyPos){
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
        List<TrafficLineMonthlyVo> monthlyVoList=trafficLineMonthlyService.queryTrafficLineMonthlyVo(map);
        trafficLineMonthlyService.updateTrafficLineMonthly(monthlyVoList,trafficLineMonthlyPos);
        //修改主表数据
        trafficMainService.updateTrafficMainPo(trafficMainPo,list.get(0).getRecordTime());
    }

    @Override
    public List<TrafficLineDailyVo> queryMaxTrafficVolumeList(Map<String, Object> mapWhere) {
        return mapper.queryMaxTrafficVolume(mapWhere);
    }
}
