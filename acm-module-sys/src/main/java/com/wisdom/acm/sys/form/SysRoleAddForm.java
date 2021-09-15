package com.wisdom.acm.sys.form;

import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class SysRoleAddForm  extends BaseForm {

    @NotBlank(message = "角色代码不能为空")
    private String roleCode;

    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    private String roleDesc;

    public String getLogContent(){
        return "增加角色，角色名称："+ this.getRoleName();
    }
}
