package com.wisdom.acm.wf.controller;

import com.wisdom.acm.wf.service.WfDelegateProcService;
import com.wisdom.acm.wf.vo.WfBizTypeVo;
import com.wisdom.acm.wf.vo.WfDelegateProcVo;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class WfDelegateProcController {

    @Autowired
    private WfDelegateProcService wfDelegateProcService;

    /**
     * 分配需要代理的流程业务
     * @param map
     * @return
     */
    @PostMapping(value = "/delegate/proc/list")
    public ApiResult wfDelegateProc(@RequestBody Map<String,Object> map) {
        Integer delegateId = Integer.parseInt(map.get("delegateId").toString());
        List<String> bizTypeCodes = (List)map.get("bizTypeCodes");
        wfDelegateProcService.wfDelegateProc(delegateId,bizTypeCodes);
        return ApiResult.success();
    }

    /**
     * 查询已代理的流程业务
     * @param delegateId
     * @return
     */
    @GetMapping(value = "/delegate/assigned/proc/{delegateId}/list")
    public ApiResult queryDelegateProcList(@PathVariable("delegateId") Integer delegateId){
        List<WfDelegateProcVo> list = wfDelegateProcService.queryDelegateProcList(delegateId);
        return ApiResult.success(list);
    }

    /**
     * 流程业务表单
     * @param delegateId
     * @return
     */
    @GetMapping(value = "/delegate/{delegateId}/list")
    public ApiResult queryDelegateWfTypeList (@PathVariable("delegateId") Integer delegateId){
        List<WfBizTypeVo> list = wfDelegateProcService.queryDelegateWfTypeList(delegateId);
        return ApiResult.success(list);
    }

    /**
     * 删除已代理的流程业务
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/delegate/{ids}/delete")
    public ApiResult deleteAssignedWfType(@PathVariable("ids") List<Integer> ids){
        wfDelegateProcService.deleteDelegateProc(ids);
        return ApiResult.success();
    }

}
