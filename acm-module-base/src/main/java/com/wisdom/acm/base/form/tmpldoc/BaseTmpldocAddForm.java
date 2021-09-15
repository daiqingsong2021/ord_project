package com.wisdom.acm.base.form.tmpldoc;

import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
public class BaseTmpldocAddForm extends BaseForm {

    @NotNull(message = "标题不能为空")
    private String docTitle;

    @NotNull(message = "编号不能为空")
    private String docNum;

    @NotNull(message = "版本号不能为空")
    private String docVersion;

    private String docObject;

    private Integer fileId;

    //文档专业
    private String profession;

    //密级
    private String secutyLevel;

    //作者
    private String author;

    //文档类别
    private String docClassify;

    public String getLogContent(){
        return "增加文档，文档标题："+ this.getDocTitle();
    }

}