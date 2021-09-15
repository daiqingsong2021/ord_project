package com.wisdom.acm.szxm.form.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class HolidayUpdateForm
{
    @NotNull(message = "主键ID不能为空")
    private Integer id;

    private Integer projectId;

    @NotNull(message = "人员ID不能为空")
    private Integer peopleId;

    /**
     * 请假期间负责工作人员ID
     */
    //@NotNull(message = "请假期间负责工作人员ID不能为空")
    private Integer agentId;

    @NotBlank(message = "人员姓名不能为空")
    private String peopleName;

    private String orgName;

    // @NotBlank(message = "请假类别不能为空")
    private String type;

    @NotNull(message = "开始时间不能为空")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date startTime;

    @NotNull(message = "结束时间不能为空")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date endTime;

    @NotNull(message = "天数不能为空")
    private Integer days;

    private String reason;

    private String ryZw;

    private String status;
    //请假人员类型，lwry:劳务人员
    private String peopleType;
}
