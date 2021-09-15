package com.wisdom.base.common.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "wsd_sys_org")
@Data
public class SysOrgPo extends BasePo {

    /**
     * 名称
     */
    @Column(name = "org_name")
    private String orgName;

    /**
     * 代码
     */
    @Column(name = "org_code")
    private String orgCode;

    /**
     * 父ID
     */
    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "biz_type")
    private String bizType;

    @Column(name = "biz_id")
    private Integer bizId;
    /**
     * 组织类型
     */
    @Column(name="org_type")
    private Integer orgType;

    /**
     * OBS级别
     */
    @Column(name="org_level")
    private String orgLevel;

    /**
     * 组织状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 所属地域
     */
    @Column(name = "grog_loc")
    private String grogLoc;

    /**
     * 组织地址
     */
    @Column(name = "org_address")
    private String orgAddress;

    /**
     * 邮编
     */
    @Column(name = "zip_code")
    private String zipCode;

    /**
     * 联系人
     */
    @Column(name = "org_contract")
    private String orgContract;

    /**
     * 联系电话
     */
    @Column(name = "contract_num")
    private String contractNum;

    /**
     * 电子邮箱
     */
    @Column(name = "org_email")
    private String orgEmail;

    /**
     * 网站地址
     */
    @Column(name = "web_address")
    private String webAddress;

    /**
     * 生效日期
     */
    @Column(name = "effect_date")
    private Date effectDate;

    /**
     * 失效日期
     */
    @Column(name = "invalid_date")
    private Date invalidDate;

    /**
     * 删除标识（1：删除，2：未删除）
     */
    @Column(name = "del")
    private Integer del;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

}

