package com.wisdom.acm.sys.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class SysUserAddBatchFrom {

    /**
     * 用户账号
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    private String email;

    /**
     * 用户名称
     */
    @NotBlank(message = "用户名称不能为空")
    private String actuName;

    private String level;

    /**
     * 用户性别
     */
    @NotNull(message = "用户性别不能为空")
    private Integer sex;

    private List<Integer> roles;

    @NotNull(message = "所属部门不能为空")
    private Integer orgId;
}


