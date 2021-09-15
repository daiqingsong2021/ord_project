package com.wisdom.acm.base.form.tmpldelv;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseTmpldelvAddForm extends BaseForm {

    @NotNull(message = "交付物模板Id不能为空")
    private Integer typeId;

    @NotNull(message = "父Id不能为空")
    private Integer parentId;

    @NotNull(message = "标题不能为空")
    @LogParam(title = "标题")
    private String delvTitle;

    @NotNull(message = "编号不能为空")
    @LogParam(title = "编号")
    private String delvNum;

    @LogParam(title = "类别")
    private String delvType;

    @LogParam(title = "备注")
    private String delvDesc;

    private String type;

}