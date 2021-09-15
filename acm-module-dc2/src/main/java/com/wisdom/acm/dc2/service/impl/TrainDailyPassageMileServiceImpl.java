package com.wisdom.acm.dc2.service.impl;

import com.wisdom.acm.dc2.form.TrainDailyPassageMileAddForm;
import com.wisdom.acm.dc2.form.TrainDailyPassageMileUpdateForm;
import com.wisdom.acm.dc2.mapper.TrainDailyPassageMileMapper;
import com.wisdom.acm.dc2.po.TrainDailyPassageMilePo;
import com.wisdom.acm.dc2.service.TrainDailyPassageMileService;
import com.wisdom.acm.dc2.vo.TrainDailyPassageMileVo;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.feign.doc.CommDocService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
@Slf4j
public class TrainDailyPassageMileServiceImpl extends BaseService<TrainDailyPassageMileMapper, TrainDailyPassageMilePo> implements TrainDailyPassageMileService
{


    @Autowired
    private CommDocService commDocService;
    @Autowired
    private LeafService leafService;

    /**
     * TrainDailyPassageMileVo插入数据库
     * @param trainDailyPassageMileAddForm
     * @return
     */
    @Override
    public TrainDailyPassageMileVo addTrainDailyPassageMile(TrainDailyPassageMileAddForm trainDailyPassageMileAddForm)
    {
        TrainDailyPassageMilePo trainDailyPassageMilePo = dozerMapper.map(trainDailyPassageMileAddForm, TrainDailyPassageMilePo.class);
        trainDailyPassageMilePo.setId(leafService.getId());
        super.insert(trainDailyPassageMilePo);
        TrainDailyPassageMileVo trainDailyPassageMileVo= dozerMapper.map(super.selectById(trainDailyPassageMilePo.getId()), TrainDailyPassageMileVo.class);
        return trainDailyPassageMileVo;
    }

    @Override
    public void deleteTrainDailyPassageMile(Integer id) {
        super.deleteById(id);
    }


    /**
     * 更新
     * @param trainDailyPassageMileUpdateForm
     * @return
     */
    @Override
    public TrainDailyPassageMileVo updateTrainDailyPassageMile(TrainDailyPassageMileUpdateForm trainDailyPassageMileUpdateForm) {
        TrainDailyPassageMilePo trainDailyPassageMilePo = dozerMapper.map(trainDailyPassageMileUpdateForm, TrainDailyPassageMilePo.class);
        super.updateSelectiveById(trainDailyPassageMilePo);
        TrainDailyPassageMileVo trainDailyPassageMileVo = dozerMapper.map(super.selectById(trainDailyPassageMilePo.getId()), TrainDailyPassageMileVo.class);//po对象转换为Vo对象

        return trainDailyPassageMileVo;
    }

    @Override
    public TrainDailyPassageMileVo selectById(Integer id) {
        TrainDailyPassageMileVo trainDailyPassageMileVo = dozerMapper.map(super.selectById(id), TrainDailyPassageMileVo.class);//po对象转换为Vo对象
        return trainDailyPassageMileVo;
    }

    /**
     * 根据参数查询TrainDailyPassageMileVo 数据
     * @param mapWhere
     * @return
     */
    @Override
    public List<TrainDailyPassageMileVo> selectByParams(Map<String, Object> mapWhere) {

        List<TrainDailyPassageMileVo>  trainDailyPassageMileVoList= mapper.selectByParams(mapWhere);
        return trainDailyPassageMileVoList;
    }





}
