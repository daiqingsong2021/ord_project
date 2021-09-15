package com.wisdom.acm.sys.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class SysUserAddFrom extends BaseForm {

    /**
     * 用户账号
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户名称
     */
    @NotBlank(message = "用户名称不能为空")
    private String actuName;

    /**
     * 用户性别
     */
    @NotNull(message = "用户性别不能为空")
    private Integer sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 证件类型
     */
    private String cardType;

    /**
     * 证件号码
     */
    private String cardNum;

    /**
     * 出生日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date birth;

    private String email;
    /**
     * 入职日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date entryDate;

    /**
     * 离职日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date quitDate;

    private String level;

    private Integer sort;

    @NotNull(message = "人员状态不能为空")
    private Integer staffStatus;

    private List<Integer> roles;

    @NotNull(message = "所属部门不能为空")
    private Integer orgId;

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

    /**
     * 用户编码
     */
    private String userCode;

    public String getLogContent(){
        return "增加用户，用户名称："+ this.getActuName();
    }
}


