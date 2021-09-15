package com.wisdom.acm.szxm.form.rygl;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AllKqConfigForm
{

    private Integer Id;

    @NotNull(message = "项目ID不能为空")
    private Integer projectId;

    @NotNull(message = "考勤日历ID不能为空")
    private Integer calenderId;

    @NotBlank(message = "考勤类型不能为空")
    private String type;

    @NotBlank(message = "管理人员是否考勤不能为空")
    private String mangerkq;

    @NotBlank(message = "劳务人员是否考勤不能为空")
    private String workerkq;

}
