package com.wisdom.acm.base.vo.set;

import lombok.Data;

@Data
public class BaseSetBoVo {

    /**
     * 业务编码
     */
    private Integer id;

    /**
     * 键
     */
    private String code;

    /**
     * 值
     */
    private String name;
}
