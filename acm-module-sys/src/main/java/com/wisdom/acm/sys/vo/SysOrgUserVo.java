package com.wisdom.acm.sys.vo;

import lombok.Data;

@Data
public class SysOrgUserVo {

    private int userId;

    private int userOrgId;

    private int orgId;

    private String roleIds;

    private String position;

    private String professional;

    private Integer sort;
}
