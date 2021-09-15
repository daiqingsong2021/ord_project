package com.wisdom.acm.base.form.set;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.enums.ParamEnum;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

@Data
public class BaseSetProjectUpdateForm extends BaseForm {
    // 工期类型
    @LogParam(title = "工期类型",type = ParamEnum.DICT, dictType = "plan.project.taskdrtntype")
    private String taskDrtnType;
    // 关键路径
    @LogParam(title = "关键路径",type = ParamEnum.DICT, dictType = "plan.project.cpmtype")
    private String cpmType;
    // 总浮时
    @LogParam(title = "总浮时")
    private Double cpmFloat;
    // 启用项目团队
    @LogParam(title = "启用项目管理团队",type = ParamEnum.OTHER)
    private Integer enableProjectTeam;
    // WBS/任务内部共享
    @LogParam(title = "启用WBS内部共享",type = ParamEnum.OTHER)
    private Integer shareWbs;
    // 消息推送
    @LogParam(title = "启用消息推送",type = ParamEnum.OTHER)
    private Integer message;
}
