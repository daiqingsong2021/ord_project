package com.wisdom.acm.dc2.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.TrainDailyPassageMileAddForm;
import com.wisdom.acm.dc2.form.TrainDailyPassageMileUpdateForm;
import com.wisdom.acm.dc2.form.TrainDailyPassageMileAddForm;
import com.wisdom.acm.dc2.form.TrainDailyPassageMileUpdateForm;
import com.wisdom.acm.dc2.po.TrainDailyPassageMilePo;
import com.wisdom.acm.dc2.po.TrainDailyPassageMilePo;
import com.wisdom.acm.dc2.vo.TrainDailyPassageMileVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface TrainDailyPassageMileService extends CommService<TrainDailyPassageMilePo>
{

    TrainDailyPassageMileVo addTrainDailyPassageMile(TrainDailyPassageMileAddForm trainDailyPassageMileAddForm);


    void deleteTrainDailyPassageMile(Integer id);

    TrainDailyPassageMileVo updateTrainDailyPassageMile(TrainDailyPassageMileUpdateForm trainDailyPassageMileUpdateForm);

    TrainDailyPassageMileVo selectById(Integer id);

    List<TrainDailyPassageMileVo> selectByParams(Map<String, Object> mapWhere);

}
