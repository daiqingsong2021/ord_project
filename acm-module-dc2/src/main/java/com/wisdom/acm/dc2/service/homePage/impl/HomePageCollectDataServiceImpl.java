package com.wisdom.acm.dc2.service.homePage.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.common.DateUtil;
import com.wisdom.acm.dc2.form.TrainScheduleAddForm;
import com.wisdom.acm.dc2.form.TrainScheduleUpdateForm;
import com.wisdom.acm.dc2.mapper.TrainDailyMapper;
import com.wisdom.acm.dc2.mapper.TrainScheduleMapper;
import com.wisdom.acm.dc2.mapper.homePage.HomePageCollectDataMapper;
import com.wisdom.acm.dc2.po.TrainDailyPo;
import com.wisdom.acm.dc2.po.TrainMonthlyPo;
import com.wisdom.acm.dc2.po.TrainSchedulePo;
import com.wisdom.acm.dc2.service.TrainMonthlyService;
import com.wisdom.acm.dc2.service.TrainScheduleService;
import com.wisdom.acm.dc2.service.homePage.HomePageCollectDataService;
import com.wisdom.acm.dc2.vo.TrainScheduleVo;
import com.wisdom.acm.dc2.vo.homePage.HomePageCollectDataVo;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.feign.doc.CommDocService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HomePageCollectDataServiceImpl extends BaseService<HomePageCollectDataMapper, TrainDailyPo> implements HomePageCollectDataService
{



    @Override
    public HomePageCollectDataVo selectCollectDataList(Map<String, Object> mapWhere)
    {
        String recordTime="";
        String recordTimeMonth="";
        String recordTimeYear="";
        //定义获取前几天的数据
        int trafficLine=1;
        Date today =new Date();
        recordTimeMonth=DateUtil.getDateFormat(DateUtil.getDayBefore(today,1),"yyyy-MM");
        recordTimeYear=DateUtil.getDateFormat(DateUtil.getDayBefore(today,1),"yyyy");

        //当格式化定义为#.00，对数字0格式化时结果为：.00，此时应使用0.00格式化#.## 格式：小数点后两位，多余的0不显示
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        HomePageCollectDataVo resultDataVo=new HomePageCollectDataVo();

        mapWhere.put("recordTime",recordTime);
        HomePageCollectDataVo trafficLineDataVo=null;
            while(ObjectUtils.isEmpty(trafficLineDataVo))
            {
                recordTime= DateUtil.getDateFormat(DateUtil.getDayBefore(today,trafficLine),"yyyy-MM-dd");
                mapWhere.put("recordTime",recordTime);
                trafficLineDataVo=mapper.selectTrafficLineByParams(mapWhere);
                trafficLine++;
            }
         if(!ObjectUtils.isEmpty(trafficLineDataVo))
         {
             //年度累计值
             resultDataVo.setTrafficVolumeYear(trafficLineDataVo.getTrafficVolumeYear());
             //月度累计值
             resultDataVo.setTrafficVolumeMonth(trafficLineDataVo.getTrafficVolumeMonth());
             //月度平均值
             resultDataVo.setTrafficVolumeMonthAverage(new BigDecimal(decimalFormat.format(trafficLineDataVo.getTrafficVolumeMonthAverage())));
         }

        recordTime= DateUtil.getDateFormat(DateUtil.getDayBefore(today,1),"yyyy-MM-dd");
        mapWhere.put("recordTime",recordTime);
        mapWhere.put("recordTimeMonth",recordTimeMonth);
        mapWhere.put("recordTimeYear",recordTimeYear);
        HomePageCollectDataVo trainDataVo=mapper.selectTrainByParams(mapWhere);
        if(!ObjectUtils.isEmpty(trainDataVo))
        {
            //年度行车公里数
            resultDataVo.setOperatingKilometres(trainDataVo.getOperatingKilometres());
            //月度兑现率
            resultDataVo.setFulfillmentRate(new BigDecimal(decimalFormat.format(trainDataVo.getFulfillmentRate())));
            //月度准点率
            resultDataVo.setOnTimeRate(new BigDecimal(decimalFormat.format(trainDataVo.getOnTimeRate())));
        }

        HomePageCollectDataVo energyDataVo=mapper.selectEnergyByParams(mapWhere);
        if(!ObjectUtils.isEmpty(energyDataVo))
        {
            //累计耗电量
            resultDataVo.setTotalPowerConsumption(energyDataVo.getTotalPowerConsumption());
            //月度牵引占比
            resultDataVo.setTowPowerRate(new BigDecimal(decimalFormat.format(energyDataVo.getTowPowerRate())));
            //月度动照占比
            resultDataVo.setMotivePowerRate(new BigDecimal(decimalFormat.format(energyDataVo.getMotivePowerRate())));
        }

        HomePageCollectDataVo constructionDataVo=mapper.selectConstructionByParams(mapWhere);
        if(!ObjectUtils.isEmpty(constructionDataVo))
        {
            //年度计划
            resultDataVo.setTotalPlanYear(constructionDataVo.getTotalPlanYear());
            //月度兑现率
            if(!"0".equals(String.valueOf(constructionDataVo.getTotalPlanConstructionQuantity())))
            {
                resultDataVo.setTotalActualRate(constructionDataVo.getTotalActualConstructionQuantity().divide(constructionDataVo.getTotalPlanConstructionQuantity() ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
            }
        }


        return resultDataVo;
    }
}
