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

        /*config.setJdbcMaxActiveConnections(Tools.parseInt(this.maxActive)); //???????????????
        config.setJdbcMaxWaitTime(Tools.parseInt(this.maxWait)); //?????????????????????
        config.setJdbcMaxIdleConnections(Tools.parseInt(this.minIdle)); //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????0??????????????????
        config.setJdbcMaxCheckoutTime(Tools.parseInt(this.timeBetweenEvictionRunsMillis)); //??????????????????
        config.setJdbcPingQuery(this.validationQuery);
        config.setJdbcPingEnabled(Tools.parseBoolean(this.testWhileIdle)); //????????????????????????????????????
        config.setJdbcPingConnectionNotUsedFor(Tools.parseInt(this.minEvictableIdleTimeMillis)); //????????????????????????????????????????????????*/

        //config.setDatabaseSchemaUpdate("false");
        //config.setDeploymentResources(activitiResources); //?????????????????????
        //config.setCustomPostCommandInterceptors(Arrays.asList(new SelfProcessInterceptor()));

        Map<String, List<ActivitiEventListener>> listeners = new HashMap<>();
        //?????????????????????
        List<ActivitiEventListener> eventListenerList = new ArrayList<>();
        eventListenerList.add(new TaskCreateListenerHandler(confUrl));
        listeners.put(ActivitiEventType.TASK_CREATED.name(), eventListenerList);
        if(!Tools.isEmpty(confUrl)) {
            //?????????????????????
            eventListenerList = new ArrayList<>();
            eventListenerList.add(new TaskCompletedListenerHandler(confUrl));
            listeners.put(ActivitiEventType.TASK_COMPLETED.name(), eventListenerList);
            //?????????????????????
            eventListenerList = new ArrayList<>();
            eventListenerList.add(new ActivityCompletedListenerHandler(confUrl));
            listeners.put(ActivitiEventType.ACTIVITY_COMPLETED.name(), eventListenerList);
            //?????????????????????
            eventListenerList = new ArrayList<>();
            eventListenerList.add(new ProcessCompletedListenerHandler(confUrl));
            listeners.put(ActivitiEventType.PROCESS_COMPLETED.name(), eventListenerList);
            //?????????????????????
            eventListenerList = new ArrayList<>();
            eventListenerList.add(new ProcessCancelledListenerHandler(confUrl));
            listeners.put(ActivitiEventType.PROCESS_CANCELLED.name(), eventListenerList);
        }
        //????????????
        config.setTypedEventListeners(listeners);

        //???????????????
        config.setActivityFontName(WorkflowConstants.WORKLOW_FONT_NAME);
        config.setAnnotationFontName(WorkflowConstants.WORKLOW_FONT_NAME);
        config.setLabelFontName(WorkflowConstants.WORKLOW_FONT_NAME);
        config.setJpaHandleTransaction(false);
        config.setJpaCloseEntityManager(false);
        config.setJobExecutorActivate(false);//JobExecutor?????????????????????????????????????????????,JobExecutor???????????????????????????????????????
        config.setAsyncExecutorEnabled(false); //?????????true?????????????????????????????????????????????AsyncExecutor??????

        Map<Object, Object> beans = new HashMap<>();
        beans.put("expr", new ExprUtil()); //??????????????????
        config.setBeans(beans);

        //????????????????????????
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
     * ??????????????????
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
