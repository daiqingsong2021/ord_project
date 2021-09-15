package com.wisdom.acm.szxm.po.rygl;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "szxm_rygl_all_record")
@MappedSuperclass
@Data
public class AllKqRecordPo
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
     * 部门名称
     */
    @Column(name = "ORG_NAME")
    private String orgName;

    /**
     * 组织机构ID
     */
    @Column(name = "ORG_ID")
    private Integer orgId;

    /**
     * 打卡日期 格式如20190708
     */
    @Column(name = "check_time")
    private Integer checkTime;

    /**
     * 0 管理人员 1 特殊工种 2 普通人员
     */
    @Column(name = "type")
    private String type;
}
