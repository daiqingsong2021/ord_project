package com.wisdom.acm.sys.form;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SysMenuUpdateForm extends BaseForm {

    @NotNull(message = "id不能为空")
    private Integer id;

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

    @NotBlank(message = "菜单地址不能为空")
    @LogParam(title = "菜单地址")
    private String url;

    @NotBlank(message = "图标不能为空")
    @LogParam(title = "图标")
    private String image;

    @NotBlank(message = "国际化不能为空")
    @LogParam(title = "国际化")
    private String i18n;

    @LogParam(title = "隐藏")
    private Integer hidden;

    @NotNull(message = "share不能为空")
    @LogParam(title = "共享")
    private Integer share;

    @NotNull(message = "isMenu不能为空")
    @LogParam(title = "是否是菜单")
    private Integer isMenu;

    @LogParam(title = "排序号")
    private Integer sort;

    @LogParam(title = "菜单明细")
    private String menuDesc;

    @NotNull(message = "active不能为空")
    @LogParam(title = "是否激活")
    private Integer active;

    @NotNull(message = "菜单类型不能为空")
    @LogParam(title = "菜单类型")
    private Integer menuType;

    private String groupName;

    public String getLogContent(){
        return "";
    }

}
