package com.wisdom.base.common.vo.plan.task;

import lombok.Data;

/**
 * 新增任务/WBS初始数据
 *
 */
@Data
public class PredecessorLinkVo {

    // 任务ID
    private Integer TaskUID;
    // 延时
    private Double LinkLag;
    // 延时类型
    private Integer LagFormat;
    // 默认完成时间
    private Integer Type;
    // 计划开始最小值
    private Integer PredecessorUID;
}
