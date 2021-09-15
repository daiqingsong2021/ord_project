package com.wisdom.acm.sys.vo;

import lombok.Data;

@Data
public class SysUserProjectInfoVo {
    /**
     * 项目id
     */
    private Integer projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 部门名称
     */
    private String orgName;
    /**
     * 角色名称
     */
    private String roleName;
}
