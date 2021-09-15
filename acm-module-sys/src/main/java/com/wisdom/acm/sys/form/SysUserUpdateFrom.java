package com.wisdom.acm.sys.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class SysUserUpdateFrom extends BaseForm {
    /**
     * 用户id
     */
    @NotNull(message = "id不能为空")
    private Integer id;

    @NotBlank(message = "用户名不能为空")
    @LogParam(title = "用户名")
    private String userName;

    /**
     * 用户名称
     */
    @NotBlank(message = "用户名称不能为空")
    @LogParam(title = "姓名")
    private String actuName;

    /**
     * 用户性别
     */
    @Min( value = 0,message = "用户性别不能为空")
    @LogParam(title = "性别")
    private Integer sex;

    /**
     * 手机号
     */
    @LogParam(title = "手机号码")
    private String phone;

    @LogParam(title = "邮箱")
    private String email;

    /**
     * 证件类型
     */
    @LogParam(title = "证件类型")
    private String cardType;

    /**
     * 证件号码
     */
    @LogParam(title = "证件号码")
    private String cardNum;

    /**
     * 出生日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @LogParam(title = "出生日期")
    private Date birth;

    /**
     * 入职日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @LogParam(title = "入职日期")
    private Date entryDate;

    /**
     * 离职日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @LogParam(title = "离职日期")
    private Date quitDate;

    @LogParam(title = "用户密级")
    private String level;

    @LogParam(title = "排列顺序")
    private Integer sort;

    @NotNull(message = "人员状态不能为空")
    @LogParam(title = "用户状态")
    private Integer staffStatus;

    private List<Integer> roles;

    @NotNull(message = "所属部门不能为空")
    @LogParam(title = "所属部门")
    private Integer orgId;

    private String userType;

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
}
