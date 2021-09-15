package com.wisdom.base.common.form.szxm;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class InventoryUpdateForm
{
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 更新时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date updateTime;

    /**
     * 本周消耗量
     */
    private Integer thisWeekConsume;

    /**
     * 下周计划量
     */
    private Integer nextWeekPlanQuantity;
}
