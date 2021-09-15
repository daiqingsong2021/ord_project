package com.wisdom.acm.sys.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SysOrgAddForm  extends BaseForm {
    /**
     * 代码
     */
    @NotBlank(message = "组织代码不能为空")
    private String orgCode;

    /**
     * 名称
     */
    @NotBlank(message = "组织名称不能为空")
    private String orgName;


    /**
     * 父ID
     */
    @NotNull(message = "上层机构不能为空")
    private Integer parentId;


    /**
     * 组织类型
     */
    @NotNull(message = "机构类型不能为空")
    private Integer orgType;

    /**
     * OBS级别
     */
    @NotNull(message = "机构级别不能为空")
    private String orgLevel;

    /**
     * 备注
     */
    private String remark;

    /**
     * 组织状态
     */
    private Integer status;

    /**
     * 所属地域
     */
    private String grogLoc;

    private Integer sort;

    /**
     * 组织地址
     */
    private String orgAddress;

    /**
     * 邮编
     */
    private String zipCode;

    /**
     * 联系人
     */
    private String orgContract;

    /**
     * 联系电话
     */
    private String contractNum;

    /**
     * 电子邮箱
     */
    private String orgEmail;

    /**
     * 网站地址
     */
    private String webAddress;

    /**
     * 生效日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date effectDate;

    /**
     * 失效日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date invalidDate;

    public String getLogContent(){
        return "增加组织，组织名称："+ this.getOrgName();
    }
}
