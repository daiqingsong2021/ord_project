package com.wisdom.acm.processing.controller.flow;

import com.google.common.collect.Lists;
import com.wisdom.acm.processing.po.report.FineDailyReportPo;
import com.wisdom.acm.processing.service.report.FineDailyReportService;
import com.wisdom.base.common.controller.WFController;
import com.wisdom.base.common.dc.util.EnumsUtil;
import com.wisdom.base.common.form.WfRuningProcessForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.wf.WfFormDataVo;
import com.wisdom.base.common.vo.wf.WfRunProcessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zll
 * 2020/12/24/024 16:43
 * Description:<帆软报表流程审批>
 */
@RestController
@RequestMapping("fineDailyReport/flow")
public class FineDailyReportFlowController extends WFController {
    @Autowired
    private FineDailyReportService fineDailyReportService;
    @Override
    public ApiResult<WfRunProcessVo> customWorkFlowCandidate(@RequestBody WfRuningProcessForm form) {
        return super.customWorkFlowCandidate(form);
    }

    /**
     * 发起流程后事件
     * @param form
     */
    @Override
    public ApiResult startWorkFlowAfter(@RequestBody WfRuningProcessForm form)
    {
        //把业务数据的状态变更为审批中
        List<WfFormDataVo> fromData=form.getFormData();
        List<String> bizIds = ListUtil.toValueList(fromData,"bizId",String.class);
        FineDailyReportPo updatePo =new FineDailyReportPo();
        updatePo.setReviewStatus(EnumsUtil.StatusEnum.APPROVAL.getCode());
        List<Integer> ids= Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        fineDailyReportService.updateSelectiveByIds(updatePo,ids);
        return ApiResult.success();
    }

    /**
     * 完成流程事件后
     * @param form
     */
    @Override
    public ApiResult completeWorkFlowAfter(@RequestBody WfRuningProcessForm form)
    {
        //把业务数据的状态变更为已批准.
        List<WfFormDataVo> fromData=form.getFormData();
        List<String> bizIds = ListUtil.toValueList(fromData,"bizId",String.class);
        List<Integer> ids=Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        fineDailyReportService.approveFineDailyReportFlow(fromData.get(0).getBizType(),ids);
        return ApiResult.success();
    }

    /**
     * 驳回
     * @param form
     * @return
     */
    @Override
    public ApiResult backActivityAfter(@RequestBody WfRuningProcessForm form)
    {
        //把业务数据的状态变更为被驳回.
        List<WfFormDataVo> fromData=form.getFormData();
        List<String> bizIds = ListUtil.toValueList(fromData,"bizId",String.class);
        FineDailyReportPo updatePo=new FineDailyReportPo();
        updatePo.setReviewStatus(EnumsUtil.StatusEnum.REJECT.getCode());
        List<Integer> ids= Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        fineDailyReportService.updateSelectiveByIds(updatePo,ids);
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
        FineDailyReportPo updatePo=new FineDailyReportPo();
        updatePo.setReviewStatus(EnumsUtil.StatusEnum.INIT.getCode());
        List<Integer> ids=Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        fineDailyReportService.updateSelectiveByIds(updatePo,ids);
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
        FineDailyReportPo updatePo=new FineDailyReportPo();
        updatePo.setReviewStatus(EnumsUtil.StatusEnum.INIT.getCode());
        List<Integer> ids=Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        fineDailyReportService.updateSelectiveByIds(updatePo,ids);
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
        FineDailyReportPo updatePo =new FineDailyReportPo();
        updatePo.setReviewStatus(EnumsUtil.StatusEnum.APPROVAL.getCode());
        List<Integer> ids=Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        fineDailyReportService.updateSelectiveByIds(updatePo,ids);
        return ApiResult.success();
    }
}
