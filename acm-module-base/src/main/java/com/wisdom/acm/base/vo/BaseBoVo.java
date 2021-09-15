package com.wisdom.acm.base.vo;

import lombok.Data;

@Data
public class BaseBoVo {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 业务对象代码
     */
    private String boCode;

    /**
     * 业务对象名称
     */
    private String boName;

}
