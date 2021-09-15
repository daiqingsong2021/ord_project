//package com.wisdom.acm.sys.po;
//
//import com.wisdom.base.common.util.UUIdGenId;
//import lombok.Data;
//import tk.mybatis.mapper.annotation.KeySql;
//
//import javax.persistence.*;
//import java.util.Date;
//
//@Table(name = "wsd_sys_logger_api")
//@Data
//public class ApiLogTotal {
//
//    @Column(name = "ID")
//    @KeySql(genId = UUIdGenId.class)
//    private Long id;
//
//    /**
//     * 类/接口
//     */
//    @Column(name = "tragetClassName")
//    private String tragetClassName ;
//
//    /**
//     * 方法
//     */
//    @Column(name = "MethodName")
//    private String MethodName ;
//
//    /**
//     * 参数个数
//     */
//    @Column(name = "argsSize")
//    private Integer argsSize ;
//
//    /**
//     * 返回类型
//     */
//    @Column(name = "returnType")
//    private String returnType ;
//
//    /**
//     * 参数类型
//     */
//    @Column(name = "argsTypes")
//    private String argsTypes ;
//
//    /**
//     * 耗时
//     */
//    @Column(name = "totalTime")
//    private Long totalTime ;
//
//    /**
//     * 创建时间
//     */
//    @Column(name = "creatTime")
//    private Date creatTime ;
//}
