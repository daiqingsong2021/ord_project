package com.wisdom.acm.szxm.form.pfe;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改站点信息
 */
@Data
public class StationUpdateForm {

    private Integer projectId;

    @NotNull(message = "主键ID不能为空")
    private Integer id;

    @NotBlank(message = "名称不能为空")
    private String name;

    @NotBlank(message = "代码不能为空")
    private String code;

    @NotBlank(message = "站点类型不能为空")
    private String stationType;

    //备注
    private String remark;
}
