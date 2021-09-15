package com.wisdom.acm.processing.service.traffic.impl;

import com.google.common.collect.Lists;
import com.wisdom.acm.processing.common.DcCommonUtil;
import com.wisdom.acm.processing.mapper.traffic.TrafficLineDailyMapper;
import com.wisdom.acm.processing.po.traffic.TrafficLineDailyPo;
import com.wisdom.acm.processing.service.traffic.TrafficLineDailyService;
import com.wisdom.acm.processing.vo.traffic.TrafficLineDailyVo;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.DictionaryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zll
 * 2020/8/17/017 13:27
 * Description:<描述>
 */
@Service
@Slf4j
public class TrafficLineDailyServiceImpl extends BaseService<TrafficLineDailyMapper, TrafficLineDailyPo> implements TrafficLineDailyService {
    @Autowired
    private DcCommonUtil dcCommonUtil;

    @Autowired
    private CommDictService commDictService;
    @Override
    public List<TrafficLineDailyVo> trafficLineDailyVoList(String recordTime, String line) {
        List<String> lines= Lists.newArrayList();
        lines.add("0");
        lines.add(line);
        Example example=new Example(TrafficLineDailyPo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("recordTime",recordTime);
        criteria.andIn("line", lines);//标段IN
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
}
