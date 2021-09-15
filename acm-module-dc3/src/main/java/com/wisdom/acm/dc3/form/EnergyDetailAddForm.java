package com.wisdom.acm.dc3.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

@Data
public class EnergyDetailAddForm extends BaseForm
{

    /**
     * 日期
     */
    @LogParam(title = "日期")
    private String recordTime;

    /**
     *线路
     */
    @LogParam(title = "线路")
    private String line;


    /**
     * 逗号分隔：一期,二期,三期
     */
    @LogParam(title = "线路分期")
    private String linePeriod;

    /**
     * 0：主变电所；1：动照；2：牵引
     */
    private String powerType;

    /**
     * 变电所/车站
     */
    private String subStation;

    /**
     * 开关柜
     */
    private String switChgear;

    /**
     * 耗电量
     */
    private String powerConsumption;

    /**
     *描述
     */
    private String description;


}
