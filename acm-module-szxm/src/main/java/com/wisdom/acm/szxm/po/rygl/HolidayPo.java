package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "szxm_rygl_holiday_records")
@Data
public class HolidayPo extends BasePo
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
     * 人员ID
     */
    @Column(name = "people_id")
    private Integer peopleId;

    /**
     * 请假期间负责工作人员ID
     */
    @Column(name = "agent_id")
    private Integer agentId;

    /**
     * 人员名称
     */
    @Column(name = "PEOPLE_NAME")
    private String peopleName;
    /**
     * 编号
     */
    @Column(name = "serial_id")
    private String serialId;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 请假天数
     */
    @Column(name = "days")
    private Integer days;

    /**
     * 假期类别
     */
    @Column(name = "type")
    private String type;

    /**
     * 请假原因
     */
    @Column(name = "reason")
    private String reason;

    /**
     * 流程状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 部门名称
     */
    @Column(name = "ORG_NAME")
    private String orgName;

    /**
     * 人员职务
     */
    @Column(name = "RY_ZW")
    private String ryZw;

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

    /**
     * 请假人员类型
     */
    @Column(name = "RY_TYPE")
    private String ryType;
}
