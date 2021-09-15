package com.wisdom.acm.dc2.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class DailyRepresentationUpdateForm extends BaseForm
{

    @NotEmpty(message = "补充情况说明ID")
    private String id;
    /**
     * 记录日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @LogParam(title = "日期")
    private Date recordTime;
    /**
     *线路
     */
    @LogParam(title = "线路")
    private String line;

    /**
     *模块标题
     */
    private String moudleTitle;
    /**
     *说明
     */
    private String description;
    /**
     *说明
     */
    private String picturePath;

}
