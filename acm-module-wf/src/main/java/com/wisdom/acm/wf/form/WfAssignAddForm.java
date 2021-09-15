package com.wisdom.acm.wf.form;

import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

@Data
public class WfAssignAddForm extends BaseForm {
    /**
     * 业务id
     */
    private Integer typeId;
    /**
     * 模版id
     */
    private String modelId;
}
