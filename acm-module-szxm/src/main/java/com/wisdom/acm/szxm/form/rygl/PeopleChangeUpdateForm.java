package com.wisdom.acm.szxm.form.rygl;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PeopleChangeUpdateForm
{
    @NotNull(message = "主键ID不能为空")
    private Integer id;

    private Integer bchangerId;

    private String bchanger;

    private Integer achangerId;

    private String achanger;

    private String changeGw;

    private String orgName;

    private String contractNumber;

    private String changeReason;

}
