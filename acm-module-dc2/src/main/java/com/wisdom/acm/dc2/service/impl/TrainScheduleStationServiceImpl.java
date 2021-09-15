package com.wisdom.acm.dc2.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.TrainScheduleStationAddForm;
import com.wisdom.acm.dc2.form.TrainScheduleStationUpdateForm;
import com.wisdom.acm.dc2.mapper.TrainScheduleStationMapper;
import com.wisdom.acm.dc2.po.TrainScheduleStationPo;
import com.wisdom.acm.dc2.service.TrainScheduleStationService;
import com.wisdom.acm.dc2.vo.TrainScheduleStationVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.dc.util.StringHelper;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainScheduleStationServiceImpl extends BaseService<TrainScheduleStationMapper, TrainScheduleStationPo> implements TrainScheduleStationService
{


    @Autowired
    private CommDocService commDocService;


    /**
     * TrainScheduleStationVo插入数据库
     * @param trainScheduleStationAddForm
     * @return
     */
    @Override
    public TrainScheduleStationVo addTrainScheduleStation(TrainScheduleStationAddForm trainScheduleStationAddForm)
    {
        TrainScheduleStationPo trainScheduleStationPo = dozerMapper.map(trainScheduleStationAddForm, TrainScheduleStationPo.class);
        super.insert(trainScheduleStationPo);
        TrainScheduleStationVo trainScheduleStationVo = dozerMapper.map(trainScheduleStationPo, TrainScheduleStationVo.class);//po对象转换为Vo对象
        return trainScheduleStationVo;
    }

    @Override
    @AddLog(title = "删除列车时刻表站点", module = LoggerModuleEnum.DRIVING_TIMETABLE_STATION)
    public void deleteTrainScheduleStation(Integer id) {
        //添加日志
        TrainScheduleStationPo trainScheduleStationPo=super.selectById(id);
        AcmLogger logger = new AcmLogger();
        if(!ObjectUtils.isEmpty(trainScheduleStationPo))
        {
            logger.setLine(trainScheduleStationPo.getLine());
           // logger.setRecordTime(DateUtil.getDateFormat(trainScheduleStationPo.getRecordTime()));
            logger.setRecordId(String.valueOf(trainScheduleStationPo.getId()));
            this.addDeleteLogger(logger);
        }
        super.deleteById(id);
    }


    /**
     * 更新
     * @param trainScheduleStationUpdateForm
     * @return
     */
    @Override
    public TrainScheduleStationVo updateTrainScheduleStation(TrainScheduleStationUpdateForm trainScheduleStationUpdateForm) {
        TrainScheduleStationPo trainScheduleStationPo = dozerMapper.map(trainScheduleStationUpdateForm, TrainScheduleStationPo.class);
        super.updateSelectiveById(trainScheduleStationPo);
        TrainScheduleStationVo trainScheduleStationVo = dozerMapper.map(super.selectById(trainScheduleStationPo.getId()), TrainScheduleStationVo.class);//po对象转换为Vo对象
        return trainScheduleStationVo;
    }

    @Override
    public TrainScheduleStationVo selectById(Integer id) {
        TrainScheduleStationVo trainScheduleStationVo = dozerMapper.map(super.selectById(id), TrainScheduleStationVo.class);//po对象转换为Vo对象
        return trainScheduleStationVo;
    }

    /**
     * 根据参数查询TrainScheduleStationVo 数据
     * @param mapWhere
     * @return
     */
    @Override
    public List<TrainScheduleStationVo> selectByParams(Map<String, Object> mapWhere) {

        String line=String.valueOf(mapWhere.get("line"));
        if(line.indexOf(",")!=-1)
        {
            String[] idsArray = String.valueOf(mapWhere.get("line")).split(",");
            List<String> lines = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("lines",lines);
            mapWhere.remove("line");
        }
        List<TrainScheduleStationVo>  trainScheduleStationVoList= mapper.selectByParams(mapWhere);
        return trainScheduleStationVoList;
    }

    /**
     * 分页查询所有列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<TrainScheduleStationVo> selectTrainScheduleStationList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<TrainScheduleStationVo> trainScheduleStationVoList=mapper.selectByParams(mapWhere);
        PageInfo<TrainScheduleStationVo> pageInfo = new PageInfo<TrainScheduleStationVo>(trainScheduleStationVoList);
        return pageInfo;
    }




}
