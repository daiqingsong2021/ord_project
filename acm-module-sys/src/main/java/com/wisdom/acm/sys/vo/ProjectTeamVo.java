package com.wisdom.acm.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.Date;

@Data
public class ProjectTeamVo extends TreeVo<ProjectTeamVo> {

    private String teamCode;

    private String teamName;

    private String bizType;

    private String pgSectionId;

    private Integer bizId;

    //排序号
    private Integer sortNum;

    /**
     * 标段状态Vo
     */
    private GeneralVo sectionStatusVo=new GeneralVo();

    /**
     * 开工日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date startDate;

    /**
     * 完工日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date endDate;

    private String openPgd;

    /**
     * 派工单开始日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date pgdStartDate;

    /**
     * 派工单结束日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date pgdEndDate;

    private String openExam;
    /**
     * 考核开始日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date examStartDate;

    /**
     * 考核结束日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date examEndDate;

}
