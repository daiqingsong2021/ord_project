package com.wisdom.acm.sys.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SysUpdatePassWordForm {

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassWord;

    /*
     *新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String newPassWord;
}
