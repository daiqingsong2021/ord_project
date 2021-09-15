package com.wisdom.acm.szxm.form.rygl;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WarnHouseUpdateForm
{

    @NotNull(message = "主键ID不能为空")
    private Integer id;

    private String name;

    /**
     * 仓库地址
     */
    private String address;
}
