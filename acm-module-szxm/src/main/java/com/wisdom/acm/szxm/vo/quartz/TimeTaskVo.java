package com.wisdom.acm.szxm.vo.quartz;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TimeTaskVo
{

    private Integer id;
    private String jobName;//任务名称
    private String jobGroup;//任务分组
    private String description;//任务描述
    private String beanClass;//执行类
    private String cronExpression;//执行时间
    //0 停用 1启用
    private String jobStatus;//任务状态
    private String arguments;//初始参数
    private String jobAuto;//是否自启动

}
