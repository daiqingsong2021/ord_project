package com.wisdom.acm.dc2.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.common.DateUtil;
import com.wisdom.acm.dc2.form.TrainScheduleAddForm;
import com.wisdom.acm.dc2.form.TrainScheduleStationAddForm;
import com.wisdom.acm.dc2.form.TrainScheduleStationUpdateForm;
import com.wisdom.acm.dc2.form.TrainScheduleUpdateForm;
import com.wisdom.acm.dc2.mapper.TrainScheduleMapper;
import com.wisdom.acm.dc2.po.TrainDailyPo;
import com.wisdom.acm.dc2.po.TrainSchedulePo;
import com.wisdom.acm.dc2.service.TrainScheduleService;
import com.wisdom.acm.dc2.service.TrainScheduleStationService;
import com.wisdom.acm.dc2.vo.TrainDailyVo;
import com.wisdom.acm.dc2.vo.TrainScheduleStationVo;
import com.wisdom.acm.dc2.vo.TrainScheduleVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.feign.doc.CommDocService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainScheduleServiceImpl extends BaseService<TrainScheduleMapper, TrainSchedulePo> implements TrainScheduleService
{


    @Autowired
    private CommDocService commDocService;
    @Autowired
    private TrainScheduleStationService trainScheduleStationService;
    @Autowired
    private LeafService leafService;

    /**
     * TrainScheduleVo插入数据库
     * @param trainScheduleAddForm
     * @return
     */
    @Override
    public TrainScheduleVo addTrainSchedule(TrainScheduleAddForm trainScheduleAddForm)
    {
        TrainSchedulePo trainSchedulePo = dozerMapper.map(trainScheduleAddForm, TrainSchedulePo.class);
        trainSchedulePo.setReviewStatus("INIT");
        trainSchedulePo.setId(leafService.getId());
        super.insert(trainSchedulePo);
        //上行线路站点
        List<TrainScheduleStationVo> trainScheduleUpStationList =new ArrayList<>();

        List<TrainScheduleStationAddForm> trainScheduleUpStationForms=trainScheduleAddForm.getTrainScheduleUpStationForm();
        for(TrainScheduleStationAddForm trainScheduleUpStationForm: trainScheduleUpStationForms)
        {
            trainScheduleUpStationForm.setScheduleId(trainSchedulePo.getId().toString());
            trainScheduleUpStationForm.setLine(trainSchedulePo.getLine());
            //线路类型（0：上行，1：下行）
            trainScheduleUpStationForm.setLineType("0");
            TrainScheduleStationVo trainScheduleUpStationVo= trainScheduleStationService.addTrainScheduleStation(trainScheduleUpStationForm);
            trainScheduleUpStationList.add(trainScheduleUpStationVo);
        }
        //下行线路站点
        List<TrainScheduleStationVo> trainScheduleDownStationList  =new ArrayList<>();
        List<TrainScheduleStationAddForm> trainScheduleDownStationForms=trainScheduleAddForm.getTrainScheduleDownStationForm();
        for(TrainScheduleStationAddForm trainScheduleDownStationForm: trainScheduleDownStationForms)
        {
            trainScheduleDownStationForm.setScheduleId(trainSchedulePo.getId().toString());
            trainScheduleDownStationForm.setLine(trainSchedulePo.getLine());
            //线路类型（0：上行，1：下行）
            trainScheduleDownStationForm.setLineType("1");
            TrainScheduleStationVo trainScheduleDownStationVo=trainScheduleStationService.addTrainScheduleStation(trainScheduleDownStationForm);
            trainScheduleDownStationList.add(trainScheduleDownStationVo);
        }
        TrainScheduleVo trainScheduleVo = dozerMapper.map(trainSchedulePo, TrainScheduleVo.class);//po对象转换为Vo对象
        //返回插入上行线路站点
        trainScheduleVo.setTrainScheduleUpStationVo(trainScheduleUpStationList);
        //返回插入下行线路站点
        trainScheduleVo.setTrainScheduleDownStationVo(trainScheduleDownStationList);

        trainScheduleVo.getReviewStatusVo().setCode("INIT");
        trainScheduleVo.getReviewStatusVo().setName("新建");
        return trainScheduleVo;
    }

    @Override
    @AddLog(title = "删除列车时刻表", module = LoggerModuleEnum.DRIVING_TIMETABLE)
    public void deleteTrainSchedule(Integer id) {
        //添加日志
        TrainSchedulePo trainSchedulePo=super.selectById(id);
        AcmLogger logger = new AcmLogger();
        if(!ObjectUtils.isEmpty(trainSchedulePo))
        {
            logger.setLine(trainSchedulePo.getLine());
            // logger.setRecordTime(DateUtil.getDateFormat(trainSchedulePo.getRecordTime()));
            logger.setRecordId(String.valueOf(trainSchedulePo.getId()));
            logger.setRecordStatus(trainSchedulePo.getReviewStatus());
            this.addDeleteLogger(logger);
        }
        super.deleteById(id);
        //查询上下行列车站点
        Map<String, Object> mapWhere=new HashMap<>();
        mapWhere.put("scheduleId",id);
        List<TrainScheduleStationVo> TrainScheduleStationVo=trainScheduleStationService.selectByParams(mapWhere);
        for (TrainScheduleStationVo trainScheduleStationVo:TrainScheduleStationVo )
        {
            //删除列车时刻表
            trainScheduleStationService.deleteTrainScheduleStation(Integer.valueOf(trainScheduleStationVo.getId()));
        }
    }


    /**
     * 更新
     * @param trainScheduleUpdateForm
     * @return
     */
    @Override
    public TrainScheduleVo updateTrainSchedule(TrainScheduleUpdateForm trainScheduleUpdateForm) {
        TrainSchedulePo trainSchedulePo = dozerMapper.map(trainScheduleUpdateForm, TrainSchedulePo.class);
        super.updateSelectiveById(trainSchedulePo);
        TrainScheduleVo trainScheduleVo = dozerMapper.map(super.selectById(trainSchedulePo.getId()), TrainScheduleVo.class);//po对象转换为Vo对象

        //查询上下行列车站点
        Map<String, Object> mapWhere=new HashMap<>();
        mapWhere.put("scheduleId",trainSchedulePo.getId());

        //上行线路站点
        List<TrainScheduleStationVo> trainScheduleUpStationList =new ArrayList<>();

        List<TrainScheduleStationUpdateForm> trainScheduleUpStationForms=trainScheduleUpdateForm.getTrainScheduleUpStationForm();
        for(TrainScheduleStationUpdateForm trainScheduleUpStationForm: trainScheduleUpStationForms)
        {
            TrainScheduleStationVo trainScheduleUpStationVo= trainScheduleStationService.updateTrainScheduleStation(trainScheduleUpStationForm);
            trainScheduleUpStationList.add(trainScheduleUpStationVo);
        }
        //下行线路站点
        List<TrainScheduleStationVo> trainScheduleDownStationList  =new ArrayList<>();
        List<TrainScheduleStationUpdateForm> trainScheduleDownStationForms=trainScheduleUpdateForm.getTrainScheduleDownStationForm();
        for(TrainScheduleStationUpdateForm trainScheduleDownStationForm: trainScheduleDownStationForms)
        {
            TrainScheduleStationVo trainScheduleDownStationVo=trainScheduleStationService.updateTrainScheduleStation(trainScheduleDownStationForm);
            trainScheduleDownStationList.add(trainScheduleDownStationVo);
        }
        trainScheduleVo.setTrainScheduleUpStationVo(trainScheduleUpStationList);
        trainScheduleVo.setTrainScheduleUpStationVo(trainScheduleDownStationList);

        return trainScheduleVo;
    }

    @Override
    public TrainScheduleVo selectById(Integer id) {
        TrainScheduleVo trainScheduleVo = dozerMapper.map(super.selectById(id), TrainScheduleVo.class);//po对象转换为Vo对象

        //查询上下行列车站点
        Map<String, Object> mapWhere=new HashMap<>();
        mapWhere.put("scheduleId",trainScheduleVo.getId());
        List<TrainScheduleStationVo> trainScheduleStationVoList=trainScheduleStationService.selectByParams(mapWhere);
        //上行线路站点
        List<TrainScheduleStationVo> trainScheduleUpStationList =new ArrayList<>();
        //下行线路站点
        List<TrainScheduleStationVo> trainScheduleDownStationList  =new ArrayList<>();
        for (TrainScheduleStationVo trainScheduleStationVo:trainScheduleStationVoList )
        {
            //线路类型（0：上行，1：下行）
            if("0".equals(trainScheduleStationVo.getLineType()))
            {
                trainScheduleUpStationList.add(trainScheduleStationVo);
            }
            if("1".equals(trainScheduleStationVo.getLineType()))
            {
                trainScheduleDownStationList.add(trainScheduleStationVo);
            }
        }
        trainScheduleVo.setTrainScheduleUpStationVo(trainScheduleUpStationList);
        trainScheduleVo.setTrainScheduleUpStationVo(trainScheduleDownStationList);
        return trainScheduleVo;
    }

    /**
     * 根据参数查询TrainScheduleVo 数据
     * @param mapWhere
     * @return
     */
    @Override
    public List<TrainScheduleVo> selectByParams(Map<String, Object> mapWhere) {

        String line=String.valueOf(mapWhere.get("line"));
        if(line.indexOf(",")!=-1)
        {
            String[] idsArray = String.valueOf(mapWhere.get("line")).split(",");
            List<String> lines = new ArrayList<String>(Arrays.asList(idsArray));
            mapWhere.put("lines",lines);
            mapWhere.remove("line");
        }
        List<TrainScheduleVo>  trainScheduleVoList= mapper.selectByParams(mapWhere);
        for (TrainScheduleVo trainScheduleVo:trainScheduleVoList)
        {
            Map<String, Object> upMapWhere =new HashMap<>();
            upMapWhere.put("scheduleId",trainScheduleVo.getId());
            upMapWhere.put("lineType","0");
            trainScheduleVo.setTrainScheduleUpStationVo(trainScheduleStationService.selectByParams(upMapWhere));
            //下行站点
            upMapWhere.put("lineType","1");
            trainScheduleVo.setTrainScheduleDownStationVo(trainScheduleStationService.selectByParams(upMapWhere));
        }
        return trainScheduleVoList;
    }

    /**
     * 分页查询所有列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<TrainScheduleVo> selectTrainScheduleList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<TrainScheduleVo> trainScheduleVoList=mapper.selectByParams(mapWhere);
        for (TrainScheduleVo trainScheduleVo:trainScheduleVoList)
        {
            Map<String, Object> upMapWhere =new HashMap<>();
            upMapWhere.put("scheduleId",trainScheduleVo.getId());
            upMapWhere.put("lineType","0");
            trainScheduleVo.setTrainScheduleUpStationVo(trainScheduleStationService.selectByParams(upMapWhere));
            //下行站点
            upMapWhere.put("lineType","1");
            trainScheduleVo.setTrainScheduleDownStationVo(trainScheduleStationService.selectByParams(upMapWhere));
        }
        PageInfo<TrainScheduleVo> pageInfo = new PageInfo<TrainScheduleVo>(trainScheduleVoList);
        return pageInfo;
    }

    @Override
    public void approveTrainScheduleWorkFlow(String bizType, List<Integer> ids) {

        TrainSchedulePo updatePo = new TrainSchedulePo();
        updatePo.setReviewStatus("APPROVED");
        this.updateSelectiveByIds(updatePo, ids);

        //更改文件状态
        String bizIds = "";
        for (Integer id : ids) {
            bizIds += (id + ",");
        }
        bizIds = bizIds.substring(0, bizIds.lastIndexOf(","));
        commDocService.releaseDocByBizTypeAndBizIds("MATERIEL-SORT",bizIds);//发布关联文档
        commDocService.releaseDocByBizTypeAndBizIds(bizType, bizIds);//发布关联文档

        //推送通知
        List<TrainSchedulePo> arriveRecordPos = super.selectByIds(ids);
        //szxmCommonUtil.approveFlowAndSendMessage(bizType, arriveRecordPos);
    }

    @Override
    public List<TrainScheduleVo> selectFlowTrainScheduleList(Map<String, Object> mapWhere) {
        if (StringHelper.isNotNullAndEmpty(String.valueOf(mapWhere.get("ids")))) {//如果主键Ids 不为空
            String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
            List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
            List<Integer> tempids = ids.stream().map(id -> Integer.valueOf(id)).collect(Collectors.toList());
            mapWhere.put("ids", tempids);
        }
        List<TrainScheduleVo> trainScheduleVoList=mapper.selectByParams(mapWhere);
        return trainScheduleVoList;
    }



}
