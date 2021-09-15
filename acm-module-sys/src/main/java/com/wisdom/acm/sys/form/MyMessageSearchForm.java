package com.wisdom.acm.sys.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class MyMessageSearchForm {

    //项目名称
    private String title;

    //接收时间
    //开始
    private String startTime;
    //结束
    private String endTime;
}
