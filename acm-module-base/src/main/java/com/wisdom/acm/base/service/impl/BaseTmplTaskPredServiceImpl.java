package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.form.tmpltask.BaseTmplTaskPredUpdateForm;
import com.wisdom.acm.base.mapper.BaseTmplTaskPredMapper;
import com.wisdom.acm.base.po.BaseTmplTaskPo;
import com.wisdom.acm.base.po.BaseTmplTaskPredPo;
import com.wisdom.acm.base.service.BaseTmplTaskPredService;
import com.wisdom.acm.base.service.BaseTmplTaskService;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskPredVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.plan.task.pred.PlanTmplTaskPredForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseTmplTaskPredServiceImpl extends BaseService<BaseTmplTaskPredMapper, BaseTmplTaskPredPo> implements BaseTmplTaskPredService {

    @Autowired
    private BaseTmplTaskService baseTmplTaskService;

    @Override
    public List<BaseTmplTaskPredVo> queryTmplTaskPredListByTmplId(Integer tmplId){
        return this.mapper.selectTmplTaskPredListByTmplId(tmplId);
    }

    @Override
    public List<BaseTmplTaskPredVo> queryTmplTaskPredList(Integer taskId) {
        return this.mapper.selectTmplTaskPredList(taskId);
    }

    @Override
    public List<BaseTmplTaskPredVo> queryTmplTaskFllowList(Integer taskId) {
        return this.mapper.selectTmplTaskFllowList(taskId);
    }

    @Override
    public List<BaseTmplTaskPredVo> queryTmpltaskTreeListByTwoIds(Integer taskId, Integer predTaskId) {
        Map<String, Integer> map = new HashMap<>();
        map.put("taskId",taskId);
        map.put("predTaskId",predTaskId);
        return this.mapper.queryTmpltaskTreeListByTwoIds(map);
    }

    @Override
    public List<BaseTmplTaskPredVo> queryFollowTmpltaskTreeListByTwoIds(Integer taskId, Integer predTaskId) {
        Map<String, Integer> map = new HashMap<>();
        map.put("taskId",taskId);
        map.put("predTaskId",predTaskId);
        return this.mapper.queryFollowTmpltaskTreeListByTwoIds(map);
    }



    @Override
    public BaseTmplTaskPredPo assignBaseTmplTaskPred(Integer taskId, Integer predTaskId) {
        BaseTmplTaskPredPo baseTmplTaskPredPo = new BaseTmplTaskPredPo();
        baseTmplTaskPredPo.setTaskId(taskId);
        baseTmplTaskPredPo.setPredTaskId(predTaskId);
        baseTmplTaskPredPo.setRelationType("FS");
        baseTmplTaskPredPo.setLagQty(0.0);
        super.insert(baseTmplTaskPredPo);
        return baseTmplTaskPredPo;
    }

    @Override
    public void addPlanTmplTaskPred(List<BaseTmplTaskPredPo> baseTmplTaskPredPoList){
        super.insert(baseTmplTaskPredPoList);
    }

    @Override
    @AddLog(title = "修改逻辑关系" , module = LoggerModuleEnum.BM_TMPL_PLAN)
    public BaseTmplTaskPredPo updateBaseTmplTaskPred(BaseTmplTaskPredUpdateForm baseTmplTaskPredUpdateForm) {
        BaseTmplTaskPredPo baseTmplTaskPredPo = this.selectById(baseTmplTaskPredUpdateForm.getId());
        if(baseTmplTaskPredPo==null){
            throw new BaseException("修改的任务不存在!");
        }
        // 添加修改日志
        this.addChangeLogger(baseTmplTaskPredUpdateForm,baseTmplTaskPredPo);
        dozerMapper.map(baseTmplTaskPredUpdateForm, baseTmplTaskPredPo);
        super.updateById(baseTmplTaskPredPo);
        return baseTmplTaskPredPo;
    }

    @Override
    public void deleteBaseTmplTaskPred(List<Integer> ids) {
        super.deleteByIds(ids);
    }

    @Override
    public BaseTmplTaskPredVo queryTmplTaskPredById(Integer id){
        BaseTmplTaskPredVo baseTmplTaskPredVo = this.mapper.queryTmplTaskPredById(id);
        return baseTmplTaskPredVo;
    }

    @Override
    public String queryDeletePredTaskLogger(List<Integer> ids) {
        List<BaseTmplTaskPredPo> baseTmplTaskPredPos = super.selectByIds(ids);
        StringBuffer logger = new StringBuffer();
        List<Integer> preIds = new ArrayList<>();
        List<Integer> taskIds = new ArrayList<>();
        if (!ObjectUtils.isEmpty(baseTmplTaskPredPos)){
            for (BaseTmplTaskPredPo baseTmplTaskPredPo : baseTmplTaskPredPos){
                if (!preIds.contains(baseTmplTaskPredPo.getPredTaskId())){
                    preIds.add(baseTmplTaskPredPo.getPredTaskId());
                }
                if (!taskIds.contains(baseTmplTaskPredPo.getTaskId())){
                    taskIds.add(baseTmplTaskPredPo.getTaskId());
                }
            }
            List<BaseTmplTaskPo> taskPos = baseTmplTaskService.selectByIds(taskIds);
            List<BaseTmplTaskPo> prePos = baseTmplTaskService.selectByIds(preIds);
            StringBuffer task = new StringBuffer();
            StringBuffer preTask = new StringBuffer();
            for (BaseTmplTaskPo baseTmplTaskPo: taskPos){
                task.append(baseTmplTaskPo.getTaskName()+"，");
            }
            for (BaseTmplTaskPo baseTmplTaskPo: prePos){
                preTask.append(baseTmplTaskPo.getTaskName()+"，");
            }
            logger.append("删除逻辑关系，【前紧任务：" + preTask.toString().substring(0,preTask.toString().length()-1)+"；后续任务："+ task.toString().substring(0,task.toString().length()-1) + "】");
        }
        return logger.toString();
    }
}
