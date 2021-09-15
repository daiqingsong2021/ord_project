package com.wisdom.acm.sys.vo;

import lombok.Data;

import javax.persistence.Table;

@Data
public class SysRoleAuthUpdateVo {

    private Integer id;

    private String resCode;

    private String resType;
}
