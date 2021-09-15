package com.wisdom.acm.wf.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WfDelegateProcAddForm {

    @NotNull(message = "流程代理ID不能为空")
    private Integer delegateId;

    @NotBlank(message = "流程业务代码不能为空")
    private String bizTypeCode;
}
