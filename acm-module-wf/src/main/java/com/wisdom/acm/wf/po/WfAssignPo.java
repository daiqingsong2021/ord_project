package com.wisdom.acm.wf.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "wsd_wf_assign")
public class WfAssignPo extends BasePo {

    /**
     * 业务类型id
     */
    @Column(name = "type_id")
    private Integer typeId;
    /**
     * 流程定义id
     */
    @Column(name = "model_id")
    private String modelId;
    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Integer projectId;
    /**
     * 组织id
     */
    @Column(name = "org_id")
    private Integer orgId;
    /**
     * 分类码id
     */
    @Column(name = "clasf_id")
    private Integer clasfId;
    /**
     * 计划级别
     */
    @Column(name = "plan_level")
    private String planLevel;
}
