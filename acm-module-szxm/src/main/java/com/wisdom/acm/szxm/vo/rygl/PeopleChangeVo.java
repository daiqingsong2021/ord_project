package com.wisdom.acm.szxm.vo.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;

@Data
public class PeopleChangeVo
{
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 项目ID
     */
    private Integer projectId;

    private String projectName;

    /**
     * 标段ID
     */
    private Integer sectionId;

    /**
     * 标段编码
     */
    private String sectionCode;

    /**
     * 标段名称
     */
    private String sectionName;

    /**
     * 被变更人
     */
    private Integer bchangerId;

    private String code;

    private String bchanger;

    private String achanger;

    private Integer achangerId;

    private String changeGw;

    private String contractNumber;

    private String changeReason;

    private String orgName;

    private Integer projInfoId;

    /**
     * 变更日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date changeTime;

    /**
     * 创建日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date createTime;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 流程状态
     */
    private GeneralVo statusVo=new GeneralVo();
}
