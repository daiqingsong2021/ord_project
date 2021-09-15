package com.wisdom.acm.sys.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

import java.util.Date;

/**
 * 标段-树型数据VO
 */
@Data
public class SectionTreeVo extends TreeVo<SectionTreeVo> {


    private String value;
    /**
     * 代码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    private String title;

    /**
     * 标段类型编码
     */
    private String typeCode;

    /**
     * 标段类型名称
     */
    private String TypeName;

    /**
     * 项目ID
     */
    private Integer projectId;

    /**
     * 类型
     */
    private String type;
    /**
     * 专业
     */
    private String professional;
    /**
     * 单位分类
     */
    private String orgClassification;

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

    private String openPgd;

    private String openExam;
}
