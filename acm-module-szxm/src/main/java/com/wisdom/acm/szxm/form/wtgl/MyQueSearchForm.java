package com.wisdom.acm.szxm.form.wtgl;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MyQueSearchForm {

    //项目名称
    @ApiModelProperty(value = "项目名称", required = true)
    private String project;

    //创建日期
    @ApiModelProperty(value = "创建日期", required = true)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creatTime;

    //解决日期
    @ApiModelProperty(value = "解决日期", required = true)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date endTime;
}
