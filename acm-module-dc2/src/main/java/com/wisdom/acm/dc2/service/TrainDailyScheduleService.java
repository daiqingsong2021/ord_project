package com.wisdom.acm.dc2.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.TrainDailyScheduleAddForm;
import com.wisdom.acm.dc2.form.TrainDailyScheduleUpdateForm;
import com.wisdom.acm.dc2.po.TrainDailySchedulePo;
import com.wisdom.acm.dc2.po.TrainDailySchedulePo;
import com.wisdom.acm.dc2.vo.TrainDailyScheduleVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface TrainDailyScheduleService extends CommService<TrainDailySchedulePo>
{

    TrainDailyScheduleVo addTrainDailySchedule(TrainDailyScheduleAddForm trainDailyScheduleAddForm);

    PageInfo<TrainDailyScheduleVo> selectTrainDailyScheduleList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    void deleteTrainDailySchedule(Integer id);

    TrainDailyScheduleVo updateTrainDailySchedule(TrainDailyScheduleUpdateForm trainDailyScheduleUpdateForm);

    TrainDailyScheduleVo selectById(Integer id);

    List<TrainDailyScheduleVo> selectByParams(Map<String, Object> mapWhere);

}
