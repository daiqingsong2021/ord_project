package com.wisdom.acm.szxm.form.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SpecialWorkerAddForm
{
    @NotNull(message = "项目ID不能为空")
    private Integer projectId;

    @NotNull(message = "标段ID不能为空")
    private Integer sectionId;

    @NotNull(message = "人员ID不能为空")
    private Integer peopleId;

    @NotBlank(message = "工种类型不能为空")
    private String workType;
}
