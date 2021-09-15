package com.wisdom.acm.szxm.form.wtgl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class ComuQueHandleUpdateForm extends BaseForm {

    //id
    @ApiModelProperty(value = "问题id", required = true)
    private Integer id;

    //处理时间
    @ApiModelProperty(value = "处理时间", required = true)
    @NotNull(message = "处理时间不能为空")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @LogParam(title = "处理时间")
    private Date handleTime;

    //处理人
    @ApiModelProperty(value = "处理人", required = true)
    @LogParam(title = "处理人")
    private String handleUser;

    //处理结果
    @ApiModelProperty(value = "处理结果", required = true)
    @NotBlank(message = "处理结果不能为空")
    @LogParam(title = "处理结果")
    private String handleResult;

    //附件
    @ApiModelProperty(value = "附件", required = true)
    private List<Integer> fileIds;
}
