package com.wisdom.acm.base.form.tmpltask;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseTmplTaskAddForm extends BaseForm {

    @NotNull(message = "业务对象不能为空")
    private Integer tmplId;

    @NotNull(message = "父节点不能为空")
    private Integer parentId;

    @NotNull(message = "代码不能为空")
    private String taskCode;

    @NotNull(message = "名称不能为空")
    private String taskName;

    @NotNull(message = "计划工期不能为空")
    private Double planDrtn;

    @NotNull(message = "计划工时不能为空")
    private Double planQty;

    /**
     * 计划类型
     */
    private String planType;

    /**
     * 计划级别
     */
    private String planLevel;

    /**
     * 是否WBS反馈
     */
    private Integer isFeedback;

    /**
     * 是否控制账户
     */
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
    private String remark;

    /**
     * 状态
     */
    private String status;
}
