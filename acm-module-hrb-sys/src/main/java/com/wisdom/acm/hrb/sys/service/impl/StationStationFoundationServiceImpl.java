package com.wisdom.acm.hrb.sys.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.wisdom.acm.hrb.sys.common.DcCommonUtil;
import com.wisdom.acm.hrb.sys.form.StationStationFoundationUpdateForm;
import com.wisdom.acm.hrb.sys.mapper.StationStationFoundationMapper;
import com.wisdom.acm.hrb.sys.po.StationStationFoundationPo;
import com.wisdom.acm.hrb.sys.service.StationFoundationService;
import com.wisdom.acm.hrb.sys.service.StationStationFoundationService;
import com.wisdom.acm.hrb.sys.vo.StationFoundationVo;
import com.wisdom.acm.hrb.sys.vo.StationStationFoundationVo;
import com.wisdom.base.common.feign.CommDictService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zll
 * 2020/10/20/020 11:03
 * Description:<站点区间管理>
 */
@Service
@Slf4j
public class StationStationFoundationServiceImpl extends BaseService<StationStationFoundationMapper, StationStationFoundationPo> implements StationStationFoundationService {
    @Autowired
    private CommDictService commDictService;
    @Autowired
    private DcCommonUtil dcCommonUtil;
    @Autowired
    private StationFoundationService stationFoundationService;
    @Override
    public List<StationStationFoundationVo> queryStationDistanceFoundationList(String line) {
        Example example=new Example(StationStationFoundationPo.class);
        example.createCriteria().andEqualTo("line", line);
        List<StationStationFoundationVo> stationStationFoundationVos = this.selectByExample(example).stream()
                .map(e->dozerMapper.map(e,StationStationFoundationVo.class)).collect(Collectors.toList()).stream().
                        sorted(Comparator.comparing(StationStationFoundationVo::getSum)).collect(Collectors.toList());
        stationStationFoundationVos.forEach(e->{
            if(!ObjectUtils.isEmpty(stationStationFoundationVos)){
                if(commDictService.getDictMapByTypeCode("line").keySet().contains(e.getLine())) {
                    e.setLineName(dcCommonUtil.getDictionaryName(commDictService.getDictMapByTypeCode("line"), e.getLine()));
                }
                if(commDictService.getDictMapByTypeCode("distance.type").keySet().contains(e.getDistanceType().toString())) {
                    e.setDistanceTypeName(dcCommonUtil.getDictionaryName(commDictService.getDictMapByTypeCode("distance.type"), String.valueOf(e.getDistanceType())));
                }
                if(StringUtils.equalsIgnoreCase("0",e.getIsNewLine().toString())){
                    e.setIsNewLineName("否");
                }else if(StringUtils.equalsIgnoreCase("1",e.getIsNewLine().toString())){
                    e.setIsNewLineName("是");
                }else if(StringUtils.equalsIgnoreCase("2",e.getIsNewLine().toString())){
                    e.setIsNewLineName("不区分");
                }
                e.setStationUpName(stationFoundationService.getStationListMap(stationStationFoundationVos.get(0).getLine(),null).get(e.getStationUp()));
                e.setStationDownName(stationFoundationService.getStationListMap(stationStationFoundationVos.get(0).getLine(),null).get(e.getStationDown()));
            }
        });
        return stationStationFoundationVos;
    }

    @Override
    public List<StationStationFoundationVo> updateStationDistanceFoundationList(List<StationStationFoundationUpdateForm> stationStationFoundationUpdateForms) {
        mapper.updateStationDistanceFoundationList(stationStationFoundationUpdateForms.stream().map(e->dozerMapper.map(e,StationStationFoundationPo.class)).collect(Collectors.toList()));
        List<StationStationFoundationVo> stationStationFoundationVos = stationStationFoundationUpdateForms.stream().map(e->dozerMapper.map(e,StationStationFoundationPo.class)).
                collect(Collectors.toList()).stream().map(e->dozerMapper.map(e,StationStationFoundationVo.class)).collect(Collectors.toList()).stream().
                sorted(Comparator.comparing(StationStationFoundationVo::getSum)).collect(Collectors.toList());
        stationStationFoundationVos.forEach(e->{
            if(commDictService.getDictMapByTypeCode("line").keySet().contains(e.getLine())) {
                e.setLineName(dcCommonUtil.getDictionaryName(commDictService.getDictMapByTypeCode("line"), e.getLine()));
            }
            if(commDictService.getDictMapByTypeCode("distance.type").keySet().contains(e.getDistanceType().toString())) {
                e.setDistanceTypeName(dcCommonUtil.getDictionaryName(commDictService.getDictMapByTypeCode("distance.type"), String.valueOf(e.getDistanceType())));
            }
            if(StringUtils.equalsIgnoreCase("0",e.getIsNewLine().toString())){
                e.setIsNewLineName("否");
            }else if(StringUtils.equalsIgnoreCase("1",e.getIsNewLine().toString())){
                e.setIsNewLineName("是");
            }else if(StringUtils.equalsIgnoreCase("2",e.getIsNewLine().toString())){
                e.setIsNewLineName("不区分");
            }
            e.setStationUpName(stationFoundationService.getStationListMap(stationStationFoundationVos.get(0).getLine(),null).get(e.getStationUp()));
            e.setStationDownName(stationFoundationService.getStationListMap(stationStationFoundationVos.get(0).getLine(),null).get(e.getStationDown()));
            e.setStationUpDistance(e.getStationUpDistance().setScale(3, BigDecimal.ROUND_HALF_UP));
            e.setStationDownDistance(e.getStationDownDistance().setScale(3, BigDecimal.ROUND_HALF_UP));
        });
        return stationStationFoundationVos;
    }

    @Override
    public void delStationStationFoundationServiceByLine(List<String> lines) {
        mapper.delStationStationFoundationServiceByLine(lines);
    }


    /**
     *
     * @param mapWhere
     * @return
     * 出段   String period;
     *      *正线始发站  String startStation;
     *      *终点站 String endStation;
     *      * 进段 inStation;
     *      * 上行下行  stationDirection   0：上行  1：下行
     *      'isNewLine   是否为新线里程（0否，1是，2不区分）
     *
     */
    @Override
    public StationStationFoundationVo queryStationToStationMileage(Map<String, Object> mapWhere) {

        //出段
        String period="";
        //正线始发站
        String startStation="";
        //终点站
        String endStation="";
        //入段
        String inStation="";
        //上行下行
        String stationDirection=mapWhere.get("stationDirection").toString();


        //辅助线到站点的距离
        BigDecimal assistToStationLength=new BigDecimal("0");
        //站点到站点的距离
        BigDecimal stationToStationLength=new BigDecimal("0");
        //站点到辅助线的距离
        BigDecimal stationToAssistLength=new BigDecimal("0");
        //总里程
        BigDecimal stationMileage=new BigDecimal("0");
        //新线总里程
        BigDecimal newLineStationMileage=new BigDecimal("0");
        //既有线总里程
        BigDecimal oldLineStationMileage=new BigDecimal("0");

        if(!ObjectUtils.isEmpty(mapWhere.get("period")))
        {
            period=mapWhere.get("period").toString();
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("startStation")))
        {
            startStation=mapWhere.get("startStation").toString();
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("endStation")))
        {
            endStation=mapWhere.get("endStation").toString();
        }
        if(!ObjectUtils.isEmpty(mapWhere.get("inStation")))
        {
            inStation=mapWhere.get("inStation").toString();
        }

        //从出段 到正线始发站的距离
        if(!StringUtils.isEmpty(period))
        {
            Map<String, Object> tempMapWhere=new HashMap<>();
            tempMapWhere.put("line",mapWhere.get("line"));
            tempMapWhere.put("stationUp",period);
            if(!StringUtils.isEmpty(startStation))
            {
                tempMapWhere.put("stationDown",startStation);
            }
            List<StationStationFoundationVo> stationStationFoundationVos = mapper.queryStationStationFoundationByParam(tempMapWhere);
            if(!ObjectUtils.isEmpty(stationStationFoundationVos))
            {
                //上行下行  stationDirection   0：上行  1：下行
                if("0".equals(stationDirection))
                {
                    assistToStationLength=stationStationFoundationVos.get(0).getStationUpDistance();
                }
                else
                {
                    assistToStationLength=stationStationFoundationVos.get(0).getStationDownDistance();
                }
                //isNewLine   是否为新线里程（0否，1是，2不区分）
                if("0".equals(stationStationFoundationVos.get(0).getIsNewLine().toString()))
                {
                    oldLineStationMileage=assistToStationLength;
                }
                else if("1".equals(stationStationFoundationVos.get(0).getIsNewLine().toString()))
                {
                    newLineStationMileage=assistToStationLength;
                }
            }
        }
        //正线站点  站点间的距离
        if(!StringUtils.isEmpty(startStation))
        {
            Map<String, Object> tempMapWhere=new HashMap<>();
            tempMapWhere.put("line",mapWhere.get("line"));
            tempMapWhere.put("distanceType","0");
            StationStationFoundationVo startStationVo=new StationStationFoundationVo();
            StationStationFoundationVo endStationVo=new StationStationFoundationVo();

            //终点站相关信息
            if(!StringUtils.isEmpty(endStation))
            {

                //判断是上行 还是下行    上行下行  stationDirection   0：上行  1：下行
                Map<String, Object> mileMapWhere=new HashMap<>();
                mileMapWhere.put("line",mapWhere.get("line"));
                //正线站点 间距类型（0正线区域站点，1正线区域站点-辅助线/车辆段站点）
                mileMapWhere.put("distanceType","0");

                if("0".equals(stationDirection))
                {

                    tempMapWhere.put("stationUp",startStation);
                    //始发站相关信息
                    //List<StationFoundationVo> startStationFoundationVo=stationFoundationService.queryStationListByParam(tempMapWhere);
                    List<StationStationFoundationVo> stationStationFoundationVo=mapper.queryStationStationFoundationByParam(tempMapWhere);
                    if(!ObjectUtils.isEmpty(stationStationFoundationVo))
                    {
                        startStationVo=stationStationFoundationVo.get(0);
                    }
                    tempMapWhere.remove("stationUp");
                    tempMapWhere.put("stationDown",endStation);
                    //List<StationFoundationVo> endStationFoundationVo=stationFoundationService.queryStationListByParam(tempMapWhere);
                    List<StationStationFoundationVo> endStationFoundationVo=mapper.queryStationStationFoundationByParam(tempMapWhere);
                    if(!ObjectUtils.isEmpty(endStationFoundationVo))
                    {
                        endStationVo=endStationFoundationVo.get(0);
                    }


                    mileMapWhere.put("startOrderNumber",startStationVo.getSum());
                    mileMapWhere.put("endOrderNumber",endStationVo.getSum());
                    StationStationFoundationVo stationToStationMileage=mapper.queryStationToStationMileage(mileMapWhere);
                    if(!ObjectUtils.isEmpty(stationToStationMileage))
                    {
                        stationToStationLength=stationToStationMileage.getStationUpDistance();
                    }
                    //既有线
                    mileMapWhere.put("isNewLine","0");
                    StationStationFoundationVo stationToStationOldLineMileage=mapper.queryStationToStationMileage(mileMapWhere);
                    //isNewLine   是否为新线里程（0否，1是，2不区分）
                    if(!ObjectUtils.isEmpty(stationToStationOldLineMileage))
                    {
                        oldLineStationMileage=oldLineStationMileage.add(stationToStationOldLineMileage.getStationUpDistance());
                    }

                    //新线
                    mileMapWhere.put("isNewLine","1");
                    StationStationFoundationVo stationToStationNewLineMileage=mapper.queryStationToStationMileage(mileMapWhere);
                    if(!ObjectUtils.isEmpty(stationToStationNewLineMileage))
                    {
                        newLineStationMileage=newLineStationMileage.add(stationToStationNewLineMileage.getStationUpDistance());
                    }
                }
                else
                {
                    tempMapWhere.put("stationDown",startStation);
                    //始发站相关信息
                    //List<StationFoundationVo> startStationFoundationVo=stationFoundationService.queryStationListByParam(tempMapWhere);
                    List<StationStationFoundationVo> stationStationFoundationVo=mapper.queryStationStationFoundationByParam(tempMapWhere);
                    if(!ObjectUtils.isEmpty(stationStationFoundationVo))
                    {
                        startStationVo=stationStationFoundationVo.get(0);
                    }
                    tempMapWhere.remove("stationDown");
                    tempMapWhere.put("stationUp",endStation);
                    //List<StationFoundationVo> endStationFoundationVo=stationFoundationService.queryStationListByParam(tempMapWhere);
                    List<StationStationFoundationVo> endStationFoundationVo=mapper.queryStationStationFoundationByParam(tempMapWhere);
                    if(!ObjectUtils.isEmpty(endStationFoundationVo))
                    {
                        endStationVo=endStationFoundationVo.get(0);
                    }

                    //下行时序号是倒叙  在查询时between  and   必须是小的数值在前 ，大的数值在后 所以要调换站点顺序值
                    mileMapWhere.put("startOrderNumber",endStationVo.getSum());
                    mileMapWhere.put("endOrderNumber",startStationVo.getSum());
                    StationStationFoundationVo stationToStationMileage=mapper.queryStationToStationMileage(mileMapWhere);
                    if(!ObjectUtils.isEmpty(stationToStationMileage))
                    {
                        stationToStationLength=stationToStationMileage.getStationDownDistance();
                    }
                    //既有线
                    mileMapWhere.put("isNewLine","0");
                    StationStationFoundationVo stationToStationOldLineMileage=mapper.queryStationToStationMileage(mileMapWhere);
                    //isNewLine   是否为新线里程（0否，1是，2不区分）
                    if(!ObjectUtils.isEmpty(stationToStationOldLineMileage))
                    {
                        oldLineStationMileage = oldLineStationMileage.add(stationToStationOldLineMileage.getStationDownDistance());
                    }
                    //新线
                    mileMapWhere.put("isNewLine","1");
                    StationStationFoundationVo stationToStationNewLineMileage=mapper.queryStationToStationMileage(mileMapWhere);
                    if(!ObjectUtils.isEmpty(stationToStationNewLineMileage))
                    {
                        newLineStationMileage=newLineStationMileage.add(stationToStationNewLineMileage.getStationDownDistance());
                    }
                }
            }

        }

        //正线站点到辅助线的距离
        if(!StringUtils.isEmpty(endStation))
        {
            Map<String, Object> tempMapWhere=new HashMap<>();
            tempMapWhere.put("line",mapWhere.get("line"));
            tempMapWhere.put("distanceType","1");
            //从正线站点到辅助线站点 里程  只能从辅助线到正线站点查询里程
            tempMapWhere.put("stationDown",endStation);
            //存在入线
            if(!StringUtils.isEmpty(inStation))
            {
                tempMapWhere.put("stationUp",inStation);
                List<StationStationFoundationVo> stationStationFoundationVos = mapper.queryStationStationFoundationByParam(tempMapWhere);
                if(ObjectUtils.isEmpty(stationStationFoundationVos))
                {
                    tempMapWhere.remove("stationDown");
                    stationStationFoundationVos = mapper.queryStationStationFoundationByParam(tempMapWhere);
                }
                if(!ObjectUtils.isEmpty(stationStationFoundationVos))
                {
                    //上行下行  stationDirection   0：上行  1：下行
                    if("0".equals(stationDirection))
                    {
                        stationToAssistLength=stationStationFoundationVos.get(0).getStationUpDistance();
                    }
                    else
                    {
                        stationToAssistLength=stationStationFoundationVos.get(0).getStationDownDistance();
                    }

                    //isNewLine   是否为新线里程（0否，1是，2不区分）
                    if("0".equals(stationStationFoundationVos.get(0).getIsNewLine().toString()))
                    {
                        oldLineStationMileage=oldLineStationMileage.add(stationToAssistLength);
                    }
                    else if("1".equals(stationStationFoundationVos.get(0).getIsNewLine().toString()))
                    {
                        newLineStationMileage=newLineStationMileage.add(stationToAssistLength);
                    }
                }

            }
        }

        //计算总里程
        stationMileage=assistToStationLength.add(stationToStationLength).add(stationToAssistLength);

        StationStationFoundationVo StationStationFoundationVo=new StationStationFoundationVo();
        StationStationFoundationVo.setOldLineStationMileage(oldLineStationMileage);
        StationStationFoundationVo.setNewLineStationMileage(newLineStationMileage);
        StationStationFoundationVo.setStationMileage(stationMileage);
        return StationStationFoundationVo;
    }




}
