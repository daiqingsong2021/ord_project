package com.wisdom.base.common.po;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "wsd_logger_api")
@Data
public class ApiLogTotal {

    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;

    /**
     * 类/接口
     */
    @Column(name = "tragetClassName")
    private String tragetClassName ;

    /**
     * 方法
     */
    @Column(name = "MethodName")
    private String MethodName ;

    /**
     * 参数个数
     */
    @Column(name = "argsSize")
    private Integer argsSize ;

    /**
     * 返回类型
     */
    @Column(name = "returnType")
    private String returnType ;

    /**
     * 参数类型
     */
    @Column(name = "argsTypes")
    private String argsTypes ;

    /**
     * 耗时
     */
    @Column(name = "totalTime")
    private Long totalTime ;

    /**
     * 创建时间
     */
    @Column(name = "creatTime")
    private Date creatTime ;
}
