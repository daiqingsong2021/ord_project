package com.wisdom.acm.dc4.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ConstructionDetailAddForm class
 * @author  chenhai
 * @data 2020/08/13
 * 新增施工日况对象
 */
@Data
public class ConstructionDailyAddForm  extends BaseForm
{

    @ApiModelProperty(value="日期")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @LogParam(title = "日期")
    private Date recordTime;

    @ApiModelProperty(value="线路")
    @LogParam(title = "线路")
    private String line;

    @ApiModelProperty(value="周计划施工数")
    @LogParam(title = "周计划施工数")
    private BigDecimal weeklyPlanConstructionQuantity;

    @ApiModelProperty(value="周计划实际完成施工数")
    @LogParam(title = "周计划实际完成施工数")
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

}
