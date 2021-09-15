package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "szxm_rygl_people_entry")
@Data
public class PeopleEntryPo extends BasePo
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
     * 项目基础信息ID 外键
     */
    @Column(name = "proj_info_id")
    private Integer projInfoId;

    /**
     * 进退场编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 进退场类别 0 进场 1退场
     */
    @Column(name = "type")
    private String type;

    @Column(name = "org_name")
    private String orgName;


    /**
     * 进退场时间
     */
    @Column(name = "entry_time")
    private Date entryTime;

    /**
     * 人员类型0 普通人员 1管理人员
     */
    @Column(name = "peo_entry_type")
    private String peoEntryType;

    /**
     * 状态
     */
    @Column(name = "status")
    private String status;


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
