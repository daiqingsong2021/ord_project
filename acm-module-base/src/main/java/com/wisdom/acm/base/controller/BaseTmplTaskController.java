package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.tmpltask.*;
import com.wisdom.acm.base.po.BaseTmplPlanPo;
import com.wisdom.acm.base.po.BaseTmplTaskPo;
import com.wisdom.acm.base.service.BaseTmplPlanService;
import com.wisdom.acm.base.service.BaseTmplTaskService;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplPlanVo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskTreeVo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tmpltask")
public class BaseTmplTaskController extends BaseController {

    @Autowired
    private BaseTmplTaskService baseTmplTaskService;

    @Autowired
    private BaseTmplPlanService baseTmplPlanService;

    @Autowired
    protected org.dozer.Mapper dozerMapper;

    @Autowired
    private CommUserService userService;

    /**
     * 查询树形结构
     * @return
     */
    @GetMapping(value = "/tree")
    public ApiResult queryTmplTaskTreeList() {
        List<BaseTmplTaskTreeVo> retList = baseTmplTaskService.queryTmpltaskTreeList();
        return ApiResult.success(retList);
    }

    /**
     * 查询计划模板下的所有
     * @return
     */
    @GetMapping(value = "/{tmplId}/tree")
    public ApiResult queryTmplTaskTreeList(@PathVariable("tmplId")Integer tmplId) {
        List<BaseTmplTaskTreeVo> retList = baseTmplTaskService.queryTmpltaskTreeListByTmplId(tmplId);
        return ApiResult.success(retList);
    }

    /**
     * 增加计划模板
     * @param tmplPlanAddForm
     * @return
     */
    @AddLog(title = "增加计划模板",module = LoggerModuleEnum.BM_TMPL_PLAN)
    @PostMapping(value = "/add")
    public ApiResult addTmplPlan(@RequestBody @Valid BaseTmplPlanAddForm tmplPlanAddForm) {
        this.setAcmLogger(new AcmLogger("增加计划模板，名称为："+tmplPlanAddForm.getTmplName()));
        BaseTmplPlanPo baseTmplPlanPo = baseTmplPlanService.addTmplPlan(tmplPlanAddForm);
        BaseTmplPlanVo baseTmplPlanVo = baseTmplPlanService.getTmplPlanInfoById(baseTmplPlanPo.getId());
        baseTmplPlanVo.setType("tmpl");
        return ApiResult.success(baseTmplPlanVo);
    }

    /**
     * 增加计划模板wbs
     * @param tmplTaskAddForm
     * @return
     */
    @AddLog(title = "增加计划模板WBS",module = LoggerModuleEnum.BM_TMPL_PLAN)
    @PostMapping(value = "/wbs/add")
    public ApiResult addTmplWbs(@RequestBody @Valid BaseTmplTaskAddForm tmplTaskAddForm) {
        this.setAcmLogger(new AcmLogger("增加计划模板WBS，名称为："+tmplTaskAddForm.getTaskName()));
        BaseTmplTaskPo baseTmplTaskPo = baseTmplTaskService.addTmplWbs(tmplTaskAddForm);
        BaseTmplTaskVo baseTmplTaskVo = baseTmplTaskService.getTmplTaskById(baseTmplTaskPo.getId());
        baseTmplTaskVo.setType("wbs");
        return ApiResult.success(baseTmplTaskVo);
    }

    /**
     * 增加计划模板task
     * @param tmplTaskAddForm
     * @return
     */
    @AddLog(title = "增加计划模板任务",module = LoggerModuleEnum.BM_TMPL_PLAN)
    @PostMapping(value = "/task/add")
    public ApiResult addTmplTask(@RequestBody @Valid BaseTmplTaskAddForm tmplTaskAddForm) {
        this.setAcmLogger(new AcmLogger("增加计划模板任务，名称为："+tmplTaskAddForm.getTaskName()));
        BaseTmplTaskPo baseTmplTaskPo = baseTmplTaskService.addTmplTask(tmplTaskAddForm);
        BaseTmplTaskVo baseTmplTaskVo = baseTmplTaskService.getTmplTaskById(baseTmplTaskPo.getId());
        baseTmplTaskVo.setType("task");
        return ApiResult.success(baseTmplTaskVo);
    }

    /**
     * 更新计划模板信息
     * @param tmplPlanUpdateForm
     * @return
     */
    @PutMapping(value = "/update")
    public ApiResult updateTmplPlan(@RequestBody @Valid BaseTmplPlanUpdateForm tmplPlanUpdateForm) {
        BaseTmplPlanPo baseTmplPlanPo = baseTmplPlanService.updateTmplPlan(tmplPlanUpdateForm);
        BaseTmplPlanVo baseTmplPlanVo = baseTmplPlanService.getTmplPlanInfoById(baseTmplPlanPo.getId());
        baseTmplPlanVo.setType("tmpl");
        return ApiResult.success(baseTmplPlanVo);
    }

    /**
     * 更新计划模板wbs
     * @param tmplTaskUpdateForm
     * @return
     */
    @PutMapping(value = "/wbs/update")
    public ApiResult updateTmplWbs(@RequestBody @Valid BaseTmplTaskUpdateForm tmplTaskUpdateForm) {
        BaseTmplTaskPo baseTmplTaskPo = baseTmplTaskService.updateTmplWbs(tmplTaskUpdateForm);
        BaseTmplTaskVo baseTmplTaskVo = baseTmplTaskService.getTmplTaskById(baseTmplTaskPo.getId());
        baseTmplTaskVo.setType("wbs");
        return ApiResult.success(baseTmplTaskVo);
    }

    /**
     * 更新计划模板task
     * @param tmplTaskUpdateForm
     * @return
     */
    @PutMapping(value = "/task/update")
    public ApiResult updateTmplTask(@RequestBody @Valid BaseTmplTaskUpdateForm tmplTaskUpdateForm) {
        BaseTmplTaskPo baseTmplTaskPo = baseTmplTaskService.updateTmplTask(tmplTaskUpdateForm);
        BaseTmplTaskVo baseTmplTaskVo = baseTmplTaskService.getTmplTaskById(baseTmplTaskPo.getId());
        baseTmplTaskVo.setType("task");
        return ApiResult.success(baseTmplTaskVo);
    }

    /**
     * 查询计划模板信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}/info")
    public ApiResult queryTmplPlanInfo(@PathVariable("id")int id) {
        BaseTmplPlanVo baseTmplPlanVo = baseTmplPlanService.getTmplPlanInfoById(id);
        baseTmplPlanVo.setType("tmpl");
        return ApiResult.success(baseTmplPlanVo);
    }

    /**
     * 查询计划模板wbs
     * @param id
     * @return
     */
    @GetMapping(value = "/wbs/{id}/info")
    public ApiResult queryTmplWbsInfo(@PathVariable("id")int id) {
        BaseTmplTaskVo baseTmplTaskVo = baseTmplTaskService.getTmplTaskById(id);
        baseTmplTaskVo.setType("wbs");
        return ApiResult.success(baseTmplTaskVo);
    }

    /**
     * 查询计划模板task
     * @param id
     * @return
     */
    @GetMapping(value = "/task/{id}/info")
    public ApiResult queryTmplTaskInfo(@PathVariable("id")int id) {
        BaseTmplTaskVo baseTmplTaskVo = baseTmplTaskService.getTmplTaskById(id);
        baseTmplTaskVo.setType("task");
        return ApiResult.success(baseTmplTaskVo);
    }

    /**
     * 删除计划模板
     * @param ids
     * @return
     */
    @AddLog(title = "删除计划模板",module = LoggerModuleEnum.BM_TMPL_PLAN)
    @DeleteMapping(value = "/delete")
    public ApiResult deleteTmplPlan(@RequestBody List<Integer> ids) {
        String tmplNames = baseTmplPlanService.queryNamesByIds(ids,"tmplName");
        this.setAcmLogger(new AcmLogger("批量删除计划模板及其子节点，名称如下："+tmplNames));
        baseTmplTaskService.deleteBaseTmplPlan(ids);
        return ApiResult.success();
    }

    /**
     * 删除计划模板
     * @param tmplId
     * @return
     */
    @AddLog(title = "删除计划模板",module = LoggerModuleEnum.BM_TMPL_PLAN)
    @DeleteMapping(value = "/{tmplId}/delete")
    public ApiResult deleteTmplPlanById(@PathVariable("tmplId")Integer tmplId) {
        List<Integer> ids = new ArrayList<>();
        ids.add(tmplId);
        String taskNames = baseTmplPlanService.queryNamesByIds(ids,"tmplName");
        this.setAcmLogger(new AcmLogger("删除计划模板及其子节点，名称如下："+taskNames));
        baseTmplTaskService.deleteTmplPlanById(tmplId);
        return ApiResult.success();
    }

    /**
     * 删除计划模板wbs
     * @param ids
     * @return
     */
    @AddLog(title = "删除计划模板",module = LoggerModuleEnum.BM_TMPL_PLAN)
    @DeleteMapping(value = "/wbs/delete")
    public ApiResult deleteTmplWbs(@RequestBody List<Integer> ids) {
        String taskNames = baseTmplTaskService.queryNamesByIds(ids,"taskName");
        this.setAcmLogger(new AcmLogger("批量删除计划模板WBS及其子节点，名称如下："+taskNames));
        baseTmplTaskService.deleteBaseTmplTask(ids);
        return ApiResult.success();
    }

    /**
     * 删除计划模板wbs
     * @param
     * @return
     */
    @AddLog(title = "删除计划模板",module = LoggerModuleEnum.BM_TMPL_PLAN)
    @DeleteMapping(value = "/wbs/{wbsId}/delete")
    public ApiResult deleteTmplWbsByWbsId(@PathVariable("wbsId")Integer wbsId) {
        List<Integer> ids = new ArrayList<>();
        ids.add(wbsId);
        String taskNames = baseTmplTaskService.queryNamesByIds(ids,"taskName");
        this.setAcmLogger(new AcmLogger("删除计划模板WBS及其子节点，名称如下："+taskNames));
        baseTmplTaskService.deleteTmplWbsByWbsId(wbsId);
        return ApiResult.success();
    }

    /**
     * 删除计划模板task
     * @param ids
     * @return
     */
    @AddLog(title = "删除计划模板",module = LoggerModuleEnum.BM_TMPL_PLAN)
    @DeleteMapping(value = "/task/delete")
    public ApiResult deleteTmplTask(@RequestBody List<Integer> ids) {
        String taskNames = baseTmplTaskService.queryNamesByIds(ids,"taskName");
        this.setAcmLogger(new AcmLogger("批量删除计划模板任务及其子节点，名称如下："+taskNames));
        baseTmplTaskService.deleteBaseTmplTask(ids);
        return ApiResult.success();
    }

    /**
     * 删除计划模板task
     * @param
     * @return
     */
    @AddLog(title = "删除计划模板",module = LoggerModuleEnum.BM_TMPL_PLAN)
    @DeleteMapping(value = "/task/{taskId}/delete")
    public ApiResult deleteTmplWbsByTaskId(@PathVariable("taskId")Integer taskId) {
        List<Integer> ids = new ArrayList<>();
        ids.add(taskId);
        String taskNames = baseTmplTaskService.queryNamesByIds(ids,"taskName");
        this.setAcmLogger(new AcmLogger("删除计划模板任务及其子节点，名称如下："+taskNames));
        baseTmplTaskService.deleteTmplWbsByWbsId(taskId);
        return ApiResult.success();
    }

    /**
     * 导出为计划模版
     * @param taskTmplAddForm
     * @return
     */
    @PostMapping("/define/tmpl/task/add")
    public ApiResult addPlanTaskTmplByDefineId(@RequestBody @Valid TaskTmplAddForm taskTmplAddForm){
        this.baseTmplTaskService.savePlanTaskTmplByDefineId(taskTmplAddForm,userService.getLoginUser());
        return ApiResult.success();
    }
}
