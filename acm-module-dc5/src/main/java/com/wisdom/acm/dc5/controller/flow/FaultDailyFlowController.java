package com.wisdom.acm.dc5.controller.flow;
import com.google.common.collect.Lists;
import com.wisdom.acm.dc5.common.SzxmCommonUtil;
import com.wisdom.acm.dc5.common.SzxmEnumsUtil;
import com.wisdom.acm.dc5.po.FaultDailyPo;
import com.wisdom.acm.dc5.service.FaultDailyService;
import com.wisdom.base.common.controller.WFController;
import com.wisdom.base.common.dc.util.EnumsUtil;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.form.WfRuningProcessForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.base.common.vo.wf.WfActivityVo;
import com.wisdom.base.common.vo.wf.WfCandidateVo;
import com.wisdom.base.common.vo.wf.WfFormDataVo;
import com.wisdom.base.common.vo.wf.WfRunProcessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("gzrk/faultDaily")
public class FaultDailyFlowController extends WFController
{
    @Autowired
    private FaultDailyService faultDailyService;


    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private CommUserService commUserService;


    @Override
    public ApiResult<WfRunProcessVo> customWorkFlowCandidate(@RequestBody WfRuningProcessForm form) {
        //查询该流程是否指定特殊筛选组  不为空时
        if (!ObjectUtils.isEmpty(form.getCandidate()) && !ObjectUtils.isEmpty(form.getCandidate().getActivities())) {
            Map<String, String> stringMap = szxmCommonUtil.getActivitiCodeMap(form);
            List<String> codeList = ListUtil.mapToList(stringMap);
            if (ObjectUtils.isEmpty(codeList)) {
                List<WfFormDataVo> fromData = form.getFormData();
                if (ObjectUtils.isEmpty(fromData)){
                    return super.customWorkFlowCandidate(form);
                }
                List<String> bizIds = ListUtil.toValueList(fromData, "bizId", String.class);
                List<Integer> ids = Lists.newArrayList();
                for (String bizId : bizIds) {
                    ids.add(Integer.valueOf(bizId));
                }
                List<WfActivityVo> activityVoList=szxmCommonUtil.filterFlowCandiateUser(form, stringMap);
                WfRunProcessVo vo = new WfRunProcessVo();
                vo.setCandidate(new WfCandidateVo().setActivities(activityVoList));
                return ApiResult.success(vo);
            } else {
                return super.customWorkFlowCandidate(form);
            }
        }
        return super.customWorkFlowCandidate(form);
    }
    /**
     * 发起流程后事件
     * @param form
     */
    @Override
    public ApiResult startWorkFlowAfter(@RequestBody WfRuningProcessForm form){
        //把业务数据的状态变更为审批中.
        List<WfFormDataVo> fromData=form.getFormData();
        List<String> bizIds = ListUtil.toValueList(fromData,"bizId",String.class);
        FaultDailyPo updatePo =new FaultDailyPo();
        updatePo.setStatus(EnumsUtil.StatusEnum.APPROVAL.getCode());
        //获取当前登录人信息
        UserInfo userInfo= commUserService.getLoginUser();
        updatePo.setInitiatorId(userInfo.getId());
        updatePo.setInitiator(userInfo.getActuName());
        updatePo.setInitTime(new Date());
        List<Integer> ids= Lists.newArrayList();
        for(String bizId:bizIds) {
            ids.add(Integer.valueOf(bizId));
        }
        faultDailyService.updateSelectiveByIds(updatePo,ids);
        return ApiResult.success();
    }

    /**
     * 完成流程事件后
     * @param form
     */
    @Override
    public ApiResult completeWorkFlowAfter(@RequestBody WfRuningProcessForm form) {
        //把业务数据的状态变更为已批准.
        List<WfFormDataVo> fromData=form.getFormData();
        List<String> bizIds = ListUtil.toValueList(fromData,"bizId",String.class);
        List<Integer> ids=Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        faultDailyService.approveFaultDailyWorkFlow(fromData.get(0).getBizType(), ids);
        return ApiResult.success();
    }

    @Override
    public ApiResult backActivityAfter(@RequestBody WfRuningProcessForm form) {

        //把业务数据的状态变更为被驳回.
        List<WfFormDataVo> fromData=form.getFormData();
        List<String> bizIds = ListUtil.toValueList(fromData,"bizId",String.class);
        FaultDailyPo updatePo =new FaultDailyPo();
        updatePo.setStatus(SzxmEnumsUtil.StatusEnum.REJECT.getCode());
        List<Integer> ids=Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        faultDailyService.updateSelectiveByIds(updatePo,ids);
        return ApiResult.success();
    }

    /**
     * 终止流程后事件
     * @param form 表单
     * @return
     */
    @Override
    public ApiResult terminateWorkFlowAfter(@RequestBody WfRuningProcessForm form)
    {
        //把业务数据的状态变更为新建.
        List<WfFormDataVo> fromData=form.getFormData();
        List<String> bizIds = ListUtil.toValueList(fromData,"bizId",String.class);
        FaultDailyPo updatePo =new FaultDailyPo();
        updatePo.setStatus(SzxmEnumsUtil.StatusEnum.INIT.getCode());
        List<Integer> ids=Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        faultDailyService.updateSelectiveByIds(updatePo,ids);
        return ApiResult.success();
    }

    /**
     * 删除流程后事件
     * @param form 表单
     * @return
     */
    @Override
    public ApiResult deleteWorkFlowAfter(@RequestBody WfRuningProcessForm form)
    {
        //把业务数据的状态变更为新建.
        List<WfFormDataVo> fromData=form.getFormData();
        List<String> bizIds = ListUtil.toValueList(fromData,"bizId",String.class);
        FaultDailyPo updatePo =new FaultDailyPo();
        updatePo.setStatus(SzxmEnumsUtil.StatusEnum.INIT.getCode());
        List<Integer> ids=Lists.newArrayList();
        for(String bizId:bizIds)  {
            ids.add(Integer.valueOf(bizId));
        }
        faultDailyService.updateSelectiveByIds(updatePo,ids);
        return ApiResult.success();
    }

    /**
     * 执行工作项事件后
     * @param form
     * @return
     */
    @Override
    public ApiResult<WfRunProcessVo> executeTaskAfter(@RequestBody WfRuningProcessForm form)
    {
        //把业务数据的状态变更为审批中
        List<WfFormDataVo> fromData=form.getFormData();
        List<String> bizIds = ListUtil.toValueList(fromData,"bizId",String.class);
        FaultDailyPo updatePo =new FaultDailyPo();
        updatePo.setStatus(SzxmEnumsUtil.StatusEnum.APPROVAL.getCode());
        List<Integer> ids=Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        faultDailyService.updateSelectiveByIds(updatePo,ids);
        return ApiResult.success();
    }
}
