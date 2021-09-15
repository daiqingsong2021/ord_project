package com.wisdom.acm.szxm.service.quartz.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.common.JsonHelper;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.form.quartz.TimeTaskAddForm;
import com.wisdom.acm.szxm.form.quartz.TimeTaskUpdateForm;
import com.wisdom.acm.szxm.mapper.quartz.TimeTaskMapper;
import com.wisdom.acm.szxm.po.rygl.TimeTaskPo;
import com.wisdom.acm.szxm.service.quartz.TimeTaskService;
import com.wisdom.acm.szxm.vo.quartz.TimeTaskVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TimeTaskServiceImpl extends BaseService<TimeTaskMapper, TimeTaskPo> implements TimeTaskService
{
    private Logger logger = LoggerFactory.getLogger(TimeTaskServiceImpl.class);

    @Autowired private Scheduler scheduler;

    @Override
    public PageInfo<TimeTaskVo> selectJobList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum)
    {
        PageHelper.orderBy("job_group,CREAT_TIME desc");
        PageHelper.startPage(currentPageNum, pageSize);
        List<TimeTaskVo> timeTaskVoList = mapper.selectTimeTask(mapWhere);
        PageInfo<TimeTaskVo> pageInfo = new PageInfo<TimeTaskVo>(timeTaskVoList);
        return pageInfo;
    }

    @Override public TimeTaskVo addTimeTask(TimeTaskAddForm timeTaskAddForm)
    {

        Example example = new Example(TimeTaskPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobName", timeTaskAddForm.getJobName());
        criteria.andEqualTo("jobGroup", timeTaskAddForm.getJobGroup());
        TimeTaskPo existPo = this.selectOneByExample(example);
        if (!ObjectUtils.isEmpty(existPo))
        {
            throw new BaseException("同一个分组下，任务名称不能重复");
        }
        TimeTaskPo timeTaskPo = dozerMapper.map(timeTaskAddForm, TimeTaskPo.class);
        if (ObjectUtils.isEmpty(timeTaskPo.getSort()))
        {
            timeTaskPo.setSort(this.selectNextSort());
        }
        //启动定时任务
        try
        {
            this.schedulerJob(timeTaskPo);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new BaseException("新增定时任务失败");
        }
        timeTaskPo.setJobStatus("1");//运行状态
        super.insert(timeTaskPo);
        TimeTaskVo timeTaskVo = dozerMapper.map(timeTaskPo, TimeTaskVo.class);//po对象转换为Vo对象
        return timeTaskVo;
    }

    @Override public TimeTaskVo updateTimeTask(TimeTaskUpdateForm timeTaskUpdateForm)
    {
        //更新Schedule
        TimeTaskPo olderPo = this.selectById(timeTaskUpdateForm.getId());//将数据查询出来
        if(!olderPo.getCronExpression().equals(timeTaskUpdateForm.getCronExpression()))
        {//不一致更新 Schedule
            CronScheduleBuilder cronScheduleBuilder =
                    CronScheduleBuilder.cronSchedule(timeTaskUpdateForm.getCronExpression().trim());
            Trigger newTrigger =
                    TriggerBuilder.newTrigger().withIdentity(olderPo.getJobName(), olderPo.getJobGroup()).startNow()
                            .withSchedule(cronScheduleBuilder).build();
            TriggerKey key = new TriggerKey(olderPo.getJobName(), olderPo.getJobGroup());
            try
            {
                scheduler.rescheduleJob(key,newTrigger);
            }
            catch (Exception e)
            {
               logger.error(e.getMessage());
               throw new BaseException("更新失败!");
            }
        }
        //设置description
        TimeTaskPo updateTimeTaskPo = dozerMapper.map(timeTaskUpdateForm, TimeTaskPo.class);
        this.updateSelectiveById(updateTimeTaskPo);//根据ID更新po，值为null的不更新，只更新不为null的值

        olderPo.setCronExpression(timeTaskUpdateForm.getCronExpression());
        olderPo.setDescription(timeTaskUpdateForm.getDescription());
        TimeTaskVo timeTaskVo = dozerMapper.map(olderPo, TimeTaskVo.class);//po对象转换为Vo对象
        return timeTaskVo;
    }

    @Override public void deleteTimeTask(List<Integer> ids)
    {
        List<TimeTaskPo> timeTaskPoList = this.selectByIds(ids);
        for (TimeTaskPo timeTaskPo : timeTaskPoList)
        {
            try
            {
                TriggerKey triggerKey = TriggerKey.triggerKey(timeTaskPo.getJobName(), timeTaskPo.getJobGroup());
                scheduler.pauseTrigger(triggerKey);                                 // 停止触发器
                scheduler.unscheduleJob(triggerKey);                                // 移除触发器
                scheduler.deleteJob(JobKey.jobKey(timeTaskPo.getJobName(), timeTaskPo.getJobGroup()));// 删除任务
            }
            catch (Exception e)
            {
                logger.error(e.getMessage());
                throw new BaseException("删除定时任务失败");
            }
        }
        this.deleteByIds(ids);
    }

    /**
     * 执行一次任务
     */
    @Override public void triggerTimeTask(Integer id)
    {
        TimeTaskPo timeTaskPo = this.selectById(id);
        JobKey key = new JobKey(timeTaskPo.getJobName(), timeTaskPo.getJobGroup());
        try
        {
            scheduler.triggerJob(key);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new BaseException("执行一次定时任务失败");
        }
    }

    @Override public void pauseTimeTask(Integer id)
    {
        TimeTaskPo timeTaskPo = this.selectById(id);
        JobKey key = new JobKey(timeTaskPo.getJobName(), timeTaskPo.getJobGroup());
        try
        {
            scheduler.pauseJob(key);
            timeTaskPo.setJobStatus("0");//停止状态
            this.updateById(timeTaskPo);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new BaseException("暂停任务失败");
        }
    }

    @Override public void resumeTimeTask(Integer id)
    {
        TimeTaskPo timeTaskPo = this.selectById(id);
        JobKey key = new JobKey(timeTaskPo.getJobName(), timeTaskPo.getJobGroup());
        try
        {
            scheduler.resumeJob(key);
            timeTaskPo.setJobStatus("1");//运行状态
            this.updateById(timeTaskPo);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new BaseException("恢复任务失败");
        }
    }

    /**
     * 启动触发器
     *
     * @param timeTaskPo
     * @throws Exception
     */
    private void schedulerJob(TimeTaskPo timeTaskPo) throws Exception
    {
        //构建job信息
        Class cls = Class.forName(timeTaskPo.getBeanClass());
        // cls.newInstance(); // 检验类是否存在
        JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(timeTaskPo.getJobName(), timeTaskPo.getJobGroup())
                .withDescription(timeTaskPo.getDescription()).build();

        if(StringHelper.isNotNullAndEmpty(timeTaskPo.getArguments()))
        {
            Map<String,Object> arguments=JsonHelper.fromJsonWithGson(timeTaskPo.getArguments(),Map.class);
            for (Map.Entry<String, Object> entry : arguments.entrySet())
            {
                jobDetail.getJobDataMap().put(entry.getKey(),entry.getValue());
            }
        }
        // 触发时间点
        CronScheduleBuilder cronScheduleBuilder =
                CronScheduleBuilder.cronSchedule(timeTaskPo.getCronExpression().trim());
        Trigger trigger =
                TriggerBuilder.newTrigger().withIdentity(timeTaskPo.getJobName(), timeTaskPo.getJobGroup()).startNow()
                        .withSchedule(cronScheduleBuilder).build();
        //交由Scheduler安排触发
        scheduler.scheduleJob(jobDetail, trigger);

    }
}
