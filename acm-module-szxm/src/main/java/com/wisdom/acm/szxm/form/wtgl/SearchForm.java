package com.wisdom.acm.szxm.form.wtgl;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchForm {
    @ApiModelProperty(value = "名称", required = true)
    //名称
    private String title;
}
