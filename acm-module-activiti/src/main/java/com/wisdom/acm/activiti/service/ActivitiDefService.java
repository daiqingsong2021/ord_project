package com.wisdom.acm.activiti.service;

public interface ActivitiDefService {
    /**
     * 删除工作流
     * @param deploymentId
     */
    void deleteProcess(String deploymentId);
}
