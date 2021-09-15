package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 项目基础信息表
 */
@Table(name = "szxm_rygl_projinfo")
@Data
public class ProjInfoPo extends BasePo {

    /**
     * 项目ID
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 标段ID
     */
    @Column(name = "section_id")
    private Integer sectionId;

    /**
     * 单位编码
     */
    @Column(name = "org_code")
    private String orgCode;

    /**
     * 单位名称
     */
    @Column(name = "org_name")
    private String orgName;

    /**
     * 项目部名称
     */
    @Column(name = "proj_unit_name")
    private String projUnitName;

    /**
     * 单位分类
     */
    @Column(name = "org_category")
    private String orgCategory;

    /**
     * 单位类型
     */
    @Column(name = "org_type")
    private String orgType;

    /**
     * 项目部地址
     */
    @Column(name = "proj_unit_address")
    private String projUnitAddress;

    /**
     * 法人代表
     */
    @Column(name = "corporationer")
    private String corporationer;


    /**
     * 分管项目部领导
     */
    @Column(name = "leader")
    private String leader;


    /**
     * 技术代表
     */
    @Column(name = "artisan")
    private String artisan;


    /**
     * 责任人电话
     */
    @Column(name = "telphone")
    private String telPhone;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 父ID
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 系统组织ID
     */
    @Column(name = "sys_org_id")
    private Integer sysOrgId;


}
