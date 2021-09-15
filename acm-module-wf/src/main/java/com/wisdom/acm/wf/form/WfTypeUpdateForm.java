package com.wisdom.acm.wf.form;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WfTypeUpdateForm extends BaseForm {

    private Integer id;

    //业务代码
    @NotBlank(message = "业务代码不能为空")
    @LogParam(title = "业务代码")
    private String typeCode;

    //业务名称
    @NotBlank(message = "业务名称不能为空")
    @LogParam(title = "业务名称")
    private String typeName;

    //表单地址
    @LogParam(title = "表单地址")
    private String url;

    //流程事件
    @LogParam(title = "流程事件")
    private String event;

    //模块代码
    @LogParam(title = "模块代码")
    private String moduleCode;
}
