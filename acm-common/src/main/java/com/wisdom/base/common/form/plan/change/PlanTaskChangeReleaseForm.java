package com.wisdom.base.common.form.plan.change;

import com.wisdom.base.common.form.IdTypeForm;
import lombok.Data;

import java.util.List;

/**
 * @Author: szc
 * @Date: 2019/3/26 13:59
 * @Version 1.0
 */
@Data
public class PlanTaskChangeReleaseForm {

    private Integer projectId;

    //变更id
    private List<IdTypeForm> tasks;

    //变更申请原因
    private String changeReason;

    //变更影响分析
    private String changeEffect;

    //采取措施说明
    private String changeWayDesc;
}
