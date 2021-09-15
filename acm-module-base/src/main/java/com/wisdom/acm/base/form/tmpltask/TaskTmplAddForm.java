package com.wisdom.acm.base.form.tmpltask;

import com.wisdom.base.common.vo.plan.task.PlanTaskTreeVo;
import com.wisdom.base.common.vo.plan.task.PlanTmplTaskDelvForm;
import com.wisdom.base.common.vo.plan.task.pred.PlanTmplTaskPredForm;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TaskTmplAddForm {
    /**
     * 模版名称
     */
    @NotNull(message = "名称不能为空")
    private String tmplName;

    /**
     * 是否全局
     */
    @NotNull(message = "是否全局不能为空")
    private Integer isGlobal;

    /**
     * 计划定义id
     */
    private Integer defineId;
    /**
     * 任务数据
     */
    private List<PlanTaskTreeVo> taskLists;

    /**
     * 逻辑关系
     */
    private List<PlanTmplTaskPredForm> taskPredLists;

    /**
     * 交付物
     */
    private List<PlanTmplTaskDelvForm> taskDelvLists;

}
