package com.wisdom.acm.sys.form;

import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SysMenuAddForm extends BaseForm {

    @NotNull(message = "父id不能为空")
    private Integer parentId;

    @NotBlank(message = "菜单代码不能为空")
    @LogParam(title = "菜单代码")
    private String menuCode;

    @NotBlank(message = "菜单名称不能为空")
    @LogParam(title = "菜单名称")
    private String menuName;

    @NotBlank(message = "菜单简码不能为空")
    @LogParam(title = "菜单简码")
    private String shortCode;

    @NotBlank(message = "地址不能为空")
    @LogParam(title = "地址")
    private String url;

    @NotBlank(message = "图标不能为空")
    @LogParam(title = "图标")
    private String image;

    @NotNull(message = "不能为空")
    private Integer hidden;

    @NotNull(message = "不能为空")
    private Integer share;

    @NotNull(message = "不能为空")
    private Integer isMenu;

    private Integer sort;

    @NotBlank(message = "国际化不能为空")
    private String i18n;

    private String menuDesc;

    @NotNull(message = "不能为空")
    private Integer active;

    private Integer menuType;

    private String groupName;
}
