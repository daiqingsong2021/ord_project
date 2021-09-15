package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 证书管理表
 */
@Table(name = "szxm_rygl_cert_gl")
@Data
public class CertGlPo extends BasePo
{

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
     * 证书名称
     */
    @Column(name = "cert_name")
    private String certName;

    /**
     * 验证地址
     */
    @Column(name = "cert_verify_url")
    private String certVerifyUrl;

    /**
     * 预警间隔天数
     */
    @Column(name = "warn_period")
    private Integer warnPeriod;
}
