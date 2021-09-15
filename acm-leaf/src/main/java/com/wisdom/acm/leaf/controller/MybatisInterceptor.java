package com.wisdom.acm.leaf.controller;

import com.wisdom.base.common.aspect.BaseMybatisInterceptor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.sql.Connection;


//拦截StatementHandler类中参数类型为Statement的 prepare 方法
//即拦截 Statement prepare(Connection var1, Integer var2) 方法
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Component
public class MybatisInterceptor extends BaseMybatisInterceptor {

}
