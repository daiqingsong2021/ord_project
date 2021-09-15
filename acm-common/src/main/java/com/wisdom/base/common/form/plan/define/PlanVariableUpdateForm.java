package com.wisdom.base.common.form.plan.define;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.enums.ParamEnum;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

@Data
public class PlanVariableUpdateForm extends BaseForm {
    /**
     * 计划定义id
     */
    private Integer id;

    /**
     *  工期类型
     */
    @LogParam(title = "工期类型" , type = ParamEnum.DICT, dictType = "plan.project.taskdrtntype")
    private String taskDrtnType;

    /**
     *  任务完成%类型
     */
    @LogParam(title = "任务完成%类型" , type = ParamEnum.DICT, dictType = "plan.define.taskfinishtype")
    private String taskFinpctType;

    /**
     *  关键路径
     */
    @LogParam(title = "关键路径类型" , type = ParamEnum.DICT, dictType = "plan.project.cpmtype")
    private String cpmType;

    /**
     *  启动内部共享
     */
    @LogParam(title = "启动内部共享" , type = ParamEnum.OTHER)
    private Integer shareWbs;

    /**
     *  总浮时
     */
    @LogParam(title = "总浮时")
    private Double cpmFloat;

}
