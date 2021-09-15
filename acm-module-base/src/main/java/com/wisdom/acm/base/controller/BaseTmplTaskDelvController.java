package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.tmpltask.BaseTmplTaskDelvAssignForm;
import com.wisdom.acm.base.form.tmpltask.BaseTmplTaskDelvUpdateForm;
import com.wisdom.acm.base.po.BaseTmplTaskDelvPo;
import com.wisdom.acm.base.service.BaseTmplDelvService;
import com.wisdom.acm.base.service.BaseTmplTaskDelvService;
import com.wisdom.acm.base.service.BaseTmplTaskService;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskDelvVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tmpltask")
public class BaseTmplTaskDelvController extends BaseController {

    @Autowired
    private BaseTmplTaskDelvService baseTmplTaskDelvService;

    @Autowired
    private BaseTmplDelvService baseTmplDelvService;

    @Autowired
    protected org.dozer.Mapper dozerMapper;

    @Autowired
    private BaseTmplTaskService baseTmplTaskService;

    /**
     * 查询交付清单列表
     * @param taskId
     * @return
     */
    @GetMapping(value = "/{taskId}/delv/list")
    public ApiResult queryTmplTaskDelvList(@PathVariable("taskId")Integer taskId) {
        List<BaseTmplTaskDelvVo> retList = baseTmplTaskDelvService.queryTmplTaskDelvListByTaskId(taskId);
        return ApiResult.success(retList);
    }

    /**
     * 查询交付清单列表
     * @param tmplId
     * @return
     */
    @GetMapping(value = "/{tmplId}/template/delv/list")
    public ApiResult queryTmplTaskDelvListByTmplId(@PathVariable("tmplId")Integer tmplId) {
        List<BaseTmplTaskDelvVo> retList = baseTmplTaskDelvService.queryTmplTaskDelvListByTmplId(tmplId);
        return ApiResult.success(retList);
    }

    /**
     * 分配交付物
     * @param
     * @return
     */
    @PostMapping(value = "/{taskId}/delv/assign")
    @AddLog(title = "分配交付清单" , module = LoggerModuleEnum.BM_TMPL_PLAN)
    public ApiResult assignTmplTaskDelv(@PathVariable("taskId")Integer taskId, @RequestBody List<Integer> delvIds) {
        String logger = baseTmplDelvService.queryAssignTaskDelv(delvIds);
        String taskName = baseTmplTaskService.selectById(taskId).getTaskName();
        this.setAcmLogger(new AcmLogger("任务或WBS\""+taskName+"\""+logger));
        List<BaseTmplTaskDelvVo> baseTmplTaskDelvPoList = baseTmplTaskDelvService.assignTmplTaskDelv(taskId,delvIds);
        return ApiResult.success(baseTmplTaskDelvPoList);
    }

    /**
     * 更新交付物
     * @param baseTmplTaskDelvUpdateForm
     * @return
     */
    /*@PutMapping(value = "/delv/update")
    public ApiResult updateTmplTaskDelv(@RequestBody @Valid BaseTmplTaskDelvUpdateForm baseTmplTaskDelvUpdateForm) {
        BaseTmplTaskDelvPo baseTmplTaskDelvPo = baseTmplTaskDelvService.updateTmplTaskDelv(baseTmplTaskDelvUpdateForm);
        BaseTmplTaskDelvVo baseTmplTaskDelvVo = dozerMapper.map(baseTmplTaskDelvPo, BaseTmplTaskDelvVo.class);
        return ApiResult.success(baseTmplTaskDelvVo);
    }*/

    /**
     * 删除交付清单
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/delv/delete")
    @AddLog(title = "删除交付清单分配",module = LoggerModuleEnum.BM_TMPL_PLAN)
    public ApiResult deleteTmplTaskDelv(@RequestBody List<Integer> ids) {
        String names = baseTmplTaskDelvService.queryBaseTmplTaskDelvByIds(ids);
        this.setAcmLogger(new AcmLogger("批量删除交付清单分配，交付清单名称如下："+names));
        baseTmplTaskDelvService.deleteBaseTmplTaskDelv(ids);
        return ApiResult.success();
    }

    /**
     * 查询交付清单的基本信息
     * @param id
     * @return
     */
    @GetMapping(value = "/delv/{id}/info")
    public ApiResult queryGbTypeInfo(@PathVariable("id")int id) {
        BaseTmplTaskDelvVo baseTmplTaskDelvVo = baseTmplTaskDelvService.queryBaseTmplTaskDelvInfo(id);
        return ApiResult.success(baseTmplTaskDelvVo);
    }
}
