package com.wisdom.base.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 团队-标段
 */
@Data
public class ProjectTeamVo {
    /**
     * 项目ID
     */
    private Integer projectId;
    /**
     * ID
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 代码
     */
    private String code;
    /**
     * 标段类型
     */
    private String typeCoe;

    /**
     * 标段类型名称
     */
    private String typeName;
    /**
     * 父级ID
     */
    private Integer parentId;
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
     * 施工单位
     */
    private List<GeneralVo> cuList;
    /**
     * 监理单位
     */
    private List<GeneralVo> ccuList;

    /**
     * 业主代表
     */
    private List<GeneralVo> ownerList;


    /**
     * 品高标段id
     */
    private String pgSectionId;

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
