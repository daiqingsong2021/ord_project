package com.wisdom.acm.dc2.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.TrainDailyAddForm;
import com.wisdom.acm.dc2.form.TrainDailyUpdateForm;
import com.wisdom.acm.dc2.po.TrainDailyPo;
import com.wisdom.acm.dc2.vo.TrainDailyVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface TrainDailyService extends CommService<TrainDailyPo>
{

    TrainDailyVo addTrainDaily(TrainDailyAddForm trainDailyAddForm);

    PageInfo<TrainDailyVo> selectTrainDailyList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    void deleteTrainDaily(Integer id);

    TrainDailyVo updateTrainDaily(TrainDailyUpdateForm trainDailyUpdateForm);

    TrainDailyVo selectById(Integer id);

    List<TrainDailyVo> selectByParams(Map<String, Object> mapWhere);

    /**
     * 小铃铛
     * @param mapWhere
     * @return
     */
    List<TrainDailyVo> selectFlowTrainDailyList(Map<String, Object> mapWhere);
    TrainDailyVo getTrainDailyList(Integer id);

    void approvedTrainDaily(List<Integer> ids);
}
