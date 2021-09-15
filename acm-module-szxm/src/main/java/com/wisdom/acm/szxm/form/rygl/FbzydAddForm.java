package com.wisdom.acm.szxm.form.rygl;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FbzydAddForm
{
    @NotNull(message = "项目基础信息ID不能为空")
    private Integer projInfoId;

    private Integer projectId;

    private Integer sectionId;

    @NotBlank(message = "分包作业队名称不能为空")
    private String name;

    /**
     * 分包作业队地址
     */
    private String address;

    private Integer residentNum;

    private String houseCharacter;

    private String houseArea;

}
