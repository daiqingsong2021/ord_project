package com.wisdom.acm.dc2.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "odr_moudle_daily_representation")
@Data
public class DailyRepresentationPo extends BasePo
{

    /**
     * 记录日期
     */
    @Column(name = "RECORD_TIME")
    private Date recordTime;
    /**
     *线路
     */
    @Column(name = "LINE")
    private String line;


    /**
     *模块标题
     */
    @Column(name = "MOUDLE_TITLE")
    private String moudleTitle;
    /**
     *说明
     */
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     *说明
     */
    @Column(name = "PICTURE_PATH")
    private String picturePath;



}
