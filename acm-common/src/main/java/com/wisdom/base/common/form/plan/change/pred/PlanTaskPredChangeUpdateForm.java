package com.wisdom.base.common.form.plan.change.pred;

import lombok.Data;

/**
 * @Author: szc
 *
 * @Date: 2019/3/12 14:17
 * @Version 1.0
 */
@Data
public class PlanTaskPredChangeUpdateForm {
    // 主键ID
    private Integer id;
    // 原始逻辑关系ID
    private Integer logicId;
    // 新逻辑关系类型
    private String newRelationType;
    // 新延时
    private Double newLagNum;
}
