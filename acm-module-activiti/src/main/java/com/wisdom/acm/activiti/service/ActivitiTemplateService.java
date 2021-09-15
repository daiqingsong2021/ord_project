package com.wisdom.acm.activiti.service;


import com.wisdom.acm.activiti.form.ActivitiTemplateAddForm;
import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2020-03-30 9:26
 * Description：<描述>
 */
public interface ActivitiTemplateService {

    /**
     * 添加流程对应模板
     * @param activitiTemplateAddForm
     */
    void addActivitiTemplate(ActivitiTemplateAddForm activitiTemplateAddForm);

    /**
     * 查询流程对应模板
     * @param activitiId
     */
    String getActivitiTemplate(String activitiId);

    /**
     * 根据流程节点id查询
     * @param activitiIds
     */
    Map<String, String> queryActivitiByIds(List<String> activitiIds);
}
