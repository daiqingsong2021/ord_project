package com.wisdom.acm.dc2.vo.homePage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.acm.dc2.vo.TrainScheduleVo;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class HomePageCollectDataVo
{


    /**
     * 客运数据
     */

    //本月累计客运量(万人次)
    private BigDecimal trafficVolumeMonth=new BigDecimal("0");

    //本月日均客运量（万人次）
    private BigDecimal trafficVolumeMonthAverage=new BigDecimal("0");

    //年累计客运量(万人次)
    private BigDecimal trafficVolumeYear=new BigDecimal("0");





    /**
     * 行车
     */

    /**
     *运营里程
     */
    private BigDecimal operatingKilometres=new BigDecimal("0");

    /**
     * 兑现率
     */
    private BigDecimal fulfillmentRate=new BigDecimal("0");

    /**
     * 准点率
     */
    private BigDecimal onTimeRate=new BigDecimal("0");

    /**
     * 能耗
     */

    /**
     * 当月总耗电量
     */
    private BigDecimal totalPowerConsumption=new BigDecimal("0");

    /**
     *动照占比
     */
    private BigDecimal motivePowerRate=new BigDecimal("0");

    /**
     *牵引占比
     */
    private BigDecimal towPowerRate=new BigDecimal("0");

    /**
     * 施工
     */

    /**
     * 年度计划总数
     */
    private BigDecimal totalPlanYear=new BigDecimal("0");
    /**
     * 月计划总数
     */
    private BigDecimal totalPlanConstructionQuantity=new BigDecimal("0");

    /**
     *月计划实际完成总数
     */
    private BigDecimal totalActualConstructionQuantity =new BigDecimal("0");
    /**
     *月计划实际完成总数 兑现率
     */
    private BigDecimal totalActualRate=new BigDecimal("0");


}
