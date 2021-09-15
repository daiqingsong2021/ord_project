package com.wisdom.acm.base.vo.tmpltask;

import lombok.Data;

@Data
public class BaseTmplTaskPredVo {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 任务
     */
    private Integer taskId;

    /**
     * 紧前任务
     */
    private Integer predTaskId;

    /**
     * 名称
     */
    private String taskName;

    /**
     * 代码
     */
    private String taskCode;

    /**
     * 关系类型
     */
    private String relationType;

    /**
     * 延迟
     */
    private Double lagQty;

    /**
     * 工期
     */
    //private Double planDrtn;
}
