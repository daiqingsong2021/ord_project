package com.wisdom.acm.wf.controller;

import com.wisdom.acm.wf.service.WfFormDataService;
import com.wisdom.base.common.vo.wf.WfProcessBizVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WfFormDataController {

    @Autowired
    private WfFormDataService wfFormDataService;
    /**
     * 查询业务ID集合
     * @param procInstId
     * @return
     */
    @GetMapping("/form/data/{procInstId}/list")
    public List<WfProcessBizVo> queryWfFormDataBizIds(@PathVariable("procInstId") Integer procInstId){
        //通过流程实例ID查询业务ID集合
        List<WfProcessBizVo> processBizVos = wfFormDataService.queryWfFormDataBizIds(procInstId);
        return processBizVos;
    }
}
