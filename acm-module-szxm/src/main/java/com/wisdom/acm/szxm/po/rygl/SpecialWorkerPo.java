package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "szxm_rygl_speci_worker")
@Data
public class SpecialWorkerPo extends BasePo
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
     * 工种类型 数据字典 szxm.rygl.worktype 多选，以逗号分隔
     */
    @Column(name = "work_type")
    private String workType;


    /**
     * 流程状态
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
