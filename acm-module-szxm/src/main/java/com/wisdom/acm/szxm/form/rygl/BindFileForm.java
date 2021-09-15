package com.wisdom.acm.szxm.form.rygl;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BindFileForm
{

    @NotNull(message = "bizId不能为空")
    private Integer bizId;

    @NotBlank(message = "bizType不能为空")
    private String bizType;

    /**
     * 仓库地址
     */
    private List<Integer> fileIds;
}
