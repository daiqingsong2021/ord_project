package com.wisdom.acm.szxm.form.rygl;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class SpecialWorkCertUpdateForm
{
    @NotNull(message = "主键不能为空")
    private Integer id;

    @NotNull(message = "证书有效期不能为空")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date certExpirationTime;

    @NotNull(message = "证书首次发放日期不能为空")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date certFirstPublishTime;

    @NotBlank(message = "证书编号不能为空")
    private String certNum;

    @NotNull(message = "证书管理ID不能为空")
    private Integer certGlId;

    @NotNull(message = "特殊工种ID不能为空")
    private Integer specialWorkerId;

    /**
     * 文件Ids
     */
    private List<Integer> fileIds;
}
