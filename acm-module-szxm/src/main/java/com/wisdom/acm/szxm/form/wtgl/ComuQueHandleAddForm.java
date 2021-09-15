package com.wisdom.acm.szxm.form.wtgl;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
public class ComuQueHandleAddForm {

    //所属问题
    @ApiModelProperty(value = "问题id", required = true)
    private Integer questionId;
    @ApiModelProperty(value = "项目id", required = true)
    private Integer projectId;

    //处理时间
    @ApiModelProperty(value = "处理时间", required = true)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date handleTime;

    //处理人
    @ApiModelProperty(value = "处理人", required = true)
    private String handleUser;

    //处理结果说明
    @ApiModelProperty(value = "处理结果说明", required = true)
    @NotBlank(message = "处理结果说明不能为空")
    private String handleResult;

    //附件
    @ApiModelProperty(value = "附件", required = true)
    private List<Integer> fileIds;

}
