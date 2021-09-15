package com.wisdom.acm.sys.vo;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysUserInfoVo {

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
     * 用户性别
     */
    private GeneralVo sex;

    /**
     * 手机号
     */
    private String phone;

    private GeneralVo status;

    private String email;
    /**
     * 证件类型
     */
    private GeneralVo cardType;


    private GeneralVo staffStatus;

    /**
     * 证件号码
     */
    private String cardNum;

    /**
     * 出生日期
     */
    private Date birth;

    /**
     * 入职日期
     */
    private Date entryDate;

    /**
     * 离职日期
     */
    private Date quitDate;

    /**
     * 密级
     */
    private GeneralVo level;

    private Integer sort;


    private List<SysRoleVo> roles;

    private GeneralVo org;

    /**
     * 所属部门
     */
    private GeneralVo dept;

    private String userType;

    /**
     * 备注
     */
    private String remark;
    /**
     * 用户项目信息
     */
    private List<SysUserProjectInfoVo> projectInfoVos;

    /**
     * 职位
     */
    private String position;

    /**
     * 专业
     */
    private String professional;

    /**
     * 学历
     */
    private String education;
}


