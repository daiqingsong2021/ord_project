package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "SZXM_RYGL_LWRYKQ_RECORD")
@MappedSuperclass
@Data
public class LwryKqRecordPo
{

    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    protected Integer id;

    /**
     * 排序编号
     */
    @Column(name = "sort_num")
    protected Integer sort;

    /**
     * 最后更新时间
     */
    @Column(name = "last_upd_time")
    protected Date lastUpdTime;

    /**
     * 最后更新人
     */
    @Column(name = "last_upd_user")
    protected Integer lastUpdUser;

    /**
     * 创建日期
     */
    @Column(name = "creat_time")
    protected Date creatTime;

    /**
     * 创建人
     */
    @Column(name = "creator")
    protected Integer creator;

    /**
     * 更新IP
     */
    @Column(name = "last_upd_ip")
    protected String lastUpdIp;

    /**
     * 版本号
     */
    @Column(name = "wsdver")
    protected Long wsdver = 0l;
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
     * 人员ID
     */
    @Column(name = "people_id")
    private Integer peopleId;

    /**
     * 人员名称
     */
    @Column(name = "PEOPLE_NAME")
    private String peopleName;

    /**
     * 人员职务
     */
    @Column(name = "job")
    private String job;

    /**
     * 联系方式
     */
    @Column(name = "telphone")
    private String telPhone;

    /**
     * 身份证号
     */
    @Column(name = "id_card")
    private String idCard;

    /**
     * 是否缺勤 1 是 0 否
     */
    @Column(name = "is_qq")
    private Integer isQq;

    /**
     * 部门名称
     */
    @Column(name = "ORG_NAME")
    private String orgName;

    @Column(name = "ORG_ID")
    private Integer orgId;
    /**
     * 记录日期 格式如20190708
     */
    @Column(name = "jl_rq")
    private Integer jlRq;
}
