package com.wisdom.acm.dc4.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ConstructionDailyPo class
 * @author  chenhai
 * @data 2020/08/10
 * 施工日况
 */
@Data
public class ConstructionDailyRcVo {
    private String rcId;
    @ApiModelProperty(value="日期")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;

    @ApiModelProperty(value="线路")
    private String line;

    @ApiModelProperty(value="周计划施工数")
    private BigDecimal weeklyPlanConstructionQuantity ;

    @ApiModelProperty(value="周计划实际完成施工数")
    private BigDecimal weeklyActualConstructionQuantity;


    @ApiModelProperty(value="日补充施工数")
    private BigDecimal dailyPlanConstructionQuantity;

    @ApiModelProperty(value="日补充实际完成施工数")
    private BigDecimal dailyActualConstructionQuantity;

    @ApiModelProperty(value="临补充施工数")
    private BigDecimal tempPlanConstructionQuantity;

    @ApiModelProperty(value="临补充实际完成施工数")
    private BigDecimal tempActualConstructionQuantity;

    @ApiModelProperty(value="抢修施工施工数")
    private BigDecimal repairPlanConstructionQuantity;

    @ApiModelProperty(value="抢修施工实际完成施工数")
    private BigDecimal repairActualConstructionQuantity;

    @ApiModelProperty(value="计划总数")
    private BigDecimal totalPlanConstructionQuantity;

    @ApiModelProperty(value="计划实际完成总数")
    private BigDecimal totalActualConstructionQuantity;

    @ApiModelProperty(value="情况说明")
    private String  description;

    private  Date creatTime;
}
