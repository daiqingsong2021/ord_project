package com.wisdom.acm.dc2.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.wisdom.acm.dc2.common.DateUtil;
import com.wisdom.acm.dc2.common.SzxmCommonUtil;
import com.wisdom.acm.dc2.form.*;
import com.wisdom.acm.dc2.mapper.TrainDailyMapper;
import com.wisdom.acm.dc2.mapper.TrainMonthlyMapper;
import com.wisdom.acm.dc2.po.TrainDailyPassageMilePo;
import com.wisdom.acm.dc2.po.TrainDailyPo;
import com.wisdom.acm.dc2.po.TrainMonthlyPo;
import com.wisdom.acm.dc2.service.TrainDailyPassageMileService;
import com.wisdom.acm.dc2.service.TrainDailyScheduleService;
import com.wisdom.acm.dc2.service.TrainDailyService;
import com.wisdom.acm.dc2.service.TrainMonthlyService;
import com.wisdom.acm.dc2.vo.TrainDailyPassageMileVo;
import com.wisdom.acm.dc2.vo.TrainDailyScheduleVo;
import com.wisdom.acm.dc2.vo.TrainDailyVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.feign.doc.CommDocService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainDailyServiceImpl extends BaseService<TrainDailyMapper, TrainDailyPo> implements TrainDailyService
{


    @Autowired
    private CommDocService commDocService;
    @Autowired
    private SzxmCommonUtil szxmCommonUtil;
    @Autowired
    private TrainMonthlyMapper trainMonthlyMapper;
    @Autowired
    private TrainMonthlyService trainMonthlyService;
    @Autowired
    private LeafService leafService;
    @Autowired
    private TrainDailyScheduleService trainDailyScheduleService;
    @Autowired
    private TrainDailyPassageMileService trainDailyPassageMileService;

    /**
     * TrainDailyVo插入数据库
     * @param trainDailyAddForm
     * @return
     */
    @Override
    @AddLog(title = "新增行车日况", module = LoggerModuleEnum.DRIVING_DRIVINGINFO,initContent = true)
    public TrainDailyVo addTrainDaily(TrainDailyAddForm trainDailyAddForm)
    {
        TrainDailyPo trainDailyPo = dozerMapper.map(trainDailyAddForm, TrainDailyPo.class);
        trainDailyPo.setReviewStatus("INIT");
        trainDailyPo.setId(leafService.getId());
        super.insert(trainDailyPo);
        //上行线路站点
        List<TrainDailyScheduleVo> trainScheduleUpStationList =new ArrayList<>();

        List<TrainDailyScheduleAddForm> trainScheduleUpStationForms=trainDailyAddForm.getTrainDailyScheduleUpAddForm();
        for(TrainDailyScheduleAddForm trainScheduleUpStationForm: trainScheduleUpStationForms)
        {
            trainScheduleUpStationForm.setTrainDailyId(trainDailyPo.getId().toString());
            trainScheduleUpStationForm.setLine(trainDailyPo.getLine());
            trainScheduleUpStationForm.setRecordTime(trainDailyPo.getRecordTime());
            //线路类型（0：上行，1：下行）
            trainScheduleUpStationForm.setLineType("0");
            TrainDailyScheduleVo trainScheduleUpStationVo= trainDailyScheduleService.addTrainDailySchedule(trainScheduleUpStationForm);
            trainScheduleUpStationList.add(trainScheduleUpStationVo);
        }
        //下行线路站点
        List<TrainDailyScheduleVo> trainScheduleDownStationList  =new ArrayList<>();
        List<TrainDailyScheduleAddForm> trainScheduleDownStationForms=trainDailyAddForm.getTrainDailyScheduleDownAddForm();
        for(TrainDailyScheduleAddForm trainScheduleDownStationForm: trainScheduleDownStationForms)
        {
            trainScheduleDownStationForm.setTrainDailyId(trainDailyPo.getId().toString());
            trainScheduleDownStationForm.setLine(trainDailyPo.getLine());
            //线路类型（0：上行，1：下行）
            trainScheduleDownStationForm.setLineType("1");
            TrainDailyScheduleVo trainScheduleDownStationVo=trainDailyScheduleService.addTrainDailySchedule(trainScheduleDownStationForm);
            trainScheduleDownStationList.add(trainScheduleDownStationVo);
        }

        //没有审批流程直接更新月度数据
        // this.updateTrainMonthly(trainDailyPo.getRecordTime(),trainDailyPo.getLine());
        TrainDailyVo trainDailyVo = dozerMapper.map(trainDailyPo, TrainDailyVo.class);//po对象转换为Vo对象

        TrainDailyPassageMileAddForm trainDailyPassageMileAddForm=trainDailyAddForm.getTrainDailyPassageMileAddForm();
        if(!ObjectUtils.isEmpty(trainDailyPassageMileAddForm))
        {
            TrainDailyPassageMilePo TrainDailyPassageMilePo= dozerMapper.map(trainDailyPassageMileAddForm, TrainDailyPassageMilePo.class);
            TrainDailyPassageMilePo.setId(leafService.getId());
            TrainDailyPassageMilePo.setTrainDailyId(trainDailyPo.getId().toString());
            trainDailyPassageMileService.insert(TrainDailyPassageMilePo);
            trainDailyVo.setTrainDailyPassageMileVo(trainDailyPassageMileService.selectById(TrainDailyPassageMilePo.getId()));
        }
        //返回插入上行线路站点
        trainDailyVo.setTrainDailyUpSchedule(trainScheduleUpStationList);
        //返回插入下行线路站点
        trainDailyVo.setTrainDailyDownSchedule(trainScheduleDownStationList);
        return trainDailyVo;
    }

    @Override
    @AddLog(title = "删除行车日况", module = LoggerModuleEnum.DRIVING_DRIVINGINFO)
    public void deleteTrainDaily(Integer id)
    {
        //添加日志
        TrainDailyPo trainDailyPo=super.selectById(id);
        AcmLogger logger = new AcmLogger();
        if(!ObjectUtils.isEmpty(trainDailyPo))
        {
            logger.setLine(trainDailyPo.getLine());
            logger.setRecordTime(DateUtil.getDateFormat(trainDailyPo.getRecordTime()));
            logger.setRecordId(String.valueOf(trainDailyPo.getId()));
            logger.setRecordStatus(trainDailyPo.getReviewStatus());
            this.addDeleteLogger(logger);
        }
        super.deleteById(id);
        //查询上下行列车站点
        Map<String, Object> mapWhere=new HashMap<>();
        mapWhere.put("trainDailyId",id);
        List<TrainDailyScheduleVo> TrainScheduleStationVos=trainDailyScheduleService.selectByParams(mapWhere);
        //for 循环 为了记录日志
        //List<String> ids = TrainScheduleStationVos.stream().map(TrainDailyScheduleVo::getId).collect(Collectors.toList());
        for (TrainDailyScheduleVo trainScheduleStationVo:TrainScheduleStationVos )
        {
            //删除列车时刻表
            trainDailyScheduleService.deleteTrainDailySchedule (Integer.valueOf(trainScheduleStationVo.getId()));
        }

        ////删除非正常行驶里程数据
        Map<String, Object> tempMapWhere=new HashMap<>();
        tempMapWhere.put("trainDailyId",id);
        List<TrainDailyPassageMileVo> trainDailyPassageMileVo=trainDailyPassageMileService.selectByParams(tempMapWhere);
        for (TrainDailyPassageMileVo trainDailyPassageMile:trainDailyPassageMileVo )
        {
            //删除非正常行驶里程数据
            trainDailyPassageMileService.deleteById(trainDailyPassageMile.getId());
        }

    }


    /**
     * 更新
     * @param trainDailyUpdateForm
     * @return
     */
    @Override
    @AddLog(title = "更新行车日况", module = LoggerModuleEnum.DRIVING_DRIVINGINFO)
    public TrainDailyVo updateTrainDaily(TrainDailyUpdateForm trainDailyUpdateForm)
    {
        TrainDailyPo po =this.selectById(trainDailyUpdateForm.getId());
        if(ObjectUtils.isEmpty(po)){
            throw new BaseException("修改的数据不存在!");
        }
        // 添加修改日志
        this.addChangeLogger(trainDailyUpdateForm,po,po.getReviewStatus());
        TrainDailyPo trainDailyPo = dozerMapper.map(trainDailyUpdateForm, TrainDailyPo.class);
        super.updateSelectiveById(trainDailyPo);
        TrainDailyPo tempTrainDailyPo =super.selectById(trainDailyPo.getId());
        //只有审核通过的daily 数据才能更新到月度数据
        if("APPROVED".equals(tempTrainDailyPo.getReviewStatus()))
        {
            this.updateTrainMonthly(tempTrainDailyPo.getRecordTime(),trainDailyPo.getLine());
        }
        //this.updateTrainMonthly(tempTrainDailyPo.getRecordTime(),trainDailyPo.getLine());
        //上行线路站点
        List<TrainDailyScheduleVo> trainDailyScheduleUpList =new ArrayList<>();

        List<TrainDailyScheduleUpdateForm> trainScheduleUpStationForms=trainDailyUpdateForm.getTrainDailyScheduleUpUpdateForm();
        for(TrainDailyScheduleUpdateForm trainScheduleUpStationForm: trainScheduleUpStationForms)
        {
            TrainDailyScheduleVo trainScheduleUpStationVo= trainDailyScheduleService.updateTrainDailySchedule(trainScheduleUpStationForm);
            trainDailyScheduleUpList.add(trainScheduleUpStationVo);
        }
        //下行线路站点
        List<TrainDailyScheduleVo> trainDailyScheduleDownList  =new ArrayList<>();
        List<TrainDailyScheduleUpdateForm> trainScheduleDownStationForms=trainDailyUpdateForm.getTrainDailyScheduleDownUpdateForm();
        for(TrainDailyScheduleUpdateForm trainScheduleDownStationForm: trainScheduleDownStationForms)
        {
            TrainDailyScheduleVo trainScheduleDownStationVo=trainDailyScheduleService.updateTrainDailySchedule(trainScheduleDownStationForm);
            trainDailyScheduleDownList.add(trainScheduleDownStationVo);
        }

        TrainDailyVo trainDailyVo = dozerMapper.map(tempTrainDailyPo, TrainDailyVo.class);//po对象转换为Vo对象

        //更新非正常里程数据
        TrainDailyPassageMileUpdateForm trainDailyPassageMileUpdateForm=trainDailyUpdateForm.getTrainDailyPassageMileUpdateForm();
        if(!ObjectUtils.isEmpty(trainDailyPassageMileUpdateForm))
        {
            TrainDailyPassageMilePo TrainDailyPassageMilePo= dozerMapper.map(trainDailyPassageMileUpdateForm, TrainDailyPassageMilePo.class);
            trainDailyPassageMileService.updateSelectiveById(TrainDailyPassageMilePo);
            trainDailyVo.setTrainDailyPassageMileVo(trainDailyPassageMileService.selectById(TrainDailyPassageMilePo.getId()));
        }

        trainDailyVo.setTrainDailyUpSchedule  (trainDailyScheduleUpList);
        trainDailyVo.setTrainDailyDownSchedule(trainDailyScheduleDownList);
        return trainDailyVo;
    }

    @Override
    public TrainDailyVo selectById(Integer id) {
        TrainDailyVo trainDailyVo = dozerMapper.map(super.selectById(id), TrainDailyVo.class);//po对象转换为Vo对象

        //查询上下行列车站点
        Map<String, Object> mapWhere=new HashMap<>();
        mapWhere.put("trainDailyId",id);
        List<TrainDailyScheduleVo> trainScheduleStationVoList=trainDailyScheduleService.selectByParams(mapWhere);
        //上行线路站点
        List<TrainDailyScheduleVo> trainDailyUpScheduleList =new ArrayList<>();
        //下行线路站点
        List<TrainDailyScheduleVo> trainDailyDownScheduleList  =new ArrayList<>();
        for (TrainDailyScheduleVo trainScheduleStationVo:trainScheduleStationVoList )
        {
            //线路类型（0：上行，1：下行）
            if("0".equals(trainScheduleStationVo.getLineType()))
            {
                trainDailyUpScheduleList.add(trainScheduleStationVo);
            }
            if("1".equals(trainScheduleStationVo.getLineType()))
            {
                trainDailyDownScheduleList.add(trainScheduleStationVo);
            }
        }
        trainDailyVo.setTrainDailyUpSchedule(trainDailyUpScheduleList);
        trainDailyVo.setTrainDailyDownSchedule(trainDailyDownScheduleList);
        ////查询非正常行驶里程数据
        Map<String, Object> tempMapWhere=new HashMap<>();
        tempMapWhere.put("trainDailyId",id);
        List<TrainDailyPassageMileVo> trainDailyPassageMileVo=trainDailyPassageMileService.selectByParams(tempMapWhere);
        if(!ObjectUtils.isEmpty(trainDailyPassageMileVo))
        {
            trainDailyVo.setTrainDailyPassageMileVo(trainDailyPassageMileVo.get(0));
        }
        return trainDailyVo;
    }

    /**
     * 根据参数查询TrainDailyVo 数据
     * @param mapWhere
     * @return
     */
    @Override
    public List<TrainDailyVo> selectByParams(Map<String, Object> mapWhere) {

        List<TrainDailyVo>  trainDailyVoList= mapper.selectByParams(mapWhere);
        for (TrainDailyVo trainDailyVo:trainDailyVoList)
        {
            Map<String, Object> upMapWhere =new HashMap<>();
            upMapWhere.put("trainDailyId",trainDailyVo.getId());
            upMapWhere.put("lineType","0");
            trainDailyVo.setTrainDailyUpSchedule(trainDailyScheduleService.selectByParams(upMapWhere));
            //下行站点
            upMapWhere.put("lineType","1");
            trainDailyVo.setTrainDailyDownSchedule(trainDailyScheduleService.selectByParams(upMapWhere));


            ////查询非正常行驶里程数据
            Map<String, Object> tempMapWhere=new HashMap<>();
            tempMapWhere.put("trainDailyId",trainDailyVo.getId());
            List<TrainDailyPassageMileVo> trainDailyPassageMileVo=trainDailyPassageMileService.selectByParams(tempMapWhere);
            if(!ObjectUtils.isEmpty(trainDailyPassageMileVo))
            {
                trainDailyVo.setTrainDailyPassageMileVo(trainDailyPassageMileVo.get(0));
            }
        }

        return trainDailyVoList;
    }

    /**
     * 分页查询所有列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<TrainDailyVo> selectTrainDailyList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<TrainDailyVo> trainDailyVoList=mapper.selectByParams(mapWhere);
        for (TrainDailyVo trainDailyVo:trainDailyVoList)
        {
            Map<String, Object> upMapWhere =new HashMap<>();
            upMapWhere.put("trainDailyId",trainDailyVo.getId());
            upMapWhere.put("lineType","0");
            trainDailyVo.setTrainDailyUpSchedule(trainDailyScheduleService.selectByParams(upMapWhere));
            //下行站点
            upMapWhere.put("lineType","1");
            trainDailyVo.setTrainDailyDownSchedule(trainDailyScheduleService.selectByParams(upMapWhere));


            ////查询非正常行驶里程数据
            Map<String, Object> tempMapWhere=new HashMap<>();
            tempMapWhere.put("trainDailyId",trainDailyVo.getId());
            List<TrainDailyPassageMileVo> trainDailyPassageMileVo=trainDailyPassageMileService.selectByParams(tempMapWhere);
            if(!ObjectUtils.isEmpty(trainDailyPassageMileVo))
            {
                trainDailyVo.setTrainDailyPassageMileVo(trainDailyPassageMileVo.get(0));
            }
        }

        PageInfo<TrainDailyVo> pageInfo = new PageInfo<TrainDailyVo>(trainDailyVoList);
        return pageInfo;
    }

    @Override
    public List<TrainDailyVo> selectFlowTrainDailyList(Map<String, Object> mapWhere) {
        if (StringHelper.isNotNullAndEmpty(String.valueOf(mapWhere.get("ids")))) {//如果主键Ids 不为空
            String[] idsArray = String.valueOf(mapWhere.get("ids")).split(",");
            List<String> ids = new ArrayList<String>(Arrays.asList(idsArray));
            List<Integer> tempids = ids.stream().map(id -> Integer.valueOf(id)).collect(Collectors.toList());
            mapWhere.put("ids", tempids);
        }
        List<TrainDailyVo> trainDailyVoList=mapper.selectByParams(mapWhere);
        return trainDailyVoList;
    }

    @Override
    public TrainDailyVo getTrainDailyList(Integer id) {
        Map<String, Object> mapWhere= Maps.newHashMap();
        mapWhere.put("id",id);
        List<TrainDailyVo> trainDailyVoList=mapper.selectByParams(mapWhere);
        for (TrainDailyVo trainDailyVo:trainDailyVoList)
        {
            Map<String, Object> upMapWhere =new HashMap<>();
            upMapWhere.put("trainDailyId",trainDailyVo.getId());
            upMapWhere.put("lineType","0");
            trainDailyVo.setTrainDailyUpSchedule(trainDailyScheduleService.selectByParams(upMapWhere));
            //下行站点
            upMapWhere.put("lineType","1");
            trainDailyVo.setTrainDailyDownSchedule(trainDailyScheduleService.selectByParams(upMapWhere));
            ////查询非正常行驶里程数据
            Map<String, Object> tempMapWhere=new HashMap<>();
            tempMapWhere.put("trainDailyId",trainDailyVo.getId());
            List<TrainDailyPassageMileVo> trainDailyPassageMileVo=trainDailyPassageMileService.selectByParams(tempMapWhere);
            if(!ObjectUtils.isEmpty(trainDailyPassageMileVo))
            {
                trainDailyVo.setTrainDailyPassageMileVo(trainDailyPassageMileVo.get(0));
            }
        }
        return trainDailyVoList.get(0);
    }

    @Override
    public void approvedTrainDaily(List<Integer> ids) {
        TrainDailyPo updatePo = new TrainDailyPo();
        updatePo.setReviewStatus("APPROVED");
        this.updateSelectiveByIds(updatePo, ids);
        List<TrainDailyPo> tempTrainDailyPos=super.selectByIds(ids);
        Map<String,String> map =new HashMap();
        //插入 更新月度数据
        for (TrainDailyPo tempTrainDaily:tempTrainDailyPos){
            String recordTimeStr= DateUtil.getDateFormat(tempTrainDaily.getRecordTime(),"yyyy-MM");
            String mapKeyValue=recordTimeStr+"-"+tempTrainDaily.getLine();
            if(!map.containsValue(mapKeyValue)){
                map.put(mapKeyValue,mapKeyValue);
                //插入 更新月度数据
                this.updateTrainMonthly(tempTrainDaily.getRecordTime(),tempTrainDaily.getLine());
            }
        }
    }


    /**
     * 更新
     * @param
     * @return
     */
    public TrainMonthlyPo updateTrainMonthly(Date recordTime,String line) {

        String recordTimeStr= DateUtil.getDateFormat(recordTime,"yyyy-MM");

        //recordTime  line  参数不能为空
        if(StringHelper.isNullAndEmpty(line) || StringHelper.isNullAndEmpty(recordTimeStr))
        {
            return null;
        }
        //查找当月的日况数据
        Map<String, Object> mapWhere=new HashMap<String, Object>();
        mapWhere.put("up2MonthRecordTime",recordTimeStr);
        mapWhere.put("line",line);
        TrainDailyVo trainDailyVo=mapper.selectByMonthRecordTime(mapWhere);
        //
        Map<String, Object> mapWhereMonth=new HashMap<String, Object>();
        mapWhereMonth.put("recordTime",recordTimeStr);
        mapWhereMonth.put("line",line);
        List<TrainMonthlyPo> trainMonthlyPoList=trainMonthlyMapper.selectByParams(mapWhereMonth);
        TrainMonthlyPo trainMonthlyPo= dozerMapper.map(trainDailyVo,TrainMonthlyPo.class);
        trainMonthlyPo.setPlanOperatingColumn(trainDailyVo.getTrainScheduleVo().getPlannedOperationColumn());
        //判断月度记录是否存在
        if(ObjectUtils.isEmpty(trainMonthlyPoList))
        {
            trainMonthlyPo.setLine(line);
            trainMonthlyPo.setRecordTime(recordTime);
            trainMonthlyService.insert(trainMonthlyPo);
        }
        else
        {
            trainMonthlyPo.setId(trainMonthlyPoList.get(0).getId());
            trainMonthlyPo.setRecordTime(recordTime);
            //兑现率  实际开行（列）/计划开行（列）*100%
            BigDecimal actualOperatingColumn= new BigDecimal(trainMonthlyPo.getActualOperatingColumn());
            BigDecimal planOperatingColumn= new BigDecimal(trainMonthlyPo.getPlanOperatingColumn());
            String fulfillmentRate=actualOperatingColumn.divide(planOperatingColumn ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
            trainMonthlyPo.setFulfillmentRate(fulfillmentRate);
            //准点率（终到准点列次）/（实际开行（列））*100%
            BigDecimal arriveOntimeColumn= new BigDecimal(trainMonthlyPo.getArriveOntimeColumn());
            String onTimeRate = arriveOntimeColumn.divide(actualOperatingColumn, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
            trainMonthlyPo.setOnTimeRate(onTimeRate);
            trainMonthlyService.updateSelectiveById(trainMonthlyPo);
        }

        return trainMonthlyPo;


    }
}
