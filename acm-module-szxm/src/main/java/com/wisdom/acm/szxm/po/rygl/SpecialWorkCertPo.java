package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 特殊工种证书表
 */
@Table(name = "szxm_rygl_sw_cert")
@Data
public class SpecialWorkCertPo extends BasePo
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
     *证书管理ID
     */
    @Column(name = "cert_gl_id")
    private Integer certGlId;

    /**
     * 证书有效期
     */
    @Column(name = "cert_expiration_time")
    private Date certExpirationTime;

    /**
     * 证书有效期
     */
    @Column(name = "cert_f_ptime")
    private Date certFirstPublishTime;


    /**
     * 证书编号
     */
    @Column(name = "cert_num")
    private String certNum;


    /**
     * 预警状态 0 正常  1 预警  2 过期
     */


    /**
     * 特殊工种外键ID
     *
     */
    @Column(name = "special_worker_id")
    private Integer specialWorkerId;
}
