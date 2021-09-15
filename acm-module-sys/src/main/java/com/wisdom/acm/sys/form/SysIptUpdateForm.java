package com.wisdom.acm.sys.form;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SysIptUpdateForm extends BaseForm {

    private Integer id;
    /**
     * 代码
     */
    @NotBlank(message = "代码不能为空")
    @LogParam(title = "代码")
    private String iptCode;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @LogParam(title = "名称")
    private String iptName;

    @LogParam(title = "等级")
    private String level;

    /**
     * 备注
     */
    @LogParam(title = "备注")
    private String remark;

    private Integer sort;
}
