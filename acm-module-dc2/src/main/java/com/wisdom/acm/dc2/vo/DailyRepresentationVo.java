package com.wisdom.acm.dc2.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.po.BasePo;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
public class DailyRepresentationVo
{

    private Integer id;
    /**
     * 记录日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordTime;
    /**
     *线路
     */
    private String line;
    /**
     *线路
     */
    private String line_name;

    /**
     *模块标题
     */
    private String moudleTitle;
    /**
     *说明
     */
    private String description;
    /**
     *说明
     */
    private String picturePath;

    private GeneralVo createrVo = new GeneralVo();

}
