package com.wisdom.acm.processing.service.report.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisdom.acm.processing.common.DateUtil;
import com.wisdom.acm.processing.common.DcCommonUtil;
import com.wisdom.acm.processing.common.StringHelper;
import com.wisdom.acm.processing.form.FineDailyReportAddForm;
import com.wisdom.acm.processing.mapper.report.FineDailyReportMapper;
import com.wisdom.acm.processing.po.report.FineDailyReportPo;
import com.wisdom.acm.processing.service.report.FineDailyReportService;
import com.wisdom.acm.processing.vo.report.FineDailyReportVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.dc.util.EnumsUtil;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/12/24/024 10:06
 * Description:<描述>
 */
@Service
@Slf4j
public class FineDailyReportServiceImpl extends BaseService<FineDailyReportMapper, FineDailyReportPo> implements FineDailyReportService {
    @Autowired
    private DcCommonUtil dcCommonUtil;
    @Autowired
    private CommUserService commUserService;
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Async("odr_report")
    @AddLog(title = "系统新增1号线运营日报日志记录",module = LoggerModuleEnum.ODR_REPORT,initContent = true)
    @Override
    public FineDailyReportVo addFineDailyReport1(FineDailyReportAddForm form) {
        FineDailyReportPo po=dozerMapper.map(form,FineDailyReportPo.class);
        po.setReportName(DateUtil.getDateFormat(form.getReportDay()));
        this.insert(po);
        this.setAcmLogger(new AcmLogger("定时器生成1号线日报时间为： "+ DateUtil.getDateFormat(new Date()),form.getReportDay(),form.getLine(),
                po.getId().toString(),"INIT"));
        return null;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Async("odr_report")
    @AddLog(title = "系统新增3号线运营日报日志记录",module = LoggerModuleEnum.ODR_REPORT,initContent = true)
    @Override
    public FineDailyReportVo addFineDailyReport3(FineDailyReportAddForm form) {
        FineDailyReportPo po=dozerMapper.map(form,FineDailyReportPo.class);
        po.setReportName(DateUtil.getDateFormat(form.getReportDay()));
        this.insert(po);
        this.setAcmLogger(new AcmLogger("定时器生成3号线日报时间为： "+ DateUtil.getDateFormat(new Date()),form.getReportDay(),form.getLine(),
                po.getId().toString(),"INIT"));
        return null;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @AddLog(title = "新增运营日报记录",module = LoggerModuleEnum.ODR_REPORT,initContent = true)
    @Override
    public FineDailyReportVo addFineDailyReport(FineDailyReportAddForm form) {
        FineDailyReportPo po=dozerMapper.map(form,FineDailyReportPo.class);
        po.setReportName(DateUtil.getDateFormat(form.getReportDay()));
        po.setReportType("1");
        po.setReviewStatus("INIT");
        this.insert(po);
        this.setAcmLogger(new AcmLogger("生成日报时间为： "+ DateUtil.getDateFormat(new Date()),form.getReportDay(),form.getLine(),
                po.getId().toString(),"INIT"));
        return null;
    }

    @Override
    public PageInfo<FineDailyReportVo> getFineDailyReportList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        mapWhere.put("reviewStatus", StringHelper.formattString(String.valueOf(mapWhere.get("reviewStatus"))));
        List<String> line= Lists.newArrayList();
        if(!ObjectUtils.isEmpty(mapWhere.get("line"))){
            String[] keys = mapWhere.get("line").toString().split(",");
            for(String str: keys){
                line.add(str);
            }
        }
        mapWhere.put("lines",line);
        if(line.size()==0){
            line.add("1");line.add("2");line.add("3");
        }
        mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        mapWhere.put("endTime", StringHelper.formattString(String.valueOf(mapWhere.get("endTime"))));
        PageHelper.startPage(currentPageNum, pageSize);
        List<FineDailyReportVo> fineDailyReportList= mapper.getFineDailyReportList(mapWhere);
        PageInfo<FineDailyReportVo> pageInfo = new PageInfo<FineDailyReportVo>(fineDailyReportList);
        if (!ObjectUtils.isEmpty(pageInfo.getList()))
        {
            for(FineDailyReportVo fineDailyReportVo : pageInfo.getList()){
                if("0".equals(fineDailyReportVo.getReportType())){
                    fineDailyReportVo.setInitMan("系统生成");
                }
                //返回前端查看状态
                fineDailyReportVo.getReviewStatusVo().setName(EnumsUtil.StatusEnum.valueOf(fineDailyReportVo.getReviewStatusVo()
                        .getCode()).getName());
            }
        }
        return pageInfo;
    }

    @Override
    public void deleteFineReport(List<Integer> ids) {
        this.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void approveFineDailyReportFlow(String bizType, List<Integer> ids) {
        FineDailyReportPo updatePo = new FineDailyReportPo();
        updatePo.setReviewStatus(EnumsUtil.StatusEnum.APPROVED.getCode());
        UserInfo user=commUserService.getLoginUser();
        updatePo.setReviewer(user.getId());
        this.updateSelectiveByIds(updatePo, ids);
        List<FineDailyReportPo> fineDailyReportPos=super.selectByIds(ids);
        //推送通知
        dcCommonUtil.approveFlowAndSendMessage(bizType, fineDailyReportPos);
    }

    @Override
    public List<FineDailyReportVo> getFlowFineDailyReportList(Map<String, Object> mapWhere) {
        mapWhere.put("reviewStatus", StringHelper.formattString(String.valueOf(mapWhere.get("reviewStatus"))));
        List<String> line= Lists.newArrayList();
        if(!ObjectUtils.isEmpty(mapWhere.get("line"))){
            String[] keys = mapWhere.get("line").toString().split(",");
            for(String str: keys){
                line.add(str);
            }
        }
        mapWhere.put("lines",line);
        if(line.size()==0){
            line.add("1");line.add("2");line.add("3");
        }
        mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        mapWhere.put("endTime", StringHelper.formattString(String.valueOf(mapWhere.get("endTime"))));
        List<FineDailyReportVo> fineDailyReportList= mapper.getFineDailyReportList(mapWhere);
        if (!ObjectUtils.isEmpty(fineDailyReportList))
        {
            for(FineDailyReportVo fineDailyReportVo : fineDailyReportList){
                if("0".equals(fineDailyReportVo.getReportType())){
                    fineDailyReportVo.setInitMan("系统生成");
                }
                //返回前端查看状态
                fineDailyReportVo.getReviewStatusVo().setName(EnumsUtil.StatusEnum.valueOf(fineDailyReportVo.getReviewStatusVo()
                        .getCode()).getName());
            }
        }
        return fineDailyReportList;
    }

    @Override
    public List<FineDailyReportVo> getFlowFineDailyReports(Map<String, Object> mapWhere) {
        List<FineDailyReportVo> dailyReportVos= mapper.getFlowFineDailyReports(mapWhere);
        if (!ObjectUtils.isEmpty(dailyReportVos))
        {
            for(FineDailyReportVo dailyReportVo : dailyReportVos){
                if("0".equals(dailyReportVo.getReportType())){
                    dailyReportVo.setInitMan("系统生成");
                }
                //返回前端查看状态
                dailyReportVo.getReviewStatusVo().setName(EnumsUtil.StatusEnum.valueOf(dailyReportVo.getReviewStatusVo()
                        .getCode()).getName());
            }
        }
        return dailyReportVos;
    }
}
