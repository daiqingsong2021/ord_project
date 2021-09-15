package com.wisdom.base.common.vo.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseCustomVo implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;

    /**
     * 自定义01
     */
    private CustomValueVo custom01;

    /**
     * 自定义02
     */
    private CustomValueVo custom02;

    /**
     * 自定义03
     */
    private CustomValueVo custom03;

    /**
     * 自定义04
     */
    private CustomValueVo custom04;

    /**
     * 自定义05
     */
    private CustomValueVo custom05;

    /**
     * 自定义06
     */
    private CustomValueVo custom06;

    /**
     * 自定义07
     */
    private CustomValueVo custom07;

    /**
     * 自定义08
     */
    private CustomValueVo custom08;

    /**
     * 自定义09
     */
    private CustomValueVo custom09;

    /**
     * 自定义10
     */
    private CustomValueVo custom10;

}