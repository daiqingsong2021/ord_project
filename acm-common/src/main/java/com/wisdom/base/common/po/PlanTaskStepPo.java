package com.wisdom.base.common.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/9/17 19:04
 * @Version 1.0
 */
@Table(name = "WSD_PLAN_STEP")
@Data
public class PlanTaskStepPo extends BasePo{

    @Column(name = "name")
    private String name;

    @Column(name="code")
    private String code;

    @Column(name="totalDesign")
    private String totalDesign;

    @Column(name="unit")
    private String unit;

    @Column(name="est_wt")
    private double estwt;


}
