package com.wisdom.acm.sys.form;

import com.wisdom.base.common.form.ExtendedColumnForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SectionAddForm extends ExtendedColumnForm {

    @NotBlank(message = "代码不能为空")
    private String teamCode;

    @NotBlank(message = "名称不能为空")
    private String teamName;

    private int parentId;

    @NotBlank(message = "所属业务类型不能为空")
    private String bizType;

    @NotNull(message = "所属业务ID不能为空")
    private Integer bizId;

    @NotBlank(message = "品高标段ID不能为空")
    private String pgSectionId;

    /**
     *  0 不是监理标
     *  1 为监理标
     */
    @NotBlank(message = "是否监理标不能为空")
    private String isSupervise;
}
