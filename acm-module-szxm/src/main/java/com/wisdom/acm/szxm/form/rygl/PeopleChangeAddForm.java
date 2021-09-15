package com.wisdom.acm.szxm.form.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class PeopleChangeAddForm
{
    @NotNull(message = "项目ID不能为空")
    private Integer projectId;

    @NotNull(message = "标段ID不能为空")
    private Integer sectionId;

    @NotNull(message = "被变更人员ID不能为空")
    private Integer bchangerId;

    @NotBlank(message = "被变更人员不能为空")
    private String bchanger;

    @NotNull(message = "变更后人员ID不能为空")
    private Integer achangerId;

    @NotBlank(message = "变更后人员不能为空")
    private String achanger;

    @NotBlank(message = "变更岗位不能为空")
    private String changeGw;

    @NotBlank(message = "变更单位不能为空")
    private String orgName;

    @NotNull(message = "变更单位ID不能为空")
    private String projInfoId;

    private String contractNumber;

    private String changeReason;

    /**
     * 变更日期
     */
    @NotNull(message = "变更日期不能为空")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date changeTime;
}
