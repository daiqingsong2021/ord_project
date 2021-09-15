package com.wisdom.acm.sys.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Data
public class SysRoleVo {

    private Integer id;

    private String roleCode;

    private String roleName;

    private String roleDesc;

}
