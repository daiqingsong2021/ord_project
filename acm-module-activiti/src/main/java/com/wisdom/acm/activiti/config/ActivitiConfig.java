package com.wisdom.acm.activiti.config;

import java.sql.Connection;
import com.wisdom.acm.activiti.listener.*;
import com.wisdom.acm.activiti.util.ExprUtil;
import com.wisdom.acm.activiti.util.canvas.CustomProcessDiagramGeneratorI;
import com.wisdom.acm.activiti.util.canvas.WorkflowConstants;
import com.wisdom.acm.activiti.util.modeler.JsonpCallbackFilter;
import com.wisdom.base.common.util.calc.calendar.Tools;
import org.activiti.engine.*;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@Configuration
public class ActivitiConfig {

    @Autowired
    private CustomProcessDiagramGeneratorI customProcessDiagramGeneratorI;

    @Value("classpath:processes/*")
    private Resource[] activitiResources;

   /* @Value("${spring.datasource.maximum-pool-size}")
    private Integer maxActive;

    @Value("${spring.datasource.maxWait}")
    private Integer maxWait;

    @Value("${spring.datasource.minIdle}")
    private Integer minIdle;

    @Value("${spring.datasource.connection-test-query}")
    private String validationQuery;

    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private Integer timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.testWhileIdle}")
    private Boolean testWhileIdle;

    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;*/

    @Primary
    @Bean(name = "activitiDataSource")
    @Qualifier("activitiDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration(@Qualifier("activitiDataSource") DataSource dataSource,
                                                                       @Qualifier("transactionManager") PlatformTransactionManager transactionManager){
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(dataSource);
        config.setTransactionManager(transactionManager);

        String confUrl = this.getWfListenerConfigure(dataSource);

        /*config.setJdbcMaxActiveConnections(Tools.parseInt(this.maxActive)); //最大连接数
        config.setJdbcMaxWaitTime(Tools.parseInt(this.maxWait)); //最大等待毫秒数
        config.setJdbcMaxIdleConnections(Tools.parseInt(this.minIdle)); //最大空闲数，数据库连接的最大空闲时间。超过空闲时间，数据库连接将被标记为不可用，然后被释放。设为0表示无限制。
        config.setJdbcMaxCheckoutTime(Tools.parseInt(this.timeBetweenEvictionRunsMillis)); //连接检查时间
        config.setJdbcPingQuery(this.validationQuery);
        config.setJdbcPingEnabled(Tools.parseBoolean(this.testWhileIdle)); //连接无用后，是否主动连接
        config.setJdbcPingConnectionNotUsedFor(Tools.parseInt(this.minEvictableIdleTimeMillis)); //连接无用检查并连接间隔，单位毫秒*/

        //config.setDatabaseSchemaUpdate("false");
        //config.setDeploymentResources(activitiResources); //不需要自动部署
        //config.setCustomPostCommandInterceptors(Arrays.asList(new SelfProcessInterceptor()));

        Map<String, List<ActivitiEventListener>> listeners = new HashMap<>();
        //创建任务时监听
        List<ActivitiEventListener> eventListenerList = new ArrayList<>();
        eventListenerList.add(new TaskCreateListenerHandler(confUrl));
        listeners.put(ActivitiEventType.TASK_CREATED.name(), eventListenerList);
        if(!Tools.isEmpty(confUrl)) {
            //完成任务时监听
            eventListenerList = new ArrayList<>();
            eventListenerList.add(new TaskCompletedListenerHandler(confUrl));
            listeners.put(ActivitiEventType.TASK_COMPLETED.name(), eventListenerList);
            //完成活动时监听
            eventListenerList = new ArrayList<>();
            eventListenerList.add(new ActivityCompletedListenerHandler(confUrl));
            listeners.put(ActivitiEventType.ACTIVITY_COMPLETED.name(), eventListenerList);
            //完成流程时监听
            eventListenerList = new ArrayList<>();
            eventListenerList.add(new ProcessCompletedListenerHandler(confUrl));
            listeners.put(ActivitiEventType.PROCESS_COMPLETED.name(), eventListenerList);
            //取消流程时监听
            eventListenerList = new ArrayList<>();
            eventListenerList.add(new ProcessCancelledListenerHandler(confUrl));
            listeners.put(ActivitiEventType.PROCESS_CANCELLED.name(), eventListenerList);
        }
        //增加监听
        config.setTypedEventListeners(listeners);

        //流程图字体
        config.setActivityFontName(WorkflowConstants.WORKLOW_FONT_NAME);
        config.setAnnotationFontName(WorkflowConstants.WORKLOW_FONT_NAME);
        config.setLabelFontName(WorkflowConstants.WORKLOW_FONT_NAME);
        config.setJpaHandleTransaction(false);
        config.setJpaCloseEntityManager(false);
        config.setJobExecutorActivate(false);//JobExecutor是管理几个线程计时器的组成部分,JobExecutor对多线程的处理较为笨重缓慢
        config.setAsyncExecutorEnabled(false); //定义为true，工作流引擎在启动时就建立启动AsyncExecutor线程

        Map<Object, Object> beans = new HashMap<>();
        beans.put("expr", new ExprUtil()); //自定义表达式
        config.setBeans(beans);

        //自定义流程图样式
        config.setProcessDiagramGenerator(customProcessDiagramGeneratorI);
        config.buildProcessEngine();
        return config;

    }

    @Bean
    public ProcessEngineFactoryBean processEngineFactoryBean(SpringProcessEngineConfiguration processEngineConfiguration){
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(processEngineConfiguration);
        return processEngineFactoryBean;
    }

    @Bean
    public RepositoryService actRepositoryService(ProcessEngine processEngine){
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService actRuntimeService(ProcessEngine processEngine){
        return processEngine.getRuntimeService();
    }

    @Bean
    public IdentityService actIdentityService(ProcessEngine processEngine){
        return processEngine.getIdentityService();
    }

    @Bean
    public TaskService actTaskService(ProcessEngine processEngine){
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService actHistoryService(ProcessEngine processEngine){
        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService actManagementService(ProcessEngine processEngine){
        return processEngine.getManagementService();
    }

    @Bean
    public FormService actFormService(ProcessEngine processEngine){
        return processEngine.getFormService();
    }

    @Bean
    public JsonpCallbackFilter filter(){
        return new JsonpCallbackFilter();
    }

    /**
     * 获取连接配置
     * @param dataSource
     * @return
     */
    private String getWfListenerConfigure(DataSource dataSource){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String confUrl = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement("select s.bs_value from wsd_base_set s where s.bo_code='wf' and s.bs_key='wfListenerConfigure'");
            rs = ps.executeQuery();
            while (rs.next()) {
                confUrl = rs.getString("bs_value");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            try {
                if(rs != null){
                    rs.close();
                }
                if(ps != null){
                    ps.close();
                }
                if(conn != null){
                    conn.close();
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return confUrl;
    }
}
