package com.wisdom.base.common.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

//import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 实体对象的父类，默认含有创建、更新等通用信息 此对象继承序列化对象
 * @author wang
 */
@MappedSuperclass
@Data
public class BasePo implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    protected Integer id;

    /**
     * 排序编号
     */
    @Column(name = "sort_num")
    protected Integer sort;

    /**
     * 最后更新时间
     */
    @Column(name = "last_upd_time")
    protected Date lastUpdTime;

    /**
     * 最后更新人
     */
    @Column(name = "last_upd_user")
    protected Integer lastUpdUser;

    /**
     * 创建日期
     */
    @Column(name = "creat_time")
    protected Date creatTime;

    /**
     * 创建人
     */
    @Column(name = "creator")
    protected Integer creator;

    /**
     * 更新IP
     */
    @Column(name = "last_upd_ip")
    protected String lastUpdIp;

    /**
     * 版本号
     */
    @Column(name = "wsdver")
    protected Long wsdver = 0l;

}
