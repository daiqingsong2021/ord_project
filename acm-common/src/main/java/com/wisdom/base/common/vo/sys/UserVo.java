package com.wisdom.base.common.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户信息")
@Data
public class UserVo {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "登录名")
    private String code;

    @ApiModelProperty(value = "姓名")
    private String name;

    public UserVo() {

    }

    public UserVo(Integer id) {
        this.id = id;
    }

    public UserVo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserVo(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    /**
     * 转字符串
     *
     * @return
     */
    public String toString() {
        return this.name + "/" + this.code;
    }
}
