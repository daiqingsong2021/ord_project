package com.wisdom.acm.szxm.form.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class SpecialWorkerUpdateForm
{
    @NotNull(message = "主键ID不能为空")
    private Integer id;

    private Integer peopleId;

    @NotBlank(message = "工种类型不能为空")
    private String workType;
}
