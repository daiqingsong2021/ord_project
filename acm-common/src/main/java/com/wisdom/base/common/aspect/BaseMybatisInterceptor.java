package com.wisdom.base.common.aspect;

import com.wisdom.base.common.util.SortUtil;
import com.wisdom.base.common.util.sql.*;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BaseMybatisInterceptor implements Interceptor {


    @Value(value = "${mybatis.mapper-locations}")
    private String mybatisMapperLocations;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        //通过MetaObject优雅访问对象的属性，这里是访问statementHandler的属性;：MetaObject是Mybatis提供的一个用于方便、
        //优雅访问对象属性的对象，通过它可以简化代码、不需要try/catch各种reflect异常，同时它支持对JavaBean、Collection、Map三种类型对象的操作。
        MetaObject metaObject = MetaObject
                .forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                        new DefaultReflectorFactory());
        //先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler，然后就到BaseStatementHandler的成员变量mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        //sql语句类型 select、delete、insert、update
        String sqlCommandType = mappedStatement.getSqlCommandType().toString();

        Method m =invocation.getMethod();

        Class<?> clas = invocation.getTarget().getClass();

        BoundSql boundSql = statementHandler.getBoundSql();
        //获取到原始sql语句
        String sql = boundSql.getSql();
        String newSql = sql;
        if("select".equalsIgnoreCase(sqlCommandType)){
            // newSql = this.buildSql(sql);
            newSql = this.buildSql(sql);
        }else if("update".equalsIgnoreCase(sqlCommandType)){
            newSql = this.buildSql(sql);
        }

        //通过反射修改sql语句
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, newSql);
        return invocation.proceed();
    }

    public String buildSql (String sql ){

        if(!ObjectUtils.isEmpty(mybatisMapperLocations)
                && mybatisMapperLocations.indexOf("Oracle") > -1 ){
            return build(new StringBuffer(sql));
        }else{
            return sql;
        }
    }

    public String build(StringBuffer sql){
        // now() 转化为 sysdate
        BuildSqlVo buildSqlVo = new SingleBuildSqlVo("now()","sysdate");
        // if() 转化为 ifnull
        BuildSqlVo ifnullBuildSqlVo = new SingleBuildSqlVo("ifnull(","nvl(");
        // concat 转化为  ||
        BuildSqlVo extBuildSqlVo = new ConcatBuildSqlVo();
        // if 转化为 when case
        BuildSqlVo ifBuildSqlVo = new IfBuildSqlVo();
        // str_to_date 转化为 to_date
        BuildSqlVo toDateBuildSqlVo = new ToDateBuildSqlVo();
        // date_format 转化为 to_char
        BuildSqlVo dateToStrBuildSqlVo = new DateToStrBuildSqlVo();

        return this.building(sql,buildSqlVo,ifnullBuildSqlVo,extBuildSqlVo,ifBuildSqlVo,toDateBuildSqlVo,dateToStrBuildSqlVo);
    }

    private String building(StringBuffer sql, BuildSqlVo... buildSqlVoArr) {

        List<UpdateSqlVo> updateSqlVoList = new ArrayList<>();
        char[] sqlarr =  sql.toString().toCharArray();

        for( int i = 0; i < sqlarr.length; i ++){
            char c = sqlarr[i];
            for(BuildSqlVo sqlvo : buildSqlVoArr){
                sqlvo.build(c,i);
            }
        }

        for(BuildSqlVo sqlvo : buildSqlVoArr){
            updateSqlVoList.addAll(sqlvo.getUpdateSqlVoList());
        }

        updateSqlVoList = SortUtil.sortObj(updateSqlVoList,"start","desc");
        if(!ObjectUtils.isEmpty(updateSqlVoList)){
            for( UpdateSqlVo upsqlVo : updateSqlVoList){
                sql = sql.replace(upsqlVo.getStart(),upsqlVo.getEnd()+1, upsqlVo.getContent());
            }
        }

        return sql.toString();
    }

    public static void main(String[] args ){
        String sql = " select str_to_date('2014-04-22 12:00:12','%Y-%m-%d')   date_format(date,'%Y-%m-%d %H:%i:%s') str_to_date('2019-01-01','%Y-%m-%d')  ifnull    (  aaaa,0000  )  if((aaaa,bbb,(bbbbb,eee)),aa,bbb) ,,,   concat('%',#{key},'%') aa concat('%',#{key},'%')  if (b=   33333  ,'aaaa',aaaaabbb)aaaa  '%'||#{key}||'%' , now() ";
        /*String sql = "select r.id, p.name as proj_name, tt.task_name as wbs_name, t.task_name, r.plan_start_time, r.plan_end_time, r.rsrc_type, u.unit, u.maxunit, r.budget_qty, r.act_start_time, r.act_end_time, r.act_qty\n" +
                "   from wsd_plan_taskrsrc r\n" +
                "   left join wsd_plan_task t on r.task_id = t.id\n" +
                "   left join wsd_plan_task tt on t.parent_id = tt.id\n" +
                "   left join wsd_plan_project p on r.project_id = p.id\n" +
                "   left join wsd_rsrc_user u on r.rsrc_id = u.id\n" +
                "   where r.rsrc_id = ?\n" +
                "       and r.rsrc_type = ?\n" +
                "       and str_to_date(?,'%Y-%m-%d %H:%i:%s') <= r.plan_end_time\n" +
                "       and str_to_date(?,'%Y-%m-%d gg:aa:ee') >= r.plan_start_time ";
        */
        // String sql = "  ifnull    (  aaaa,0000  ) ";
        System.out.println(sql);
        String s = new BaseMybatisInterceptor().build(new StringBuffer(sql));
        System.out.println(s);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        //此处可以接收到配置文件的property参数
        System.out.println(properties.getProperty("name"));
    }

}
