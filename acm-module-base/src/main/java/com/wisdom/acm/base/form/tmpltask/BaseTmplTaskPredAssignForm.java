package com.wisdom.acm.base.form.tmpltask;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseTmplTaskPredAssignForm {

    private Integer taskId;

    private Integer predTaskId;

    private String relationType;

    private Double lagQty;

}
