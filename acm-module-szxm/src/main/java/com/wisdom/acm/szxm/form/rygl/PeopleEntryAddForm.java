package com.wisdom.acm.szxm.form.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class PeopleEntryAddForm
{
    @NotNull(message = "项目ID不能为空")
    private Integer projectId;

    @NotNull(message = "标段ID不能为空")
    private Integer sectionId;

    @NotNull(message = "单位ID不能为空")
    private Integer projInfoId;

    @NotBlank(message = "单位名称不能为空")
    private String orgName;

    @NotNull(message = "进/退场时间不能为空")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date entryTime;

    @NotBlank(message = "人员类型不能为空")
    private String peoEntryType;

    @NotBlank(message = "进退场类别不能为空")
    private String type;
}
