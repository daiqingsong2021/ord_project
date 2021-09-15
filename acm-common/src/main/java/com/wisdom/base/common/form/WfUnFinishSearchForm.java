package com.wisdom.base.common.form;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "查询条件表单")
@Data
public class WfUnFinishSearchForm {

    @ApiParam(value = "开始时间", required = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date startTime;

    @ApiParam(value = "结束时间", required = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date endTime;

    @ApiParam(value = "流程类型", required = false)
    private String bizType;

    @ApiParam(value = "流程名称", required = false)
    private String keyword;
}
