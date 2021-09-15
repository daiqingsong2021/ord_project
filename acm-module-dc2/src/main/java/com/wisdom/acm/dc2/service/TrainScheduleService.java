package com.wisdom.acm.dc2.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.TrainScheduleAddForm;
import com.wisdom.acm.dc2.form.TrainScheduleUpdateForm;
import com.wisdom.acm.dc2.po.TrainSchedulePo;
import com.wisdom.acm.dc2.vo.TrainScheduleVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface TrainScheduleService extends CommService<TrainSchedulePo>
{

    TrainScheduleVo addTrainSchedule(TrainScheduleAddForm trainScheduleAddForm);

    PageInfo<TrainScheduleVo> selectTrainScheduleList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    void deleteTrainSchedule(Integer id);

    TrainScheduleVo updateTrainSchedule(TrainScheduleUpdateForm trainScheduleUpdateForm);

    TrainScheduleVo selectById(Integer id);

    List<TrainScheduleVo> selectByParams(Map<String, Object> mapWhere);

    /**
     * 审批结束流程回调
     * @param ids 业务IDs
     */
    void approveTrainScheduleWorkFlow(String bizType, List<Integer> ids);

    /**
     * 小铃铛
     * @param mapWhere
     * @return
     */
    List<TrainScheduleVo> selectFlowTrainScheduleList(Map<String, Object> mapWhere);
}
