package com.wisdom.base.common.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户信息")
@Data
public class LoginUserVo {

    private Integer id;

    private String userName;

    private String actuName;

    private String userType;

    private Integer secutyLevel;

    private Boolean useTmm;
}
