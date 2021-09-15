package com.wisdom.acm.szxm.service.quartz;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.quartz.TimeTaskAddForm;
import com.wisdom.acm.szxm.form.quartz.TimeTaskUpdateForm;
import com.wisdom.acm.szxm.po.rygl.TimeTaskPo;
import com.wisdom.acm.szxm.vo.quartz.TimeTaskVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface TimeTaskService extends CommService<TimeTaskPo>
{

    /**
     * 查询Job列表
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    PageInfo<TimeTaskVo> selectJobList(Map<String,Object> mapWhere,Integer pageSize, Integer currentPageNum);

    /**
     * 增加Job
     * @param timeTaskAddForm
     * @return
     */
    TimeTaskVo addTimeTask(TimeTaskAddForm timeTaskAddForm);

    /**
     * 修改Job schedule
     * @param timeTaskUpdateForm
     * @return
     */
    TimeTaskVo updateTimeTask(TimeTaskUpdateForm timeTaskUpdateForm);

    /**
     * 删除Job
     * @param ids
     */
    void deleteTimeTask(List<Integer> ids);

    /**
     * 触发job
     * @return
     */
    void triggerTimeTask(Integer id);

    /**
     * 暂停job

     * @return
     */
    void pauseTimeTask(Integer id);

    /**
     * 恢复job
     * @return
     */
    void resumeTimeTask(Integer id);

}
