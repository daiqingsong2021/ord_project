package com.wisdom.acm.activiti.form;

import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Author：wqd
 * Date：2020-03-30 9:27
 * Description：<描述>
 */
@Data
public class ActivitiTemplateAddForm extends BaseForm {
    /**
     * 流程节点id
     */
    @NotBlank
    private String activitiId;

    /**
     * 筛选编码
     */
    private String screenCode;
}
