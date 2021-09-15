package com.wisdom.acm.dc2.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.TrainScheduleStationAddForm;
import com.wisdom.acm.dc2.form.TrainScheduleStationUpdateForm;
import com.wisdom.acm.dc2.po.TrainScheduleStationPo;
import com.wisdom.acm.dc2.vo.TrainScheduleStationVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface TrainScheduleStationService extends CommService<TrainScheduleStationPo>
{

    TrainScheduleStationVo addTrainScheduleStation(TrainScheduleStationAddForm trainScheduleAddForm);

    PageInfo<TrainScheduleStationVo> selectTrainScheduleStationList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    void deleteTrainScheduleStation(Integer id);

    TrainScheduleStationVo updateTrainScheduleStation(TrainScheduleStationUpdateForm trainScheduleUpdateForm);

    TrainScheduleStationVo selectById(Integer id);

    List<TrainScheduleStationVo> selectByParams(Map<String, Object> mapWhere);

}
