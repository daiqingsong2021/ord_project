package com.wisdom.base.common.vo.plan.feedback;

import com.wisdom.base.common.vo.TreeVo;
import lombok.Data;

/**
 * @Author: szc
 * @Date: 2019/2/27 14:47
 * @Version 1.0
 */
@Data
public class PlanTaskFeedbackReleaseTreeVo extends TreeVo<PlanTaskFeedbackReleaseTreeVo> {

    private String name;

    private String code;
    //节点类型
    private String type;
    // 本次进展完成百分比
    private Double applyPct;
    // 是否可批准/审批
    private Integer check;
    // 本次进展ID
    private Integer feedbackId;
    // 任务类型
    private Integer taskType;
}
