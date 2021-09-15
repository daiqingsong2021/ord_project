package com.wisdom.acm.sys.form;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SysRoleUpdateForm extends BaseForm {

    @NotNull(message = "id不能为空")
    private Integer id;

    @NotBlank(message = "角色代码不能为空")
    @LogParam(title = "角色代码")
    private String roleCode;

    @NotBlank(message = "角色名称不能为空")
    @LogParam(title = "角色名称")
    private String roleName;

    @LogParam(title = "角色描述")
    private String roleDesc;

}
