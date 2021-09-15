package com.wisdom.acm.processing.vo.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;
import java.util.Date;

/**
 * @author zll
 * 2020/12/24/024 10:01
 * Description:<描述>
 */
@Data
public class FineDailyReportVo {
    private Integer creator;
    private Integer id;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date reportName;
    private String line;

    //'日报类型：0：定时器生成；1：人员生成'
    private String reportType;

    //'审批人'
    private Integer reviewer;

    //'审批状态'
    private String reviewStatus;

    //'备注'
    private String description;
    //文件id
    private Integer fileId;

    //审批人or
    private String reviewor;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd hh:mm:ss")
    private Date creatTime;

    //创建人
    private String initMan;

    private String sortNum;

    private GeneralVo reportTypeVo=new GeneralVo();

    private GeneralVo reviewStatusVo=new GeneralVo();
}
