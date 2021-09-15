package com.wisdom.acm.base.form.custom;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseCustomFieldUpdateForm extends BaseForm {

    @NotNull(message = "唯一标识不能为空")
    private Integer id;

    @NotNull(message = "表名不能为空")
    @LogParam(title = "表名")
    private String tableName;

    @LogParam(title = "自定义01")
    private String custom01;

    @LogParam(title = "自定义02")
    private String custom02;

    @LogParam(title = "自定义03")
    private String custom03;

    @LogParam(title = "自定义04")
    private String custom04;

    @LogParam(title = "自定义05")
    private String custom05;

    @LogParam(title = "自定义06")
    private String custom06;

    @LogParam(title = "自定义07")
    private String custom07;

    @LogParam(title = "自定义08")
    private String custom08;

    @LogParam(title = "自定义09")
    private String custom09;

    @LogParam(title = "自定义10")
    private String custom10;

    public String getLogContent(){
        return "";
    }

}
