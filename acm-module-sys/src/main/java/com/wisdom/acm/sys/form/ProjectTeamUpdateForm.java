package com.wisdom.acm.sys.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import com.wisdom.base.common.form.ExtendedColumnForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ProjectTeamUpdateForm extends ExtendedColumnForm {

    @NotNull
    private Integer id;

    @NotBlank
    @LogParam(title = "代码")
    private String teamCode;

    @NotBlank
    @LogParam(title = "名称")
    private String teamName;

    private String sectionStatus;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date startDate;


    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date endDate;

    private String openPgd;


    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date pgdStartDate;


    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date pgdEndDate;

    private String openExam;


    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date examStartDate;


    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date examEndDate;

    @NotBlank(message = "组织编码不能为空")
    private String orgCode;

    @NotBlank(message = "组织名称不能为空")
    private String orgName;

    private int orgId;
}
