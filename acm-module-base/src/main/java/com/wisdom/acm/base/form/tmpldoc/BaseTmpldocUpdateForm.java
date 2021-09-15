package com.wisdom.acm.base.form.tmpldoc;

import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.enums.ParamEnum;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseTmpldocUpdateForm extends BaseForm {

    @NotNull(message = "文档模板主键不能为空")
    private String id;

    @NotNull(message = "标题不能为空")
    @LogParam(title = "标题")
    private String docTitle;

    @NotNull(message = "编号不能为空")
    @LogParam(title = "编号")
    private String docNum;

    @NotNull(message = "版本号不能为空")
    @LogParam(title = "版本号")
    private String docVersion;

    @LogParam(title = "所属业务对象")
    private String docObject;

    @LogParam(title = "文件名称",type = ParamEnum.OTHER)
    private Integer fileId;

    //文档专业
    @LogParam(title = "文档专业",type = ParamEnum.OTHER)
    private String profession;

    //密级
    @LogParam(title = "密级",type = ParamEnum.DICT, dictType = "comm.secutylevel")
    private String secutyLevel;

    //作者
    @LogParam(title = "文档作者")
    private String author;

    //文档类别
    @LogParam(title = "文档类别")
    private String docClassify;

}