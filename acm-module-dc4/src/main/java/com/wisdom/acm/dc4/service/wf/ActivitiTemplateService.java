package com.wisdom.acm.dc4.service.wf;


import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2020-03-30 9:26
 * Description：<描述>
 */
public interface ActivitiTemplateService {

    /**
     * 根据流程节点id查询
     * @param activitiId
     */
    String queryActiviti(String activitiId);

    /**
     * 根据流程节点id查询
     * @param activitiIds
     */
    Map<String, String> queryActivitiByIds(List<String> activitiIds);
}
