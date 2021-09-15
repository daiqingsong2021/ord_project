package com.wisdom.base.common.form.doc;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

@Data
public class DocProjectFolderUpdateForm extends BaseForm {

    private Integer id;

    /**
     * 名称
     */
    @LogParam(title = "文件夹名称")
    private String name;

    /**
     * 菜单id
     */
    private Integer menuId;
}
