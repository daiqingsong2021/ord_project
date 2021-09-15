package com.wisdom.acm.base.form.tmpltask;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.enums.ParamEnum;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseTmplTaskUpdateForm extends BaseForm {

    private Integer id;

    @NotNull(message = "代码不能为空")
    @LogParam(title = "代码")
    private String taskCode;

    @NotNull(message = "名称不能为空")
    @LogParam(title = "名称")
    private String taskName;

    @NotNull(message = "计划工期不能为空")
    @LogParam(title = "计划工期")
    private Double planDrtn;

    @NotNull(message = "计划工时不能为空")
    @LogParam(title = "计划工时")
    private Double planQty;

    /**
     * 计划类型
     */
    @LogParam(title = "计划类型")
    private String planType;

    /**
     * 计划级别
     */
    @LogParam(title = "计划级别")
    private String planLevel;

    /**
     * 是否WBS反馈
     */
    @LogParam(title = "WBS反馈",type = ParamEnum.OTHER)
    private Integer isFeedback;

    /**
     * 是否控制账户
     */
    @LogParam(title = "控制账户",type = ParamEnum.OTHER)
    private Integer controlAccount;

    /**
     * 作业类型
     */
    private String taskType;

    /**
     * 工期类型
     */
    private String drtnType;

    /**
     * 备注
     */
    @LogParam(title = "备注")
    private String remark;

    /**
     * 状态
     */
    private String status;
}
