package com.wisdom.acm.szxm.po.rygl;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "szxm_sys_time_task")
@Data
public class TimeTaskPo extends BasePo
{

    /**
     * 项目ID
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 标段ID
     */
    @Column(name = "section_id")
    private Integer sectionId;

    @Column(name = "job_name")
    private String jobName;


    @Column(name = "job_group")
    private String jobGroup;

    @Column(name = "description")
    private String description;


    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "bean_class")
    private String beanClass;

    /**
     * 0 停止 1运行
     */
    @Column(name = "job_status")
    private String jobStatus;

    /**
     * 是否跟随服务开启 自启动 0否 1是
     */
    @Column(name = "job_auto")
    private String jobAuto;

    @Column(name = "arguments")
    private String arguments;

}
