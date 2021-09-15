package com.wisdom.acm.base.form.tmpltask;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseTmplTaskDelvUpdateForm {

    private Integer id;

    @NotNull(message = "代码不能为空")
    private String delvCode;

    @NotNull(message = "名称不能为空")
    private String delvName;

    private Integer planNum;

    @NotNull(message = "类别不能为空")
    private String delvType;
}
