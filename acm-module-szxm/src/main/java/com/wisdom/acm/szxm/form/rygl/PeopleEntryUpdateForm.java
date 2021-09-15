package com.wisdom.acm.szxm.form.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class PeopleEntryUpdateForm
{
    @NotNull(message = "主键不能为空")
    private Integer id;

    @NotNull(message = "进/退场人数不能为空")
    private Integer peoNums;

    @NotNull(message = "进/退场时间不能为空")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date entryTime;

    @NotBlank(message = "人员类型不能为空")
    private String peoEntryType;

    @NotBlank(message = "进退场类别不能为空")
    private String type;
}
