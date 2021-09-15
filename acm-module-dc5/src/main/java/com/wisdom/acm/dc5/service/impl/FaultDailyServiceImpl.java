package com.wisdom.acm.dc5.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc5.common.DateUtil;
import com.wisdom.acm.dc5.common.SzxmCommonUtil;
import com.wisdom.acm.dc5.common.SzxmEnumsUtil;
import com.wisdom.acm.dc5.form.FaultDailyAddForm;
import com.wisdom.acm.dc5.form.FaultDailyUpdateForm;
import com.wisdom.acm.dc5.mapper.FaultDailyMapper;
import com.wisdom.acm.dc5.po.FaultDailyPo;
import com.wisdom.acm.dc5.po.FaultMonthlyPo;
import com.wisdom.acm.dc5.service.FaultDailyProblemService;
import com.wisdom.acm.dc5.service.FaultDailyService;
import com.wisdom.acm.dc5.service.FaultMonthlyService;
import com.wisdom.acm.dc5.vo.FaultDailyProblemVo;
import com.wisdom.acm.dc5.vo.FaultDailyVo;
import com.wisdom.acm.dc5.vo.FaultMonthlyVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.doc.CommDocService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FaultDailyServiceImpl extends BaseService<FaultDailyMapper, FaultDailyPo> implements FaultDailyService
{


    @Autowired
    private CommDocService commDocService;
    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private FaultMonthlyService faultMonthlyService;
    @Autowired
    private FaultDailyProblemService faultDailyProblemService;

    /**
     * 新增
     * @param
     * @return
     */
    @Override
    public FaultDailyVo addFaultDaily(FaultDailyAddForm faultDailyAddForm) {
        //通过时间和线路判断是否有重复的数据
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("line",faultDailyAddForm.getLine());
        queryMap.put("recordDay",DateUtil.getDateFormat(faultDailyAddForm.getRecordDay(),"yyyy_MM-dd 00:00:00"));
        List<FaultDailyVo> existFaultDaily = mapper.selectFaultDailyByParams(queryMap);
        if(CollectionUtils.isNotEmpty(existFaultDaily))
            throw new BaseException("数据库里面存在当前线路+记录日期的数据，不允许新增");
        FaultDailyPo faultDailyPo = dozerMapper.map(faultDailyAddForm, FaultDailyPo.class);
        faultDailyPo.setStatus(SzxmEnumsUtil.StatusEnum.INIT.getCode());
        super.insert(faultDailyPo);
        queryMap.clear();
        queryMap.put("id",faultDailyPo.getId());
        FaultDailyVo faultDailyVo = mapper.selectFaultDailyByParams(queryMap).get(0);
        faultDailyVo.setStatusDesc(SzxmEnumsUtil.StatusEnum.getNameByCode(faultDailyVo.getStatus()));
        return faultDailyVo;
    }

    /**
     * 更新
     * @param faultDailyUpdateForm 审批中的状态，不可以修改
     * @return
     */
    @Override
    @AddLog(title = "更新故障日况", module = LoggerModuleEnum.MALFUNCTION_DAILYREPORT)
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public FaultDailyVo updateFaultDaily(FaultDailyUpdateForm faultDailyUpdateForm){
        FaultDailyPo faultDailyPo = super.selectById(faultDailyUpdateForm.getId());
        if(null == faultDailyPo || StringUtils.equalsIgnoreCase(faultDailyPo.getStatus(),SzxmEnumsUtil.StatusEnum.APPROVAL.getCode()))
            throw new BaseException("当前日况故障是审批中的状态，不可以修改");
        // 添加修改日志
        this.addChangeLogger(faultDailyUpdateForm,faultDailyPo,faultDailyPo.getStatus());
        FaultDailyPo trainSchedulePo = dozerMapper.map(faultDailyUpdateForm, FaultDailyPo.class);
        super.updateSelectiveById(trainSchedulePo);
        //更新后修改数据
        if (StringUtils.equalsIgnoreCase(faultDailyPo.getStatus(),SzxmEnumsUtil.StatusEnum.APPROVED.getCode())){
            updateFaultMonthly(faultDailyPo.getRecordDay(),faultDailyPo.getLine());
        }
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("id",faultDailyUpdateForm.getId());
        List<FaultDailyVo> faultDailyVos = mapper.selectFaultDailyByParams(queryMap);
        //组装新增的故障数量和遗留的数据
        generateFaultTotalNum(faultDailyVos);
        FaultDailyVo faultDailyVo = faultDailyVos.get(0);
        String statusName="新建";
        if("APPROVED".equals(faultDailyVo.getStatus()))
        {
            statusName="已发布";
        }
        //faultDailyVo.setStatusDesc(SzxmEnumsUtil.StatusEnum.getNameByCode(faultDailyVo.getStatus()));
        faultDailyVo.setStatusDesc(statusName);
        return faultDailyVo;
    }

    /**
     * 删除
     * @param ids 只有新建和被驳回的状态可以删除，并且清空所有的数据
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void deleteFaultDaily(List<Integer> ids) {
        //通过ids查询数据
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("faultIdList",ids);
        List<FaultDailyProblemVo> faultDailyProblemVos = faultDailyProblemService.selectFaultDailyProblemList(queryMap);

        //删除odr_fault_daily表数据
        super.deleteByIds(ids);
        //删除odr_fault_daily_problem表数据//删除odr_fault_daily_problem_detail表数据
        //获取problemId
        List<Integer> problemIdList = faultDailyProblemVos.stream().map(FaultDailyProblemVo::getId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(problemIdList)){
            faultDailyProblemService.deleteFaultDailyProblem(problemIdList);
        }
    }

    /**
     * 分页查询所有列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<FaultDailyVo> selectFaultDailyPageList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<FaultDailyVo> faultDailyVoList=mapper.selectFaultDailyByParams(mapWhere);
        PageInfo<FaultDailyVo> pageInfo = new PageInfo<>(faultDailyVoList);
        //组装新增的故障数量和遗留的数据
        generateFaultTotalNum(pageInfo.getList());
        return pageInfo;
    }
    /**
     *查询所有列表
     * @param mapWhere
     * @return
     */
    @Override
    public List<FaultDailyVo> selectFaultDailyList(Map<String, Object> mapWhere) {
        List<FaultDailyVo> faultDailyVoList = mapper.selectFaultDailyByParams(mapWhere);
        //组装新增的故障数量和遗留的数据
        generateFaultTotalNum(faultDailyVoList);
        return faultDailyVoList;
    }

    private void generateFaultTotalNum(List<FaultDailyVo> faultDailyVoList){
        if (CollectionUtils.isEmpty(faultDailyVoList)) return;
        List<Integer> faultIdList = faultDailyVoList.stream().map(FaultDailyVo::getId).distinct().collect(Collectors.toList());
        Map<Integer, FaultDailyVo> faultNumMap = this.getFaultTotalNum(null,faultIdList);
        for(FaultDailyVo faultDailyVo : faultDailyVoList){
            FaultDailyVo temp = faultNumMap.get(faultDailyVo.getId());
            faultDailyVo.setTotalProblem(faultDailyVo.getMajorAfc()+faultDailyVo.getMajorCommunication()+faultDailyVo.getMajorConstruction()+
                    faultDailyVo.getMajorMechatronics()+faultDailyVo.getMajorOther()+faultDailyVo.getMajorPower()+faultDailyVo.getMajorSignal()+faultDailyVo.getMajorVehicle());
            if (temp == null) {
                faultDailyVo.setNewProblem(0);
                faultDailyVo.setLegacyProblem(0);
            }else{
                faultDailyVo.setNewProblem(temp.getNewProblem());
                faultDailyVo.setLegacyProblem(temp.getLegacyProblem());
            }

            String statusName="新建";
            if("APPROVED".equals(faultDailyVo.getStatus()))
            {
                statusName="已发布";
            }
            //faultDailyVo.setStatusDesc(SzxmEnumsUtil.StatusEnum.getNameByCode(faultDailyVo.getStatus()));
            faultDailyVo.setStatusDesc(statusName);
        }
    }

    @Override
    public Map<Integer, FaultDailyVo> getFaultTotalNum(Integer faultId, List<Integer> faultIdList) {
        Map<Integer,FaultDailyVo> resultMap = new HashMap<>();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("faultId",faultId);
        queryMap.put("faultIdList",faultIdList);
        //处理中和新建的都是遗留问题
        List<FaultDailyProblemVo> faultDailyProblemVos = faultDailyProblemService.selectFaultDailyProblemList(queryMap);
        //按照项目分组
        Map<Integer, List<FaultDailyProblemVo>> faultDailyProblemMap = faultDailyProblemVos.stream().collect(Collectors.groupingBy(FaultDailyProblemVo::getFaultId));
        Iterator<Map.Entry<Integer, List<FaultDailyProblemVo>>> iterator = faultDailyProblemMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, List<FaultDailyProblemVo>> next = iterator.next();
            if(CollectionUtils.isEmpty(next.getValue())) continue;
            if (!resultMap.containsKey(next.getKey())){
                List<FaultDailyProblemVo> valueList = next.getValue();
                List<FaultDailyProblemVo> legacyProblem = valueList.stream().filter(item -> !StringUtils.equalsIgnoreCase(item.getProblemStatus(), SzxmEnumsUtil.ProblemStatusEnum.DEALED.getCode())).collect(Collectors.toList());
                FaultDailyVo faultDailyVo = new FaultDailyVo();
                faultDailyVo.setLegacyProblem(legacyProblem.size());
                faultDailyVo.setNewProblem(valueList.size());
                resultMap.put(next.getKey(),faultDailyVo);
            }
        }
        return resultMap;
    }

    /**
     * 更新
     * @param
     * @return
     */
    @Override
    public FaultMonthlyPo updateFaultMonthly(Date recordTime, Integer line) {

        String recordTimeStr= DateUtil.getDateFormat(recordTime,"yyyy-MM");

        //recordTime  line  参数不能为空
        if(ObjectUtils.isEmpty(line) || StringHelper.isNullAndEmpty(recordTimeStr))
            throw new BaseException("月度表的月度时间和线路不能为空");
        //查找当月的  所有日况统计数据,已经是当月的审批结束的状态的汇总量
        Map<String, Object> mapWhere=new HashMap<String, Object>();
        mapWhere.put("startTime",recordTimeStr + "-01 00:00:00");
        mapWhere.put("endTime",recordTimeStr + "-31 23:59:59");
        mapWhere.put("line",line);
        FaultMonthlyVo faultMonthlyVo = mapper.selectDaily2MonthlyByParams(mapWhere);
        if (null == faultMonthlyVo) return null;
        faultMonthlyVo.setTotalFault(faultMonthlyVo.getMajorAfc()+faultMonthlyVo.getMajorCommunication()+faultMonthlyVo.getMajorConstruction()+
                faultMonthlyVo.getMajorMechatronics()+faultMonthlyVo.getMajorOther()+faultMonthlyVo.getMajorPower()+faultMonthlyVo.getMajorSignal()+faultMonthlyVo.getMajorVehicle());
        //是否存在月度记录
        Map<String, Object> mapWhereMonth=new HashMap<>();
        mapWhereMonth.put("up2MonthRecordTime",recordTimeStr);
        mapWhereMonth.put("line",line);
        List<FaultMonthlyPo> faultMonthlyPoList=faultMonthlyService.selectFaultMonthByParams(mapWhereMonth);
        FaultMonthlyPo faultMonthlyPo= dozerMapper.map(faultMonthlyVo,FaultMonthlyPo.class);
        //判断月度记录是否存在
        if(ObjectUtils.isEmpty(faultMonthlyPoList)) {
            faultMonthlyService.insert(faultMonthlyPo);
        } else {
            faultMonthlyPo.setId(faultMonthlyPoList.get(0).getId());
            faultMonthlyService.updateSelectiveById(faultMonthlyPo);
        }

        return faultMonthlyPo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void approveFaultDailyWorkFlow(String bizType, List<Integer> ids) {

        FaultDailyPo updatePo = new FaultDailyPo();
        updatePo.setStatus("APPROVED");
        this.updateSelectiveByIds(updatePo, ids);

        List<FaultDailyPo> faultDailyPos=super.selectByIds(ids);
        Map<String,String> map =new HashMap();
        //插入 更新月度数据
        for (FaultDailyPo tempFaultDaily:faultDailyPos) {
            //由于同一天的一条线路只能又一条记录
            String recordTimeStr= DateUtil.getDateFormat(tempFaultDaily.getRecordDay(),"yyyy-MM-dd");
            String mapKeyValue=recordTimeStr+"-"+tempFaultDaily.getLine();
            if(!map.containsValue(mapKeyValue)) {
                map.put(mapKeyValue,mapKeyValue);
                //插入 更新月度数据
                this.updateFaultMonthly(tempFaultDaily.getRecordDay(),tempFaultDaily.getLine());
            }
        }

        //推送通知
        szxmCommonUtil.approveFlowAndSendMessage(bizType, faultDailyPos);
    }
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void approvedFaultDaily(List<Integer> ids) {

        FaultDailyPo updatePo = new FaultDailyPo();
        updatePo.setStatus("APPROVED");
        this.updateSelectiveByIds(updatePo, ids);

        List<FaultDailyPo> faultDailyPos=super.selectByIds(ids);
        Map<String,String> map =new HashMap();
        //插入 更新月度数据
        for (FaultDailyPo tempFaultDaily:faultDailyPos) {
            //由于同一天的一条线路只能又一条记录
            String recordTimeStr= DateUtil.getDateFormat(tempFaultDaily.getRecordDay(),"yyyy-MM-dd");
            String mapKeyValue=recordTimeStr+"-"+tempFaultDaily.getLine();
            if(!map.containsValue(mapKeyValue)) {
                map.put(mapKeyValue,mapKeyValue);
                //插入 更新月度数据
                this.updateFaultMonthly(tempFaultDaily.getRecordDay(),tempFaultDaily.getLine());
            }
        }
    }

}
