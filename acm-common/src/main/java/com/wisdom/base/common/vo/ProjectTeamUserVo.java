package com.wisdom.base.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 项目团队成员
 */
@Data
public class ProjectTeamUserVo {

    private Integer id;

    /**
     * 团队ID
     */
    private Integer teamId;

    /**
     * 团队代码
     */
    private String teamCode;

    /**
     * 团队名称
     */
    private String teamName;

    private UserVo user;

    private List<GeneralVo> roles;

    /**
     * 职务
     */
    private String position;
    /**
     * 专业
     */
    private String professional;

    /**
     * 员工状态1 ：在岗，  0 ：离职
     */
    private GeneralVo staffStatus;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 出生日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date birth;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 用户性别
     */
    private GeneralVo sex;

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
     * 状态 1:正常，2:锁定
     */
    private GeneralVo status;
    /**
     * 密级
     */
    private GeneralVo level;
}
