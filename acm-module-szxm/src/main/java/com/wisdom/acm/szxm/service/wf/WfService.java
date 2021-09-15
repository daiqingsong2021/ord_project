package com.wisdom.acm.szxm.service.wf;

import java.util.List;
import java.util.Map;

public interface WfService
{
    /**
     * 根据流程实例ID 查询创建者ID
     * @param instanceId
     * @return
     */
    Integer getProcCreatorByProcInstId(String instanceId);
}
