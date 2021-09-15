package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "szxm_rygl_people_change")
@Data
public class PeopleChangePo extends BasePo
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
     * 项目基础信息ID
     */
    @Column(name = "proj_info_id")
    private Integer projInfoId;

    /**
     * 变更单位名称
     */
    @Column(name = "org_name")
    private String orgName;

    /**
     * 被变更人
     */
    @Column(name = "b_changer")
    private String bchanger;

    /**
     * 被变更人ID
     */
    @Column(name = "b_change_id")
    private Integer bchangerId;

    /**
     * 变更后人员
     */
    @Column(name = "a_changer")
    private String achanger;

    /**
     * 变更后人员ID
     */
    @Column(name = "a_change_id")
    private Integer achangerId;

    /**
     * 变更岗位
     */
    @Column(name = "change_gw")
    private String changeGw;

    /**
     * 合同编号
     */
    @Column(name = "contract_number")
    private String contractNumber;

    /**
     * 变更原因
     */
    @Column(name = "change_reason")
    private String changeReason;


    /**
     * 流程状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 编号
     */
    @Column(name = "code")
    private String code;

    /**
     * 变更日期
     */
    @Column(name = "change_time")
    private Date changeTime;


    // 新增
    /**
     * 发起人ID
     */
    @Column(name = "initiator_id")
    private Integer initiatorId;

    /**
     * 发起时间
     */
    @Column(name = "init_time")
    private Date initTime;

    /**
     * 发起人
     */
    @Column(name = "initiator")
    private String initiator;
}
