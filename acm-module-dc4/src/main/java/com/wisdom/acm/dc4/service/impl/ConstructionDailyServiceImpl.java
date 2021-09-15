package com.wisdom.acm.dc4.service.impl;

import com.alibaba.fescar.common.util.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc4.common.DateUtil;
import com.wisdom.acm.dc4.common.SzxmCommonUtil;
import com.wisdom.acm.dc4.form.ConstructionDailyAddForm;
import com.wisdom.acm.dc4.form.ConstructionDailyUpdateForm;
import com.wisdom.acm.dc4.mapper.ConstructionDailyMapper;
import com.wisdom.acm.dc4.po.ConstructionDailyPo;
import com.wisdom.acm.dc4.po.ConstructionMonthlyPo;
import com.wisdom.acm.dc4.service.ConstructionDailyService;
import com.wisdom.acm.dc4.service.ConstructionMonthlyService;
import com.wisdom.acm.dc4.vo.ConstructionDailyRcVo;
import com.wisdom.acm.dc4.vo.ConstructionDailyVo;
import com.wisdom.acm.dc4.vo.ConstructionMonthlyVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.GeneralVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;


@Service
public class ConstructionDailyServiceImpl extends BaseService<ConstructionDailyMapper, ConstructionDailyPo> implements ConstructionDailyService {

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private ConstructionMonthlyService constructionMonthlyService;

    @Autowired
    private LeafService leafService;
    /**
     * 新增
     * @param constructionDailyAddForm
     * @return
     */
    @Override
    public ConstructionDailyVo addConstructionDaily(ConstructionDailyAddForm constructionDailyAddForm)
    {
        ConstructionDailyPo constructionDailyPo = dozerMapper.map(constructionDailyAddForm, ConstructionDailyPo.class);
        constructionDailyPo.setReviewStatus("INIT");
        
        int id=leafService.getId();
        constructionDailyPo.setId(id);
        super.insert(constructionDailyPo);
        //插入 更新月度数据
        //this.updateConstructionMonthly(constructionDailyPo.getRecordTime(),constructionDailyPo.getLine());
        //ConstructionDailyVo constructionDailyVo = dozerMapper.map(constructionDailyPo, ConstructionDailyVo.class);//po对象转换为Vo对象
        ConstructionDailyVo constructionDailyVo =this.selectById(id);
        return constructionDailyVo;
    }

    /**
     * 删除   只能删除新建的
     * @param id
     */
    @Override
    @AddLog(title = "删除施工日况", module = LoggerModuleEnum.CONSTRUCTION_DAILYREPORT)
    public void deleteConstructionDaily(Integer id)
    {
        //添加日志
        ConstructionDailyPo constructionDailyPo=super.selectById(id);
        AcmLogger logger = new AcmLogger();
        if(!ObjectUtils.isEmpty(constructionDailyPo))
        {
            logger.setLine(constructionDailyPo.getLine());
            logger.setRecordTime(DateUtil.getDateFormat(constructionDailyPo.getRecordTime()));
            logger.setRecordId(String.valueOf(constructionDailyPo.getId()));
            logger.setRecordStatus(constructionDailyPo.getReviewStatus());
            this.addDeleteLogger(logger);
        }
        super.deleteById(id);
    }

    /**
     * 更新
     * @param constructionDailyUpdateForm
     * @return
     */
    @Override
    @AddLog(title = "更新施工日况", module = LoggerModuleEnum.CONSTRUCTION_DAILYREPORT)
    public ConstructionDailyVo updateConstructionDaily(ConstructionDailyUpdateForm constructionDailyUpdateForm)
    {
        ConstructionDailyPo po = mapper.selectByPrimaryKey(constructionDailyUpdateForm.getId());
        if(ObjectUtils.isEmpty(po)){
            throw new BaseException("修改的数据不存在!");
        }
        // 添加修改日志
        this.addChangeLogger(constructionDailyUpdateForm,po,po.getReviewStatus());
        ConstructionDailyPo constructionDailyPo = dozerMapper.map(constructionDailyUpdateForm, ConstructionDailyPo.class);
        
        super.updateSelectiveById(constructionDailyPo);
        constructionDailyPo=super.selectById(constructionDailyPo.getId());
        //只有审核通过的daily 数据才能更新到月度数据
        if("APPROVED".equals(constructionDailyPo.getReviewStatus()))
        {
            //插入 更新月度数据
            this.updateConstructionMonthly(constructionDailyPo.getRecordTime(),constructionDailyPo.getLine());
        }

        ConstructionDailyVo constructionDailyVo = dozerMapper.map(constructionDailyPo, ConstructionDailyVo.class);//po对象转换为Vo对象
        return constructionDailyVo;
    }

    /**
     * 根据id选择带流程状态
     * @param id
     * @return
     */
    @Override
    public ConstructionDailyVo selectById(Integer id)
    {
        Map<String, Object> mapWhere=new HashMap<String, Object>();
        mapWhere.put("id",id);
        List<ConstructionDailyVo> trainScheduleVoList=mapper.selectByParams(mapWhere);
        ConstructionDailyVo constructionDailyVo=new ConstructionDailyVo();
        if(!ObjectUtils.isEmpty(trainScheduleVoList))
        {
            constructionDailyVo=trainScheduleVoList.get(0);
        }
        return constructionDailyVo;
    }

    /**
     * 施工日况    分页
     * @param mapWhere    endTime   startTime  line
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<ConstructionDailyVo> selectConstructionDailyList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<ConstructionDailyVo> trainScheduleVoList=mapper.selectByParams(mapWhere);
        GeneralVo generalVo=new GeneralVo();
        generalVo.setName("系统生成");
        for(ConstructionDailyVo vo:trainScheduleVoList){
            if(vo.getCreateVo().getCode()==null){
                vo.setCreateVo(generalVo);
            }
        }
        System.out.println(trainScheduleVoList);
        PageInfo<ConstructionDailyVo> pageInfo = new PageInfo<ConstructionDailyVo>(trainScheduleVoList);
        return pageInfo;
    }

    /**
     * 施工日况    不分页
     *  @param mapWhere    endTime   startTime  line
     * @param mapWhere
     * @return
     */
    @Override
    public List<ConstructionDailyVo> selectByParams(Map<String, Object> mapWhere)
    {
        List<ConstructionDailyVo>  constructionDailyVoList= mapper.selectByParams(mapWhere);
        return constructionDailyVoList;
    }

    @Override
    public void approveConstructionDailyWorkFlow(String bizType, List<Integer> ids)
    {
        ConstructionDailyPo updatePo = new ConstructionDailyPo();
        updatePo.setReviewStatus("APPROVED");
        super.updateSelectiveByIds(updatePo, ids);

        List<ConstructionDailyPo> tempConstructionDailyPos=super.selectByIds(ids);
        Map<String,String> map =new HashMap();
        //插入 更新月度数据
        for (ConstructionDailyPo constructionDailyPo:tempConstructionDailyPos)
        {
            String recordTimeStr= DateUtil.getDateFormat(constructionDailyPo.getRecordTime(),"yyyy-MM");
            String mapKeyValue=recordTimeStr+"-"+constructionDailyPo.getLine();
            if(!map.containsValue(mapKeyValue))
            {
                map.put(mapKeyValue,mapKeyValue);
                //插入 更新月度数据
                this.updateConstructionMonthly(constructionDailyPo.getRecordTime(),constructionDailyPo.getLine());
            }
        }


        //推送通知
        List<ConstructionDailyPo> constructionDailyPos = super.selectByIds(ids);
        szxmCommonUtil.approveFlowAndSendMessage(bizType, constructionDailyPos);
    }


    /**
     * 更新
     * @param
     * @return
     */
    @AddLog(title = "更新施工月况", module = LoggerModuleEnum.CONSTRUCTION_DAILYREPORT)
    public ConstructionMonthlyPo updateConstructionMonthly(Date recordTime, String line)
    {
        String recordTimeStr= DateUtil.getDateFormat(recordTime,"yyyy-MM-dd");
        Integer year=Integer.valueOf(recordTimeStr.substring(0,4));
        Integer month=Integer.valueOf(recordTimeStr.substring(5,7)); //月份从0开始,10代表11月份
        Integer day=Integer.valueOf(recordTimeStr.substring(8,10)); //天
        //recordTime 当月所有周一的日期
        List<String> listMonDay= getWeekendInMonth(year,month,day);
        String sumWeekPlanNumWeekPlanNum="0";
        Map<String, Object> mapWhere =new HashMap<>();
        if(!ObjectUtils.isEmpty(listMonDay))
        {
            mapWhere.put("listMonDay",listMonDay);
            mapWhere.put("line",line);
            sumWeekPlanNumWeekPlanNum=  mapper.querySumWeekPlanNum(mapWhere);
            if(sumWeekPlanNumWeekPlanNum==null)
            {
                sumWeekPlanNumWeekPlanNum="0";
            }
        }
        mapWhere.put("recordTime2Month",DateUtil.getDateFormat(recordTime,"yyyy-MM"));
        //从天数据  获取月度数据
        ConstructionDailyVo constructionDailyVo=mapper.selectDaily2MonthByParams(mapWhere);

        //判断月度数据是否存在
        Map<String, Object> mapMonthWhere =new HashMap<>();
        mapMonthWhere.put("recordTime", DateUtil.getDateFormat(recordTime,"yyyy-MM"));
        mapMonthWhere.put("line",line);
        List<ConstructionMonthlyVo> constructionDailyVoLists=constructionMonthlyService.selectByParams(mapMonthWhere);
        ConstructionMonthlyPo constructionMonthlyPo=new ConstructionMonthlyPo();
        //周计划
        constructionMonthlyPo.setWeeklyPlanConstructionQuantity(new BigDecimal(sumWeekPlanNumWeekPlanNum));
        //周实际完成
        BigDecimal WeeklyActualConstructionQuantity=new BigDecimal("0");
        if(!ObjectUtils.isEmpty(constructionDailyVo.getWeeklyActualConstructionQuantity()))
        {
            WeeklyActualConstructionQuantity=constructionDailyVo.getWeeklyActualConstructionQuantity();
        }
        constructionMonthlyPo.setWeeklyActualConstructionQuantity(WeeklyActualConstructionQuantity);
        //日补充计划
        BigDecimal DailyPlanConstructionQuantity=new BigDecimal("0");
        if(!ObjectUtils.isEmpty(constructionDailyVo.getDailyPlanConstructionQuantity()))
        {
            DailyPlanConstructionQuantity=constructionDailyVo.getDailyPlanConstructionQuantity();
        }
        constructionMonthlyPo.setDailyPlanConstructionQuantity(DailyPlanConstructionQuantity);
        //日补充实际完成
        BigDecimal DailyActualConstructionQuantity=new BigDecimal("0");
        if(!ObjectUtils.isEmpty(constructionDailyVo.getDailyActualConstructionQuantity()))
        {
            DailyActualConstructionQuantity=constructionDailyVo.getDailyActualConstructionQuantity();
        }
        constructionMonthlyPo.setDailyActualConstructionQuantity(DailyActualConstructionQuantity);
        //临时补修计划施工数
        BigDecimal TempPlanConstructionQuantity=new BigDecimal("0");
        if(!ObjectUtils.isEmpty(constructionDailyVo.getTempPlanConstructionQuantity()))
        {
            TempPlanConstructionQuantity=constructionDailyVo.getTempPlanConstructionQuantity();
        }
        constructionMonthlyPo.setTempPlanConstructionQuantity(TempPlanConstructionQuantity);
        //临时补修实际完成数

        BigDecimal TempActualConstructionQuantity=new BigDecimal("0");
        if(!ObjectUtils.isEmpty(constructionDailyVo.getTempActualConstructionQuantity()))
        {
            TempActualConstructionQuantity=constructionDailyVo.getTempActualConstructionQuantity();
        }
        constructionMonthlyPo.setTempActualConstructionQuantity(TempActualConstructionQuantity);
        //抢修施工数

        BigDecimal RepairPlanConstructionQuantity=new BigDecimal("0");
        if(!ObjectUtils.isEmpty(constructionDailyVo.getRepairPlanConstructionQuantity()))
        {
            RepairPlanConstructionQuantity=constructionDailyVo.getRepairPlanConstructionQuantity();
        }
        constructionMonthlyPo.setRepairPlanConstructionQuantity (RepairPlanConstructionQuantity);
        //抢修施工实际完成数
        BigDecimal RepairActualConstructionQuantity=new BigDecimal("0");
        if(!ObjectUtils.isEmpty(constructionDailyVo.getRepairActualConstructionQuantity()))
        {
            RepairActualConstructionQuantity=constructionDailyVo.getRepairActualConstructionQuantity();
        }

        constructionMonthlyPo.setRepairActualConstructionQuantity(RepairActualConstructionQuantity);

        //计划总数
        BigDecimal  totlwPlanNumber=new BigDecimal("0");
        if(!ObjectUtils.isEmpty(constructionDailyVo.getTotalPlanConstructionQuantity()))
        {
        	totlwPlanNumber=constructionDailyVo.getTotalPlanConstructionQuantity();
        } else {
        	totlwPlanNumber=new BigDecimal(sumWeekPlanNumWeekPlanNum)
//                .add(DailyPlanConstructionQuantity)
//                .add(TempPlanConstructionQuantity)
                .add(RepairPlanConstructionQuantity);
        }
        BigDecimal  totlwActualNumber=new BigDecimal("0");
        if(!ObjectUtils.isEmpty(constructionDailyVo.getTotalActualConstructionQuantity()))
        {
        	totlwActualNumber=constructionDailyVo.getTotalActualConstructionQuantity();
        } else {
        	totlwActualNumber=constructionDailyVo.getWeeklyActualConstructionQuantity()
//                .add(DailyActualConstructionQuantity)
//                .add(TempActualConstructionQuantity)
                .add(RepairActualConstructionQuantity) ;
        }
        constructionMonthlyPo.setTotalPlanConstructionQuantity(totlwPlanNumber);
        constructionMonthlyPo.setTotalActualConstructionQuantity(totlwActualNumber);
        //判断月度记录是否存在
        if(ObjectUtils.isEmpty(constructionDailyVoLists))
        {
            constructionMonthlyPo.setLine(line);
            Date  recordMonthTimeStr= DateUtil.formatDate(recordTimeStr,"yyyy-MM");
            constructionMonthlyPo.setRecordTime(recordMonthTimeStr);
            constructionMonthlyService.insert(constructionMonthlyPo);
        }
        else
        {
            constructionMonthlyPo.setId(constructionDailyVoLists.get(0).getId());
            constructionMonthlyService.updateSelectiveById(constructionMonthlyPo);
        }

        return constructionMonthlyPo;

    }

    /**
     * 获取当月的所有周一 或者当月当前日期之前的所有周一
     * @param year
     * @param month
     * @param day   默认0 ：为全月的，  传值  则为当月 该天之前的所有周一
     * @return
     */
    public static List getWeekendInMonth(int year, int month, int day) {
        List<String> list = new ArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);// 不设置的话默认为当年
        calendar.set(Calendar.MONTH, month - 1);// 设置月份
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为当月第一天
        int daySize = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 当月最大天数
        if(day>0)
        {
            daySize =day;
        }
        for (int i = 0; i < daySize-1; i++) {
            calendar.add(Calendar.DATE, 1);//在第一天的基础上加1
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            if (week == Calendar.MONDAY) {// 1代表周日，7代表周六 判断这是一个星期的第几天从而判断是否是周末
                String tempDate=year+"-"+month+"-"+calendar.get(Calendar.DAY_OF_MONTH);
                list.add(DateUtil.getDateFormat(DateUtil.formatDate(tempDate,"yyyy-MM-dd")))  ;// 得到当天是一个月的第几天
            }
        }
        return list;
    }

    /**
     *
     * @param num
     * @return
     */
    public BigDecimal checkString2BigDecimal(String num)
    {
        if(StringUtils.isEmpty(num))
        {
            return new BigDecimal("0");
        }
        else
        {
            return new BigDecimal(num);
        }
    }



    @Override
    public void approvedConstructionDaily(List<Integer> ids)
    {
        ConstructionDailyPo updatePo = new ConstructionDailyPo();
        updatePo.setReviewStatus("APPROVED");
        super.updateSelectiveByIds(updatePo, ids);

        List<ConstructionDailyPo> tempConstructionDailyPos=super.selectByIds(ids);
        Map<String,String> map =new HashMap();
        //插入 更新月度数据
        for (ConstructionDailyPo constructionDailyPo:tempConstructionDailyPos)
        {
            String recordTimeStr= DateUtil.getDateFormat(constructionDailyPo.getRecordTime(),"yyyy-MM");
            String mapKeyValue=recordTimeStr+"-"+constructionDailyPo.getLine();
            if(!map.containsValue(mapKeyValue))
            {
                map.put(mapKeyValue,mapKeyValue);
                //插入 更新月度数据
                this.updateConstructionMonthly(constructionDailyPo.getRecordTime(),constructionDailyPo.getLine());
            }
        }
    }

    /**
     * 通过rc方式添加数据
     */
    @Override
    public void addRcConstructionDaily() {
        //查询主表数据是否含有rc_id为空的数据
//        List<Integer> ids=mapper.getConstructionDailyByRcIds();
//        if(CollectionUtils.isNotEmpty(ids)){
//            this.deleteByIds(ids);
//        }

        //获取Rc表中的数据
        List<ConstructionDailyRcVo> vos=mapper.queryConstructionDailyExitsByid();
        if(CollectionUtils.isNotEmpty(vos)){
            for(ConstructionDailyRcVo vo:vos){
                //线路转换
                String line=vo.getLine().substring(0,1);
                ConstructionDailyPo constructionDailyPo = dozerMapper.map(vo, ConstructionDailyPo.class);
                constructionDailyPo.setReviewStatus("INIT");
                //constructionDailyPo.setCreator(1105910);
                constructionDailyPo.setCreatTime(new Date());
                constructionDailyPo.setLine(line);
                int id=leafService.getId();
                constructionDailyPo.setId(id);
                super.insert(constructionDailyPo);
            }
        }
    }
}
