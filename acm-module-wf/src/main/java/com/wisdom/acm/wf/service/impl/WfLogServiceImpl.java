package com.wisdom.acm.wf.service.impl;

import com.wisdom.acm.wf.enums.WfLogOptTypeEnum;
import com.wisdom.acm.wf.form.WfLogAddForm;
import com.wisdom.acm.wf.mapper.WfLogMapper;
import com.wisdom.acm.wf.po.WfFormPo;
import com.wisdom.acm.wf.po.WfLogPo;
import com.wisdom.acm.wf.service.WfFormService;
import com.wisdom.acm.wf.service.WfLogService;
import com.wisdom.acm.wf.vo.WfLogVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommActivitiService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.DateUtil;
import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.wf.WfActivityInstanceVo;
import com.wisdom.base.common.vo.wf.WfLogDetailVo;
import com.wisdom.base.common.vo.wf.WfProcLogDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class WfLogServiceImpl extends BaseService<WfLogMapper, WfLogPo> implements WfLogService {

    @Autowired
    private CommUserService userService;

    @Autowired
    private WfFormService formService;

    @Autowired
    private CommActivitiService commActivitiService;

    /**
     * 查询流程日志
     * @param procInstId
     * @return
     */
    @Override
    public WfLogDetailVo getLogVoListByProcInstId(String procInstId){
        WfLogDetailVo retLog = new WfLogDetailVo();
        List<WfLogPo> wfLogPos = this.queryLogPoByProcInstId(procInstId);
        List<WfProcLogDetailVo> list = new ArrayList<>();
        List<Integer> userIds = ListUtil.toValueList(wfLogPos,"creator",Integer.class); // 用户ID集合
        WfFormPo form = this.formService.getFormInfoByProcInstId(procInstId);
        if(form != null){
            userIds.add(form.getCreator());
        }
        Map<Integer, UserVo> userVoMap = this.userService.getUserVoMapByUserIds(ListUtil.distinct(userIds));
        if(form != null){
            retLog.setProcessInstName(form.getTitle());;
            retLog.setCreator(userVoMap.get(form.getCreator()));
            retLog.setId(form.getId());
            retLog.setProcessInstName(form.getTitle());;
        }
        for (WfLogPo wfLogPo : wfLogPos){
            WfProcLogDetailVo logDetailVo = new WfProcLogDetailVo();
            if(!ObjectUtils.isEmpty(wfLogPo.getOptType())){
                logDetailVo.setOperateType(WfLogOptTypeEnum.getMessageByCode(wfLogPo.getOptType()));
            }
            logDetailVo.setCreateTime(wfLogPo.getCreatTime());
            logDetailVo.setEndTime(wfLogPo.getStayTime());
            logDetailVo.setWorkItemName(wfLogPo.getActivity());
            logDetailVo.setOpinion(wfLogPo.getContent());
            logDetailVo.setStayTime(DateUtil.getDatePoor(wfLogPo.getStartTime(), wfLogPo.getStayTime()));
            logDetailVo.setNextUserName(wfLogPo.getHandleUser()); ////送审至
            UserVo user = userVoMap.get(wfLogPo.getCreator());
            if(user != null){
                logDetailVo.setOperateUser(user.getName());
            }
            list.add(logDetailVo);
        }
        retLog.setWfLog(list);
        return retLog;
    }

    /**
     * 得到停留时间
     * @param wfivMap 活动实例
     * @param wfLogPo 日志
     * @return 停留时间
     */
    private String getStayTime(Map<String, WfActivityInstanceVo> wfivMap, WfLogPo wfLogPo){
        WfActivityInstanceVo actInstVo = wfivMap.get(wfLogPo.getActivityId());
        if(!Tools.isEmpty(actInstVo)){
            Date end = Tools.isEmpty(actInstVo.getEndTime()) ? new Date() : actInstVo.getEndTime();
            return DateUtil.getDatePoor(wfLogPo.getCreatTime(), end);
        }else{
            return DateUtil.getDatePoor(wfLogPo.getCreatTime(), new Date());
        }
    }

    /**
     * 得到流程的活动实例
     * @param procInstId 流程实例
     * @return 活动实例
     */
    private Map<String, WfActivityInstanceVo> getWfActivityInstance(String procInstId){
        Map<String, WfActivityInstanceVo> wfivMap = new HashMap<>();
        ApiResult<List<WfActivityInstanceVo>> result = this.commActivitiService.getActivityInstanceByProcessInstanceId(procInstId);
        if(!result.isSuccess()){
            throw new BaseException(result.getMessage());
        }
        if(!ObjectUtils.isEmpty(result.getData())){
            for(WfActivityInstanceVo waiv : result.getData()){
                wfivMap.put(waiv.getActivityId(), waiv);
            }
        }
        return wfivMap;
    }

    /**
     * 增加流程日志
     * @param wfLogAddForm
     * @return
     */
    @Override
    public WfLogVo addWfLog(WfLogAddForm wfLogAddForm){
        WfLogPo wfLogPo = dozerMapper.map(wfLogAddForm, WfLogPo.class);
        this.insert(wfLogPo);
        WfLogVo wfLogVo = dozerMapper.map(wfLogPo, WfLogVo.class);
        return wfLogVo;
    }

    /**
     * 查询流程日志
     * @param procInstId 流程实例ID
     * @return 流程日志
     */
    private List<WfLogPo> queryLogPoByProcInstId(String procInstId){
        Example example = new Example(WfLogPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("procInstId",procInstId);
        example.setOrderByClause("creat_time desc");
        List<WfLogPo> wfLogPos = this.selectByExample(example);
        return wfLogPos;
    }
}
