package com.wisdom.acm.dc3.config;

import com.wisdom.base.common.aspect.BaseMybatisInterceptor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.sql.Connection;

//拦截StatementHandler类中参数类型为Statement的 prepare 方法
//即拦截 Statement prepare(Connection var1, Integer var2) 方法
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),@Signature(type= Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
@Component
public class MybatisInterceptor extends BaseMybatisInterceptor {

}
