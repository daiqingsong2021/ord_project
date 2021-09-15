package com.wisdom.acm.dc4.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
/**
 * ConstructionDetailUpdateForm class
 * @author  chenhai
 * @data 2020/08/13
 * 施工日况更新
 */
@Data
public class ConstructionDailyUpdateForm extends BaseForm
{

    private String id;

   /* @ApiModelProperty(value="日期")
    @LogParam(title = "日期")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;

    @ApiModelProperty(value="线路")
    @LogParam(title = "线路")
    private String line;*/

    @LogParam(title = "周计划施工数")
    @ApiModelProperty(value="周计划施工数")
    private BigDecimal weeklyPlanConstructionQuantity;

    @LogParam(title = "周计划实际完成施工数")
    @ApiModelProperty(value="周计划实际完成施工数")
    private BigDecimal weeklyActualConstructionQuantity;

    @LogParam(title = "日补充施工数")
    @ApiModelProperty(value="日补充施工数")
    private BigDecimal dailyPlanConstructionQuantity;

    @LogParam(title = "日补充实际完成施工数")
    @ApiModelProperty(value="日补充实际完成施工数")
    private BigDecimal dailyActualConstructionQuantity;

    @LogParam(title = "临补充施工数")
    @ApiModelProperty(value="临补充施工数")
    private BigDecimal tempPlanConstructionQuantity;

    @LogParam(title = "临补充实际完成施工数")
    @ApiModelProperty(value="临补充实际完成施工数")
    private BigDecimal tempActualConstructionQuantity;

    @LogParam(title = "抢修施工施工数")
    @ApiModelProperty(value="抢修施工施工数")
    private BigDecimal repairPlanConstructionQuantity;

    @LogParam(title = "抢修施工实际完成施工数")
    @ApiModelProperty(value="抢修施工实际完成施工数")
    private BigDecimal repairActualConstructionQuantity;

    @ApiModelProperty(value="计划总数")
    //@LogParam(title = "计划总数")
    private BigDecimal totalPlanConstructionQuantity;
    @ApiModelProperty(value="计划实际完成总数")
    private BigDecimal totalActualConstructionQuantity;

    @ApiModelProperty(value="情况说明")
    private String  description;

}
