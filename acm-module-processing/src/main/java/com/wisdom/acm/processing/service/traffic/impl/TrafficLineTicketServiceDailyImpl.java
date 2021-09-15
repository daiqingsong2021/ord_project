package com.wisdom.acm.processing.service.traffic.impl;

import com.google.common.collect.Lists;
import com.wisdom.acm.processing.common.DcCommonUtil;
import com.wisdom.acm.processing.mapper.traffic.TrafficLineTicketDailyMapper;
import com.wisdom.acm.processing.po.traffic.TrafficLineTicketDailyPo;
import com.wisdom.acm.processing.service.traffic.TrafficLineTicketDailyService;
import com.wisdom.acm.processing.vo.traffic.TrafficLineTicketDailyVo;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.DictionaryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/17/017 13:42
 * Description:<描述>
 */
@Slf4j
@Service
public class TrafficLineTicketServiceDailyImpl extends BaseService<TrafficLineTicketDailyMapper, TrafficLineTicketDailyPo> implements TrafficLineTicketDailyService {
    @Autowired
    private DcCommonUtil dcCommonUtil;
    @Autowired
    private CommDictService commDictService;
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
