package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "szxm_rygl_glrykq_record")
@MappedSuperclass
@Data
public class GlryKqRecordPo
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
     * 是否请假 1是 0否
     */
    @Column(name = "is_qj")
    private Integer isQj;

    /**
     * 是否迟到
     */
    @Column(name = "IS_CD")
    private Integer isCd;

    /**
     * 是否早退
     */
    @Column(name = "IS_ZT")
    private Integer isZt;

    /**
     * 是否异常
     */
    @Column(name = "IS_YC")
    private Integer isYc;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;


    private String startTimeTemp;
    /**
     * 完成时间
     */
    @Column(name = "end_time")
    private Date endTime;

    private String endTimeTemp;
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
