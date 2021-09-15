package com.wisdom.acm.base.controller;

import com.wisdom.acm.base.form.tmpltask.BaseTmplTaskPredUpdateForm;
import com.wisdom.acm.base.po.BaseTmplTaskPo;
import com.wisdom.acm.base.po.BaseTmplTaskPredPo;
import com.wisdom.acm.base.service.BaseTmplTaskPredService;
import com.wisdom.acm.base.service.BaseTmplTaskService;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskPredVo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskTreeVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tmpltask")
public class BaseTmplTaskPredController extends BaseController {

    @Autowired
    private BaseTmplTaskPredService baseTmplTaskPredService;

    @Autowired
    protected org.dozer.Mapper dozerMapper;

    @Autowired
    private BaseTmplTaskService baseTmplTaskService;

    /**
     * 查询紧前任务
     * @param tmplId
     * @return
     */
    @GetMapping(value = "/{tmplId}/relation/list")
    public ApiResult queryTmplTaskPredListByTmplId(@PathVariable("tmplId")Integer tmplId) {
        List<BaseTmplTaskPredVo> retList = baseTmplTaskPredService.queryTmplTaskPredListByTmplId(tmplId);
        return ApiResult.success(retList);
    }

    /**
     * 查询紧前任务
     * @param taskId
     * @return
     */
    @GetMapping(value = "/{taskId}/relation/pred/list")
    public ApiResult queryTmplTaskPredList(@PathVariable("taskId")Integer taskId) {
        List<BaseTmplTaskPredVo> retList = baseTmplTaskPredService.queryTmplTaskPredList(taskId);
        return ApiResult.success(retList);
    }

    /**
     * 查询后续任务
     * @param taskId
     * @return
     */
    @GetMapping(value = "/{taskId}/relation/follow/list")
    public ApiResult queryTmplTaskFllowList(@PathVariable("taskId")Integer taskId) {
        List<BaseTmplTaskPredVo> retList = baseTmplTaskPredService.queryTmplTaskFllowList(taskId);
        return ApiResult.success(retList);
    }

    /**
     * 分配紧前任务
     * @param
     * @return
     */
    @PostMapping(value = "/{taskId}/relation/assign/pred/{predTaskId}")
    @AddLog(title = "分配紧前任务" , module = LoggerModuleEnum.BM_TMPL_PLAN)
    public ApiResult assignBaseTmplTaskPred(@PathVariable("taskId")Integer taskId, @PathVariable("predTaskId")Integer predTaskId) {
        List<BaseTmplTaskPredVo> BaseTmplTaskPredVoLista =  baseTmplTaskPredService.queryTmpltaskTreeListByTwoIds(taskId,predTaskId);
        if(!ObjectUtils.isEmpty(BaseTmplTaskPredVoLista)&&BaseTmplTaskPredVoLista.size()>0){
            throw new BaseException("该任务已分配！");
        }
        if(ObjectUtils.isEmpty(predTaskId)){
            throw new BaseException("请选择前置任务!");
        }
        BaseTmplTaskPo baseTmplTaskPo = baseTmplTaskService.selectById(taskId);
        List<BaseTmplTaskPo> taskPos =  baseTmplTaskService.selectByIds(ListUtil.union(ListUtil.toArrayList(taskId),ListUtil.toArrayList(predTaskId)));
        Map<Integer,BaseTmplTaskPo> taskPoMap = ListUtil.listToMap(taskPos);
        BaseTmplTaskPo taskPo = taskPoMap.get(taskId);
        if("0".equals(taskPo.getTaskType())){
            throw new BaseException("WBS不能分配逻辑关系!");
        }
        if(taskId.equals(predTaskId)){
            throw new BaseException("当前节点不能分配当前节点!");
        }
        BaseTmplTaskPredPo baseTmplTaskPredPo = baseTmplTaskPredService.assignBaseTmplTaskPred(taskId,predTaskId);
        //BaseTmplTaskPredVo BaseTmplTaskPredVo = dozerMapper.map(baseTmplTaskPredPo, BaseTmplTaskPredVo.class);
        List<BaseTmplTaskPredVo> BaseTmplTaskPredVoList =  baseTmplTaskPredService.queryTmpltaskTreeListByTwoIds(taskId,predTaskId);
        BaseTmplTaskPredVo baseTmplTaskPredVo = BaseTmplTaskPredVoList.get(0);
        this.setAcmLogger(new AcmLogger("分配逻辑关系，【"+taskPo.getTaskName()+"】分配前紧任务：【" + baseTmplTaskPredVo.getTaskName() + "】"));
        return ApiResult.success(baseTmplTaskPredVo);

    }

    /**
     * 分配后续任务
     * @param
     * @return
     */
    @PostMapping(value = "/{taskId}/relation/assign/follow/{followTaskId}")
    @AddLog(title = "分配后续任务" , module = LoggerModuleEnum.BM_TMPL_PLAN)
    public ApiResult assignBaseTmplTaskFollow(@PathVariable("taskId")Integer taskId, @PathVariable("followTaskId")Integer followTaskId) {
        List<BaseTmplTaskPredVo> BaseTmplTaskPredVoLista =  baseTmplTaskPredService.queryTmpltaskTreeListByTwoIds(followTaskId,taskId);
        if(!ObjectUtils.isEmpty(BaseTmplTaskPredVoLista)&&BaseTmplTaskPredVoLista.size()>0){
            throw new BaseException("该任务已分配！");
        }
        if(ObjectUtils.isEmpty(followTaskId)){
            throw new BaseException("请选择后置任务!");
        }
        BaseTmplTaskPo baseTmplTaskPo = baseTmplTaskService.selectById(taskId);
        List<BaseTmplTaskPo> taskPos =  baseTmplTaskService.selectByIds(ListUtil.union(ListUtil.toArrayList(taskId),ListUtil.toArrayList(followTaskId)));
        Map<Integer,BaseTmplTaskPo> taskPoMap = ListUtil.listToMap(taskPos);
        BaseTmplTaskPo taskPo = taskPoMap.get(taskId);
        if("0".equals(taskPo.getTaskType())){
            throw new BaseException("WBS不能分配逻辑关系!");
        }
        if(taskId.equals(followTaskId)){
            throw new BaseException("当前节点不能分配当前节点!");
        }
        BaseTmplTaskPredPo baseTmplTaskPredPo = baseTmplTaskPredService.assignBaseTmplTaskPred(followTaskId,taskId);
        //BaseTmplTaskPredVo BaseTmplTaskPredVo = dozerMapper.map(baseTmplTaskPredPo, BaseTmplTaskPredVo.class);
        List<BaseTmplTaskPredVo> BaseTmplTaskPredVoList =  baseTmplTaskPredService.queryFollowTmpltaskTreeListByTwoIds(followTaskId,taskId);
        BaseTmplTaskPredVo baseTmplTaskPredVo = BaseTmplTaskPredVoList.get(0);
        this.setAcmLogger(new AcmLogger("分配逻辑关系，【"+taskPo.getTaskName()+"】分配后续任务：【" + baseTmplTaskPredVo.getTaskName() + "】"));
        return ApiResult.success(baseTmplTaskPredVo);

    }


    /**
     * 修改逻辑关系
     * @param baseTmplTaskPredUpdateForm
     * @return
     */
    @PutMapping(value = "/relation/update")
    public ApiResult updateBaseTmplTaskPred(@RequestBody @Valid BaseTmplTaskPredUpdateForm baseTmplTaskPredUpdateForm) {
        BaseTmplTaskPredPo baseTmplTaskPredPo = baseTmplTaskPredService.updateBaseTmplTaskPred(baseTmplTaskPredUpdateForm);
        //BaseTmplTaskPredVo BaseTmplTaskPredVo = dozerMapper.map(baseTmplTaskPredPo, BaseTmplTaskPredVo.class);
        BaseTmplTaskPredVo BaseTmplTaskPredVo = baseTmplTaskPredService.queryTmplTaskPredById(baseTmplTaskPredPo.getId());
        return ApiResult.success(BaseTmplTaskPredVo);
    }

    /**
     * 删除逻辑关系
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/relation/delete")
    @AddLog(title = "删除逻辑关系" , module = LoggerModuleEnum.BM_TMPL_PLAN)
    public ApiResult deleteBaseTmplTaskPred(@RequestBody List<Integer> ids) {
        String logger = baseTmplTaskPredService.queryDeletePredTaskLogger(ids);
        this.setAcmLogger(new AcmLogger(logger));
        baseTmplTaskPredService.deleteBaseTmplTaskPred(ids);
        return ApiResult.success();
    }
}
