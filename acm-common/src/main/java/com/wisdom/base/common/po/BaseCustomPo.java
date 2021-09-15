package com.wisdom.base.common.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 实体对象的父类，默认含有创建、更新等通用信息 此对象继承序列化对象
 */
@MappedSuperclass
@Data
public class BaseCustomPo extends BasePo {

    /**
     * 自定义01
     */
    @Column(name = "custom_01")
    protected String custom01;

    /**
     * 自定义02
     */
    @Column(name = "custom_02")
    protected String custom02;

    /**
     * 自定义03
     */
    @Column(name = "custom_03")
    protected String custom03;

    /**
     * 自定义04
     */
    @Column(name = "custom_04")
    protected String custom04;

    /**
     * 自定义05
     */
    @Column(name = "custom_05")
    protected String custom05;

    /**
     * 自定义06
     */
    @Column(name = "custom_06")
    protected String custom06;

    /**
     * 自定义07
     */
    @Column(name = "custom_07")
    protected String custom07;

    /**
     * 自定义08
     */
    @Column(name = "custom_08")
    protected String custom08;

    /**
     * 自定义09
     */
    @Column(name = "custom_09")
    protected String custom09;

    /**
     * 自定义10
     */
    @Column(name = "custom_10")
    protected String custom10;

}
