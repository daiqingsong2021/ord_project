package com.wisdom.acm.wf.controller;

import com.wisdom.acm.wf.form.WfDelegateAddForm;
import com.wisdom.acm.wf.form.WfDelegateUpdateForm;
import com.wisdom.acm.wf.po.WfDelegatePo;
import com.wisdom.acm.wf.service.WfDelegateService;
import com.wisdom.acm.wf.vo.WfDelegateVo;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class WfDelegateController {

    @Autowired
    private WfDelegateService wfDelegateService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 查询流程代理
     * @return
     */
    @GetMapping(value = "/delegate/list")
    public ApiResult queryWfDelegateList(){
        Integer userId = Integer.valueOf(request.getHeader("userId"));
        List<WfDelegateVo> list = wfDelegateService.queryWfDelegateByUserId(userId);

        return ApiResult.success(list);
    }

    /**
     * 增加流程代理
     * @param addForm
     * @return
     */
    @PostMapping(value = "/delegate/add")
    public ApiResult addWfDelegate(@RequestBody WfDelegateAddForm addForm){
        WfDelegatePo wfDelegatePo = wfDelegateService.addWfDelegate(addForm);
        WfDelegateVo wfDelegateVo = wfDelegateService.getWfDelegateInfo(wfDelegatePo.getId());

        return ApiResult.success(wfDelegateVo);
    }

    @GetMapping(value = "/delegate/{id}/info")
    public ApiResult getWfDelegateInfo(@PathVariable("id") Integer id){
        WfDelegateVo wfDelegateVo = wfDelegateService.getWfDelegateInfo(id);

        return ApiResult.success(wfDelegateVo);
    }

    /**
     * 修改流程代理
     * @param updateForm
     * @return
     */
    @PutMapping(value = "/delegate/update")
    public ApiResult updateDelegate(@RequestBody WfDelegateUpdateForm updateForm){
        WfDelegatePo wfDelegatePo = wfDelegateService.updateWfDelegate(updateForm);
        WfDelegateVo wfDelegateVo = wfDelegateService.getWfDelegateInfo(wfDelegatePo.getId());

        return ApiResult.success(wfDelegateVo);
    }

    /**
     * 删除流程代理
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/delegate/delete")
    public ApiResult delegateWfDelegate(@RequestBody List<Integer> ids){
        wfDelegateService.deleteWfDelegate(ids);

        return ApiResult.success();
    }
}
