package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class SysUserVo {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户code
     */
    private String userCode;
    /**
     * 用户真实名称
     */
    private String actuName;

    /**
     * 1:正常，2:锁定
     */
    private GeneralVo status;

    protected Date lastloginTime;

    /**
     * 用户性别
     */
    private GeneralVo sex;

    private String userType;
    /**
     * 员工状态
     */
    private GeneralVo staffStatus;

    private Integer visits;

    public Integer getVisits() {
        return 0;
    }

    private List<SysRoleVo> roles;

    private String retRole;

    private Integer orgId;

    private Integer sortNum;

    /**
     * 角色关系表id
     */
    private Integer userRoleId;

    /**
     * 职位
     */
    private String position;

    /**
     * 专业
     */
    private String professional;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 出生日期
     */
    private Date birth;

    private String email;

    /**
     * 证件类型
     */
    private String cardType;

    /**
     * 证件号码
     */
    private String cardNum;

    /**
     * 密级
     */
    private GeneralVo level;

    /**
     * 更新时间
     */
    private Date updateTime;
}
