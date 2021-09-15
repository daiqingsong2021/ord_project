package com.wisdom.acm.szxm.form.pfe;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增站点
 */
@Data
public class StationAddForm {

    @NotNull(message = "项目ID不能为空")
    private Integer projectId;

    @NotBlank(message = "名称不能为空")
    private String name;

    @NotBlank(message = "代码不能为空")
    private String code;

    @NotBlank(message = "类型不能为空")
    private String stationType;

    //备注
    private String remark;
}
