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
            throw new BaseException("?????????????????????????????????????????????");
        }
        TimeTaskPo timeTaskPo = dozerMapper.map(timeTaskAddForm, TimeTaskPo.class);
        if (ObjectUtils.isEmpty(timeTaskPo.getSort()))
        {
            timeTaskPo.setSort(this.selectNextSort());
        }
        //??????????????????
        try
        {
            this.schedulerJob(timeTaskPo);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new BaseException("????????????????????????");
        }
        timeTaskPo.setJobStatus("1");//????????????
        super.insert(timeTaskPo);
        TimeTaskVo timeTaskVo = dozerMapper.map(timeTaskPo, TimeTaskVo.class);//po???????????????Vo??????
        return timeTaskVo;
    }

    @Override public TimeTaskVo updateTimeTask(TimeTaskUpdateForm timeTaskUpdateForm)
    {
        //??????Schedule
        TimeTaskPo olderPo = this.selectById(timeTaskUpdateForm.getId());//?????????????????????
        if(!olderPo.getCronExpression().equals(timeTaskUpdateForm.getCronExpression()))
        {//??????????????? Schedule
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
               throw new BaseException("????????????!");
            }
        }
        //??????description
        TimeTaskPo updateTimeTaskPo = dozerMapper.map(timeTaskUpdateForm, TimeTaskPo.class);
        this.updateSelectiveById(updateTimeTaskPo);//??????ID??????po?????????null??????????????????????????????null??????

        olderPo.setCronExpression(timeTaskUpdateForm.getCronExpression());
        olderPo.setDescription(timeTaskUpdateForm.getDescription());
        TimeTaskVo timeTaskVo = dozerMapper.map(olderPo, TimeTaskVo.class);//po???????????????Vo??????
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
                scheduler.pauseTrigger(triggerKey);                                 // ???????????????
                scheduler.unscheduleJob(triggerKey);                                // ???????????????
                scheduler.deleteJob(JobKey.jobKey(timeTaskPo.getJobName(), timeTaskPo.getJobGroup()));// ????????????
            }
            catch (Exception e)
            {
                logger.error(e.getMessage());
                throw new BaseException("????????????????????????");
            }
        }
        this.deleteByIds(ids);
    }

    /**
     * ??????????????????
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
            throw new BaseException("??????????????????????????????");
        }
    }

    @Override public void pauseTimeTask(Integer id)
    {
        TimeTaskPo timeTaskPo = this.selectById(id);
        JobKey key = new JobKey(timeTaskPo.getJobName(), timeTaskPo.getJobGroup());
        try
        {
            scheduler.pauseJob(key);
            timeTaskPo.setJobStatus("0");//????????????
            this.updateById(timeTaskPo);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new BaseException("??????????????????");
        }
    }

    @Override public void resumeTimeTask(Integer id)
    {
        TimeTaskPo timeTaskPo = this.selectById(id);
        JobKey key = new JobKey(timeTaskPo.getJobName(), timeTaskPo.getJobGroup());
        try
        {
            scheduler.resumeJob(key);
            timeTaskPo.setJobStatus("1");//????????????
            this.updateById(timeTaskPo);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new BaseException("??????????????????");
        }
    }

    /**
     * ???????????????
     *
     * @param timeTaskPo
     * @throws Exception
     */
    private void schedulerJob(TimeTaskPo timeTaskPo) throws Exception
    {
        //??????job??????
        Class cls = Class.forName(timeTaskPo.getBeanClass());
        // cls.newInstance(); // ?????????????????????
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
        // ???????????????
        CronScheduleBuilder cronScheduleBuilder =
                CronScheduleBuilder.cronSchedule(timeTaskPo.getCronExpression().trim());
        Trigger trigger =
                TriggerBuilder.newTrigger().withIdentity(timeTaskPo.getJobName(), timeTaskPo.getJobGroup()).startNow()
                        .withSchedule(cronScheduleBuilder).build();
        //??????Scheduler????????????
        scheduler.scheduleJob(jobDetail, trigger);

    }
}
