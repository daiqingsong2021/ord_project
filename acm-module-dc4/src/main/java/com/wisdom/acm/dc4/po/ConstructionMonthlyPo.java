package com.wisdom.acm.dc4.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.po.BasePo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ConstructionDailyPo class
 * @author  chenhai
 * @data 2020/08/10
 * 施工月况
 */
@Data
@Table(name="odr_construction_monthly")
public class ConstructionMonthlyPo extends BasePo {

    @Column(name = "record_time")
    @ApiModelProperty(value="日期")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;

    @Column(name = "line")
    @ApiModelProperty(value="线路")
    private String line;

    @Column(name = "weekly_plan_construction_quantity")
    @ApiModelProperty(value="周计划施工数")
    private BigDecimal weeklyPlanConstructionQuantity;

    @Column(name = "weekly_actual_construction_quantity")
    @ApiModelProperty(value="周计划实际完成施工数")
    private BigDecimal weeklyActualConstructionQuantity;

    @Column(name = "daily_plan_construction_quantity")
    @ApiModelProperty(value="日补充施工数")
    private BigDecimal dailyPlanConstructionQuantity;

    @Column(name = "daily_actual_construction_quantity")
    @ApiModelProperty(value="日补充实际完成施工数")
    private BigDecimal dailyActualConstructionQuantity;

    @Column(name = "temp_plan_construction_quantity")
    @ApiModelProperty(value="临补充施工数")
    private BigDecimal tempPlanConstructionQuantity;

    @Column(name = "temp_actual_construction_quantity")
    @ApiModelProperty(value="临补充实际完成施工数")
    private BigDecimal tempActualConstructionQuantity;

    @Column(name = "repair_plan_construction_quantity")
    @ApiModelProperty(value="抢修施工施工数")
    private BigDecimal repairPlanConstructionQuantity;

    @Column(name = "repair_actual_construction_quantity")
    @ApiModelProperty(value="抢修施工实际完成施工数")
    private BigDecimal repairActualConstructionQuantity;

    @Column(name = "total_plan_construction_quantity")
    @ApiModelProperty(value="计划总数")
    private BigDecimal totalPlanConstructionQuantity;

    @Column(name = "total_actual_construction_quantity")
    @ApiModelProperty(value="计划实际完成总数")
    private BigDecimal totalActualConstructionQuantity;

}
