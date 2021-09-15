package com.wisdom.acm.dc4.service.wf;

public interface WfService
{
    /**
     * 根据流程实例ID 查询创建者ID
     * @param instanceId
     * @return
     */
    Integer getProcCreatorByProcInstId(String instanceId);
}
