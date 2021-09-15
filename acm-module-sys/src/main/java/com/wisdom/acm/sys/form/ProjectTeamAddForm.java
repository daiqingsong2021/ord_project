package com.wisdom.acm.sys.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.form.ExtendedColumnForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ProjectTeamAddForm extends ExtendedColumnForm  {

    @NotBlank(message = "代码不能为空")
    private String teamCode;

    @NotBlank(message = "名称不能为空")
    private String teamName;

    private int parentId;

    @NotBlank(message = "所属业务类型不能为空")
    private String bizType;

    @NotNull(message = "所属业务ID不能为空")
    private Integer bizId;


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
