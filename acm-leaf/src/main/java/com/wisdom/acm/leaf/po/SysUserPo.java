package com.wisdom.acm.leaf.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 用户
 */
@Table(name = "wsd_sys_user")
@Data
public class SysUserPo extends BasePo {

    /**
     * 用户账号
     */
    @Column(name = "user_name")
    private String userName;
    /**
     * 用户密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 用户真实名称
     */
    @Column(name = "actu_name")
    private String actuName;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    @Column(name = "remark")
    private String remark;

    @Column(name = "level_")
    private String level;

    /**
     * 1:正常，0:锁定
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 1:普通用户；2：安全审计员；3：安全管理员；4：系统管理员
     */
    @Column(name = "user_type")
    private Integer userType;

    /**
     * 人员状态（0：离职；1：在岗）
     */
    @Column(name = "staff_status")
    private String staffStatus;

    @Column(name = "lastlogin_time")
    protected Date lastloginTime;

    /**
     * 用户性别
     */
    @Column(name = "sex")
    private Integer sex;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 证件类型
     */
    @Column(name = "card_type")
    private String cardType;

    /**
     * 证件号码
     */
    @Column(name = "card_num")
    private String cardNum;

    /**
     * 出生日期
     */
    @Column(name = "birth")
    private Date birth;

    /**
     * 入职日期
     */
    @Column(name = "entry_date")
    private Date entryDate;

    /**
     * 离职日期
     */
    @Column(name = "quit_date")
    private Date quitDate;

    /**
     * 密码错误次数
     */
    @Column(name = "error_number")
    private Integer errorNumber;

    /**
     * 登录验证错误时间
     */
    @Column(name = "error_time")
    private Date errotTime;

    /**
     * 上次修改密码时间
     */
    @Column(name = "update_pwd_time")
    private Date updatePwdTime;

    @Transient
    private Integer orgId;

}
