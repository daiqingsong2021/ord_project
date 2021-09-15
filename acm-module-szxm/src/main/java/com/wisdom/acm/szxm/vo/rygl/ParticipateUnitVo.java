package com.wisdom.acm.szxm.vo.rygl;
import lombok.Data;

import java.util.regex.Pattern;

@Data
public class ParticipateUnitVo {

    /**
     * 主键ID
     */
    private Integer id;

    private Integer sort;

    /**
     * x项目基础信息id
     */
    private String projInfoId;

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
     * 组织机构code
     */
    private String orgCode;
    /**
     * 组织机构名称
     */
    private String orgName;

    /**
     * 项目经理
     */
    private String projectLeader;

    /**
     * 监理单位
     */
    private String supervisionUnit;

    /**
     * 总监代表
     */
    private String director;

    /**
     *分管业主
     */
    private String branchManage;

    /**
     * 专业
     */
    private String professional;




}
