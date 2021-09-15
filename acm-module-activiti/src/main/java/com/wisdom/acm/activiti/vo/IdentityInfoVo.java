package com.wisdom.acm.activiti.vo;

import lombok.Data;

@Data
public class IdentityInfoVo {
    /**
     * id
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 流程实例id
     */
    private String procInstId;
    /**
     * 任务id
     */
    private String taskId;
}
