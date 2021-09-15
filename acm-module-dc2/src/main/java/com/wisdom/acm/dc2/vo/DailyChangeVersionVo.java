package com.wisdom.acm.dc2.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class DailyChangeVersionVo
{

    private Integer id;
    /**
     * 创建日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creatTime;
    /**
     * 模块记录的id
     */
    private String moudleRecordId;
    /**
     *模块名称
     */
    private String moudleName;

    /**
     *模块修改备注
     */
    private String modifyRemark;
    /**
     *修改内容
     */
    private String modifyContent;


    private GeneralVo createrVo = new GeneralVo();

}
