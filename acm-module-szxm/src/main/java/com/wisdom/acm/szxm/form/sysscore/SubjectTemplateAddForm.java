package com.wisdom.acm.szxm.form.sysscore;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Author：wqd
 * Date：2020-01-02 14:00
 * Description：<描述>
 */
@Data
public class SubjectTemplateAddForm {
    /**
     * 分组名称编码(菜单表编码)
     */
    @NotBlank(message = "分组编码不能为空")
    private String groupCode;

    /**
     * 分组名称
     */
    @NotBlank(message = "分组名称不能为空")
    private String groupTitle;

    /**
     * 业务模块编码（菜单表）
     */
    @NotBlank(message = "业务模块编码不能为空")
    private String moduleCode;

    /**
     * 业务模块名称
     */
    @NotBlank(message = "业务模块名称不能为空")
    private String moduleTitle;

    /**
     * 评分人员角色
     */
    @NotBlank(message = "评分人员角色不能为空")
    private String raters;
}
