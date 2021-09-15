package com.wisdom.acm.sys.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SysOrgUpdateForm extends BaseForm {

    @NotNull( message = "id不能为空")
    private Integer id;
    /**
     * 代码
     */
    @NotBlank(message = "组织代码不能为空")
    @LogParam(title = "机构代码")
    private String orgCode;

    /**
     * 名称
     */
    @NotBlank(message = "组织名称不能为空")
    @LogParam(title = "机构名称")
    private String orgName;

    /**
     * 组织类型
     */
    @NotNull(message = "机构类型不能为空")
    @LogParam(title = "机构类型")
    private Integer orgType;

    /**
     * OBS级别
     */
    @NotNull(message = "机构级别不能为空")
    @LogParam(title = "机构等级")
    private String orgLevel;

    /**
     * 备注
     */
    @LogParam(title = "备注")
    private String remark;

    /**
     * 组织状态
     */
    @NotNull(message = "机构状态不能为空")
    @LogParam(title = "机构状态")
    private Integer status;

    /**
     * 所属地域
     */
    @LogParam(title = "所属地域")
    private String grogLoc;

    /**
     * 组织地址
     */
    @LogParam(title = "机构地址")
    private String orgAddress;

    /**
     * 邮编
     */
    @LogParam(title = "邮编")
    private String zipCode;

    private Integer sort;

    /**
     * 联系人
     */
    @LogParam(title = "联系人")
    private String orgContract;

    /**
     * 联系电话
     */
    @LogParam(title = "联系电话")
    private String contractNum;

    /**
     * 电子邮箱
     */
    @LogParam(title = "电子邮箱")
    private String orgEmail;

    /**
     * 网站地址
     */
    @LogParam(title = "网络地址")
    private String webAddress;

    /**
     * 生效日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @LogParam(title = "生效日期")
    private Date effectDate;

    /**
     * 失效日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @LogParam(title = "失效日期")
    private Date invalidDate;

    private Integer parentId;
}
