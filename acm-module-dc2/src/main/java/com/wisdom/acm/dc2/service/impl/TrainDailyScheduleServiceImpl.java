package com.wisdom.acm.dc2.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.TrainDailyScheduleAddForm;
import com.wisdom.acm.dc2.form.TrainDailyScheduleUpdateForm;
import com.wisdom.acm.dc2.mapper.TrainDailyScheduleMapper;
import com.wisdom.acm.dc2.po.TrainDailySchedulePo;
import com.wisdom.acm.dc2.service.TrainDailyScheduleService;
import com.wisdom.acm.dc2.vo.TrainDailyScheduleVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.feign.doc.CommDocService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TrainDailyScheduleServiceImpl extends BaseService<TrainDailyScheduleMapper, TrainDailySchedulePo> implements TrainDailyScheduleService
{




    /**
     * TrainDailyScheduleVo插入数据库
     * @param trainDailyScheduleAddForm
     * @return
     */
    @Override
    public TrainDailyScheduleVo addTrainDailySchedule(TrainDailyScheduleAddForm trainDailyScheduleAddForm)
    {
        TrainDailySchedulePo trainDailySchedulePo = dozerMapper.map(trainDailyScheduleAddForm, TrainDailySchedulePo.class);
        super.insert(trainDailySchedulePo);
        TrainDailyScheduleVo trainDailyScheduleVo = dozerMapper.map(trainDailySchedulePo, TrainDailyScheduleVo.class);//po对象转换为Vo对象
        return trainDailyScheduleVo;
    }

    @Override
    @AddLog(title = "删除日况非正常行驶数据", module = LoggerModuleEnum.DAILY_ABNORMAL_SCHEDULE)
    public void deleteTrainDailySchedule(Integer id) {
        //添加日志
        TrainDailySchedulePo trainDailySchedulePo=super.selectById(id);
        AcmLogger logger = new AcmLogger();
        if(!ObjectUtils.isEmpty(trainDailySchedulePo))
        {
            logger.setLine(trainDailySchedulePo.getLine());
           // logger.setRecordTime(DateUtil.getDateFormat(trainDailySchedulePo.getRecordTime()));
            logger.setRecordId(String.valueOf(trainDailySchedulePo.getId()));
            this.addDeleteLogger(logger);
        }
        super.deleteById(id);

    }


    /**
     * 更新
     * @param trainDailyScheduleUpdateForm
     * @return
     */
    @Override
    public TrainDailyScheduleVo updateTrainDailySchedule(TrainDailyScheduleUpdateForm trainDailyScheduleUpdateForm) {
        TrainDailySchedulePo trainDailySchedulePo = dozerMapper.map(trainDailyScheduleUpdateForm, TrainDailySchedulePo.class);
        super.updateSelectiveById(trainDailySchedulePo);
        TrainDailyScheduleVo trainDailyScheduleVo = dozerMapper.map(super.selectById(trainDailySchedulePo.getId()), TrainDailyScheduleVo.class);//po对象转换为Vo对象
        return trainDailyScheduleVo;
    }

    @Override
    public TrainDailyScheduleVo selectById(Integer id) {
        TrainDailyScheduleVo trainDailyScheduleVo = dozerMapper.map(super.selectById(id), TrainDailyScheduleVo.class);//po对象转换为Vo对象
        return trainDailyScheduleVo;
    }

    /**
     * 根据参数查询TrainDailyScheduleVo 数据
     * @param mapWhere
     * @return
     */
    @Override
    public List<TrainDailyScheduleVo> selectByParams(Map<String, Object> mapWhere) {

        List<TrainDailyScheduleVo>  trainDailyScheduleVoList= mapper.selectByParams(mapWhere);
        return trainDailyScheduleVoList;
    }

    /**
     * 分页查询所有列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<TrainDailyScheduleVo> selectTrainDailyScheduleList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<TrainDailyScheduleVo> trainDailyScheduleVoList=mapper.selectByParams(mapWhere);
        PageInfo<TrainDailyScheduleVo> pageInfo = new PageInfo<TrainDailyScheduleVo>(trainDailyScheduleVoList);
        return pageInfo;
    }




}
