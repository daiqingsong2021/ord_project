package com.wisdom.acm.dc3.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;

@Data
public class EnergyDetailVo
{
    /**
     * 主键ID
     */
    private String id;

    /**
     * 日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private String recordTime;
    /**
     *线路
     */
    private String line;
    /**
     *线路
     */
    private String lineName;


    /**
     * 逗号分隔：一期,二期,三期
     */
    private String linePeriod;

    /**
     * 0：主变电所；1：动照；2：牵引
     */
    private String powerType;

    /**
     * 0：主变电所；1：动照；2：牵引
     */
    private GeneralVo powerTypeVo = new GeneralVo();

    /**
     * 变电所/车站
     */
    private String subStation;

    /**
     * 开关柜
     */
    private String switchgear="";

    /**
     * 耗电量
     */
    private String powerConsumption;

    /**
     *描述
     */
    private String description="";

    /**
     *日况id
     */
    private String dailyId;

}
