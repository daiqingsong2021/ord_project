package com.wisdom.acm.processing.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zll
 * 2020/8/20/020 19:51
 * Description:<restTemplate获取get请求返回数据>
 */
@ApiModel
public class CommonResult<T> {
    @ApiModelProperty(value = "状态码")
    private Integer code;
    @ApiModelProperty(value = "信息")
    private String msg;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
