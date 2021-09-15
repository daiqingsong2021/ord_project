package com.wisdom.acm.sys.form;

import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class SysAuthAddForm extends BaseForm {

    private Integer menuId;

    private String funcName;

    private String funcCode;

    private String shortCode;

    private Integer del;

    public String getLogContent(){return "增加权限配置，权限配置名称："+ this.getFuncName();}
}
