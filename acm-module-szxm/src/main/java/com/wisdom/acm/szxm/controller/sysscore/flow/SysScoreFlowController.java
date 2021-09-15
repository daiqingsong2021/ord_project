package com.wisdom.acm.szxm.controller.sysscore.flow;

/**
 * Author：wqd
 * Date：2020-01-06 16:16
 * Description：<描述>
 */

import com.google.common.collect.Lists;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.common.SzxmEnumsUtil;
import com.wisdom.acm.szxm.po.sysscore.SysScorePo;
import com.wisdom.acm.szxm.service.sysscore.SysScoreService;
import com.wisdom.base.common.controller.WFController;
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

/**
 * 系统评分流程控制器
 */
@RestController
@RequestMapping("system/score")
public class SysScoreFlowController extends WFController {
    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private SysScoreService sysScoreService;

    @Autowired
    private CommUserService commUserService;
    @Override
    public ApiResult<WfRunProcessVo> customWorkFlowCandidate(@RequestBody WfRuningProcessForm form) {
        //查询该流程是否指定特殊筛选组  不为空时
        if (!ObjectUtils.isEmpty(form.getCandidate()) && !ObjectUtils.isEmpty(form.getCandidate().getActivities())) {
            Map<String, String> stringMap = szxmCommonUtil.getActivitiCodeMap(form);
            List<String> codeList = ListUtil.mapToList(stringMap);
            Integer sectionId = null;
            Integer projectId = null;
            if (!ObjectUtils.isEmpty(codeList)) {
                if (codeList.contains("section") || codeList.contains("project")) {
                    List<WfFormDataVo> fromData = form.getFormData();
                    if (ObjectUtils.isEmpty(fromData)){
                        return super.customWorkFlowCandidate(form);
                    }
                    List<String> bizIds = ListUtil.toValueList(fromData, "bizId", String.class);
                    List<Integer> ids = Lists.newArrayList();
                    for (String bizId : bizIds) {
                        ids.add(Integer.valueOf(bizId));
                    }
                    List<SysScorePo> sysScorePos=sysScoreService.selectByIds(ids);
                    if (!ObjectUtils.isEmpty(sysScorePos)) {
                        //根据code选择传入标段id 或项目id
                        sectionId = sysScorePos.get(0).getSectionId();
                        projectId = sysScorePos.get(0).getProjectId();
                    }
                }

                List<WfActivityVo> activityVoList=szxmCommonUtil.filterFlowCandiateUser(form, stringMap, sectionId, projectId);

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
    public ApiResult startWorkFlowAfter(@RequestBody WfRuningProcessForm form) {
        //把业务数据的状态变更为审批中.
        List<WfFormDataVo> fromData=form.getFormData();
        List<String> bizIds = ListUtil.toValueList(fromData,"bizId",String.class);
        SysScorePo sysScorePo =new SysScorePo();
        sysScorePo.setStatus(SzxmEnumsUtil.StatusEnum.APPROVAL.getCode());
        //获取当前登录人信息
        UserInfo userInfo= commUserService.getLoginUser();
        sysScorePo.setInitiatorId(userInfo.getId());
        sysScorePo.setInitTime(new Date());
        List<Integer> ids= Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        sysScoreService.updateSelectiveByIds(sysScorePo,ids);
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
        sysScoreService.approveQuaConceFlow(fromData.get(0).getBizType(), ids);
        return ApiResult.success();
    }

    @Override
    public ApiResult backActivityAfter(@RequestBody WfRuningProcessForm form) {

        //把业务数据的状态变更为被驳回.
        List<WfFormDataVo> fromData=form.getFormData();
        List<String> bizIds = ListUtil.toValueList(fromData,"bizId",String.class);
        SysScorePo sysScorePo =new SysScorePo();
        sysScorePo.setStatus(SzxmEnumsUtil.StatusEnum.REJECT.getCode());
        List<Integer> ids=Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        sysScoreService.updateSelectiveByIds(sysScorePo,ids);
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
        SysScorePo sysScorePo =new SysScorePo();
        sysScorePo.setStatus(SzxmEnumsUtil.StatusEnum.INIT.getCode());
        List<Integer> ids=Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        sysScoreService.updateSelectiveByIds(sysScorePo,ids);
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
        SysScorePo sysScorePo =new SysScorePo();
        sysScorePo.setStatus(SzxmEnumsUtil.StatusEnum.INIT.getCode());
        List<Integer> ids= Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        sysScoreService.updateSelectiveByIds(sysScorePo,ids);
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
        SysScorePo sysScorePo =new SysScorePo();
        sysScorePo.setStatus(SzxmEnumsUtil.StatusEnum.APPROVAL.getCode());
        List<Integer> ids=Lists.newArrayList();
        for(String bizId:bizIds)
        {
            ids.add(Integer.valueOf(bizId));
        }
        sysScoreService.updateSelectiveByIds(sysScorePo,ids);
        return ApiResult.success();
    }
}
