package com.wisdom.base.common.form;

import lombok.Data;

@Data
public class PlanCprtmAssignAddForm {
    //业务ID
    private Integer bizId;

    //业务类型(project,plan,task)
    private String bizType;

    //团队类型(org,user)
    private String cprtmType;

    //团队id
    private Integer cprtmId;
}
