package com.wisdom.acm.sys.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "org_section_relation")
@Data
public class OrgSectionRelation {

    /**
     * 主键id
     */
    @Id
    private Integer id;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 标段id
     */
    @Column(name = "section_id")
    private Integer sectionId;

    /**
     * 标段编码
     */
    @Column(name = "section_code")
    private String sectionCode;
    /**
     * 标段名称
     */
    @Column(name = "section_name")
    private String sectionName;
    /**
     * 三级部门id
     */
    @Column(name = "org_id")
    private Integer orgId;

    /**
     * 三级部门编码
     */
    @Column(name = "org_code")
    private String orgCode;
    /**
     * 部门名称
     */
    @Column(name = "org_name")
    private String orgName;

    /**
     * 关系最后修改日期
     *
     */
    @Column(name="update_time")
    private Date updateTime;

    /**
     * 最近修改人
     */
    @Column(name = "update_user")
    private String updateUser;
}

