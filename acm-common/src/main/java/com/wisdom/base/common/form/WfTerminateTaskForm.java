package com.wisdom.base.common.form;

import lombok.Data;

/**
 * 驳回流程表单
 */
@Data
public class WfTerminateTaskForm {

    /**
     * 流程实例id
     */
    private String procInstId;

    /**
     * 工作项id
     */
    private String taskId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 意见
     */
    private String comment;
}
