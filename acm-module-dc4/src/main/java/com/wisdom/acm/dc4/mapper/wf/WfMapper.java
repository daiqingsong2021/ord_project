package com.wisdom.acm.dc4.mapper.wf;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 流程Mapper
 */
@Repository
public interface WfMapper
{
    /**
     * 根据流程实例ID 查询创建者ID
     * @param instanceId
     * @return
     */
    Integer getProcCreatorByProcInstId(@Param("instanceId") String instanceId);
}
