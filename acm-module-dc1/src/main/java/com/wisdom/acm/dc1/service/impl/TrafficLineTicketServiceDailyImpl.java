package com.wisdom.acm.dc1.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisdom.acm.dc1.common.DateUtil;
import com.wisdom.acm.dc1.common.DcCommonUtil;
import com.wisdom.acm.dc1.mapper.TrafficLineTicketDailyMapper;
import com.wisdom.acm.dc1.po.TrafficLineTicketDailyPo;
import com.wisdom.acm.dc1.po.TrafficLineTicketMonthlyPo;
import com.wisdom.acm.dc1.service.TrafficLineTicketDailyService;
import com.wisdom.acm.dc1.service.TrafficLineTicketMonthlyService;
import com.wisdom.acm.dc1.vo.TrafficLineTicketDailyVo;
import com.wisdom.acm.dc1.vo.TrafficLineTicketMonthlyVo;
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
 * @author zll
 * @date 2020/7/21/021 13:48
 * Description：<描述>
 */
@Slf4j
@Service
public class TrafficLineTicketServiceDailyImpl extends BaseService<TrafficLineTicketDailyMapper,TrafficLineTicketDailyPo> implements TrafficLineTicketDailyService
{
    @Autowired
    private CommDictService commDictService;
    @Autowired
    private TrafficLineTicketMonthlyService trafficLineTicketMonthlyService;
    @Autowired
    private DcCommonUtil dcCommonUtil;
    @Override
    public void delTrafficLineTicketDailyByDates(List<String> recordTime) {
        mapper.delTrafficLineTicketDailyByDates(recordTime);
    }

    @Override
    public void insertTrafficLineTicketDaily(List<TrafficLineTicketDailyPo> trafficLineTicketDailyPos) {
        this.insert(trafficLineTicketDailyPos);
    }

    @Override
    public List<TrafficLineTicketDailyVo> queryTrafficLineTicketDailys(String yearMonth,String recordTime) {
        return mapper.queryTrafficLineTicketDailys(yearMonth,recordTime);
    }

    @Override
    public List<TrafficLineTicketDailyVo> trafficLineTicketDailyVoList(String recordTime) {
        Example example=new Example(TrafficLineTicketDailyPo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("recordTime",recordTime);
        List<TrafficLineTicketDailyPo>trafficLineTicketDailyPoList=this.selectByExample(example);
        List<TrafficLineTicketDailyVo>trafficLineTicketDailyVoList= Lists.newArrayList();
        Map<String, DictionaryVo> typeDictMap=commDictService.getDictMapByTypeCode("traffic.ticket.type");
        Map<String, DictionaryVo> map=commDictService.getDictMapByTypeCode("line.network");
        for(TrafficLineTicketDailyPo trafficLineTicketDailyPo:trafficLineTicketDailyPoList){
            TrafficLineTicketDailyVo trafficLineTicketDailyVo=new TrafficLineTicketDailyVo();
            trafficLineTicketDailyVo=dozerMapper.map(trafficLineTicketDailyPo,TrafficLineTicketDailyVo.class);
            String type=trafficLineTicketDailyPo.getTicketType().setScale(0, BigDecimal.ROUND_HALF_UP).toString();//小数点后0位
            trafficLineTicketDailyVo.getTicketTypeVo().setName(dcCommonUtil.getDictionaryName(typeDictMap, type));
            trafficLineTicketDailyVo.getTicketTypeVo().setCode(type);
            trafficLineTicketDailyVo.getLineVo().setName(dcCommonUtil.getDictionaryName(map, trafficLineTicketDailyVo.getLine()));;
            trafficLineTicketDailyVo.setTicketType(trafficLineTicketDailyPo.getTicketType().setScale(0, BigDecimal.ROUND_HALF_UP));
            trafficLineTicketDailyVoList.add(trafficLineTicketDailyVo);
        }
        return trafficLineTicketDailyVoList;
    }

    @Override
    public PageInfo<TrafficLineTicketDailyVo> queryStationDailyList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        mapWhere.put("endTime", StringHelper.formattString(String.valueOf(mapWhere.get("endTime"))));
        mapWhere.put("period", StringHelper.formattString(String.valueOf(mapWhere.get("period"))));//周期
        mapWhere.put("line", StringHelper.formattString(String.valueOf(mapWhere.get("line"))));
        mapWhere.put("station", StringHelper.formattString(String.valueOf(mapWhere.get("station"))));
        PageHelper.startPage(currentPageNum, pageSize);
        List<TrafficLineTicketDailyVo> trafficLineTicketDailyVos=mapper.queryLineTicketDailyList(mapWhere);
        PageInfo<TrafficLineTicketDailyVo> pageInfo = new PageInfo<TrafficLineTicketDailyVo>(trafficLineTicketDailyVos);
        if (!ObjectUtils.isEmpty(pageInfo.getList()))
        {
            Map<String, DictionaryVo> typeDictMap=commDictService.getDictMapByTypeCode("traffic.ticket.type");
            for(TrafficLineTicketDailyVo vo:pageInfo.getList()){
                vo.getTicketTypeVo().setName(dcCommonUtil.getDictionaryName(typeDictMap, vo.getTicketTypeVo().getCode()));;
                vo.setWeek(DateUtil.getWeek(vo.getCreatTime()));
                vo.setBl(String.valueOf(vo.getRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())+"%");//取小数点后两位
            }
        }
        return pageInfo;
    }

    @Override
    public void updateLineTicketDailyPos(List<TrafficLineTicketDailyPo> trafficLineTicketDailyPos,String date) {
        for(TrafficLineTicketDailyPo po:trafficLineTicketDailyPos){
            super.updateSelectiveById(po);
        }
        //月累计统计
        String yearMonth= DateUtil.getDateFormat(DateUtil.getDateFormat(date),"yyyy-MM");
        List<TrafficLineTicketDailyVo> trafficLineTicketDailyVoList=
                this.queryTrafficLineTicketDailys(yearMonth,null);
        List<TrafficLineTicketMonthlyPo> trafficLineTicketMonthlyPos=Lists.newArrayList();
        for(TrafficLineTicketDailyVo vo :trafficLineTicketDailyVoList){
            TrafficLineTicketMonthlyPo trafficLineTicketMonthlyPo=new TrafficLineTicketMonthlyPo();
            trafficLineTicketMonthlyPo.setRecordTime(DateUtil.getDateFormat(date));
            trafficLineTicketMonthlyPo.setLine(vo.getLine());
            trafficLineTicketMonthlyPo.setLinePeriod(vo.getLinePeriod());
            trafficLineTicketMonthlyPo.setRate(vo.getRate());
            trafficLineTicketMonthlyPo.setTicketType(vo.getTicketType());
            trafficLineTicketMonthlyPo.setTotalTrafficVolume(vo.getTotalTrafficVolume());
            trafficLineTicketMonthlyPo.setTrafficVolume(vo.getTrafficVolume());
            trafficLineTicketMonthlyPos.add(trafficLineTicketMonthlyPo);
        }
        //重做逻辑
        List<TrafficLineTicketMonthlyVo>listVos=trafficLineTicketMonthlyService.queryTrafficLineTicketMonthlyVo(yearMonth);
        trafficLineTicketMonthlyService.updateLineTicketMonthly(listVos,trafficLineTicketMonthlyPos);
    }

    @Override
    public List<TrafficLineTicketDailyVo> queryTicketDailyForReport(String recordTime, String line) {
        Example example=new Example(TrafficLineTicketDailyPo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("recordTime",recordTime);
        criteria.andEqualTo("line",line);
        List<TrafficLineTicketDailyPo>trafficLineTicketDailyPoList=this.selectByExample(example);
        List<TrafficLineTicketDailyVo>trafficLineTicketDailyVoList= Lists.newArrayList();
        Map<String, DictionaryVo> typeDictMap=commDictService.getDictMapByTypeCode("traffic.ticket.type");
        Map<String, DictionaryVo> map=commDictService.getDictMapByTypeCode("line.network");
        for(TrafficLineTicketDailyPo trafficLineTicketDailyPo:trafficLineTicketDailyPoList){
            TrafficLineTicketDailyVo trafficLineTicketDailyVo=new TrafficLineTicketDailyVo();
            trafficLineTicketDailyVo=dozerMapper.map(trafficLineTicketDailyPo,TrafficLineTicketDailyVo.class);
            String type=trafficLineTicketDailyPo.getTicketType().setScale(0, BigDecimal.ROUND_HALF_UP).toString();//小数点后0位
            trafficLineTicketDailyVo.getTicketTypeVo().setName(dcCommonUtil.getDictionaryName(typeDictMap, type));
            trafficLineTicketDailyVo.getTicketTypeVo().setCode(type);
            trafficLineTicketDailyVo.getLineVo().setName(dcCommonUtil.getDictionaryName(map, trafficLineTicketDailyVo.getLine()));;
            trafficLineTicketDailyVo.setTicketType(trafficLineTicketDailyPo.getTicketType().setScale(0, BigDecimal.ROUND_HALF_UP));
            trafficLineTicketDailyVoList.add(trafficLineTicketDailyVo);
        }
        return trafficLineTicketDailyVoList;
    }
}
