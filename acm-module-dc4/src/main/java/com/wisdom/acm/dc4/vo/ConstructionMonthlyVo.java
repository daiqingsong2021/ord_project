package com.wisdom.acm.dc4.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.po.BasePo;
import com.wisdom.base.common.vo.GeneralVo;
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
public class ConstructionMonthlyVo  {

    protected Integer id;

    @ApiModelProperty(value="日期")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;

    @ApiModelProperty(value="线路")
    private String line;

    private String lineName;

    @ApiModelProperty(value="周计划施工数")
    private BigDecimal weeklyPlanConstructionQuantity;

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

    private  Date creatTime;
    /**
     * 创建人
     */
    private GeneralVo createVo = new GeneralVo();
}
