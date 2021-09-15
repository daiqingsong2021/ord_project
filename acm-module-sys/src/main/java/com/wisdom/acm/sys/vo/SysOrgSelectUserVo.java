package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysOrgSelectUserVo {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户真实名称
     */
    private String actuName;

    /**
     * 1:正常，2:锁定
     */
    private String status;

    /**
     * 员工状态
     */
    private String staffStatus;

    /**
     * 用户性别
     */
    private GeneralVo sex;

    private List<SysRoleVo> roles;

    private String retRole;
}

