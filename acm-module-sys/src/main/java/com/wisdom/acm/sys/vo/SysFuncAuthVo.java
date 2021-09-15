package com.wisdom.acm.sys.vo;

import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class SysFuncAuthVo {

    private Integer roleId;

    private String roleCode;

    private String roleName;

    private List<SysMenuVo> menuList;

}
