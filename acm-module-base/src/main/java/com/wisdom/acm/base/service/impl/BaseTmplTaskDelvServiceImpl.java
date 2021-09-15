package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.form.tmpltask.BaseTmplTaskDelvAssignForm;
import com.wisdom.acm.base.form.tmpltask.BaseTmplTaskDelvUpdateForm;
import com.wisdom.acm.base.mapper.BaseTmplTaskDelvMapper;
import com.wisdom.acm.base.po.BaseTmplTaskDelvPo;
import com.wisdom.acm.base.service.BaseTmplTaskDelvService;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskDelvVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.vo.plan.task.PlanTmplTaskDelvForm;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseTmplTaskDelvServiceImpl extends BaseService<BaseTmplTaskDelvMapper, BaseTmplTaskDelvPo> implements BaseTmplTaskDelvService {

    @Override
    public List<BaseTmplTaskDelvVo> queryTmplTaskDelvListByTaskId(Integer taskId) {
        return this.mapper.selectTmplTaskDelvList(taskId);
    }

    @Override
    public List<BaseTmplTaskDelvVo> queryTmplTaskDelvListByTmplId(Integer tmplId) {
        return this.mapper.selectTmplTaskDelvListByTmplId(tmplId);
    }

    @Override
    public List<BaseTmplTaskDelvVo> assignTmplTaskDelv(Integer taskId, List<Integer> delvIds) {
        List<BaseTmplTaskDelvVo> baseTmplTaskDelvList = new ArrayList<BaseTmplTaskDelvVo>();
        if(!ObjectUtils.isEmpty(taskId)){
            if(!ObjectUtils.isEmpty(delvIds)&&delvIds.size()>0){
                for(Integer delvId:delvIds){
                    Map<String,Integer> idMap = new HashMap<String,Integer>();
                    idMap.put("taskId",taskId);
                    idMap.put("delvId",delvId);
                    List<BaseTmplTaskDelvVo> baseTmplTaskDelvVoList = this.mapper.selectTaskDelvByTaskDelv(idMap);
                    if(baseTmplTaskDelvVoList.size()==0){
                        BaseTmplTaskDelvAssignForm baseTmplTaskDelvAssignForm = new BaseTmplTaskDelvAssignForm();
                        baseTmplTaskDelvAssignForm.setTaskId(taskId);
                        baseTmplTaskDelvAssignForm.setDelvId(delvId);
                        BaseTmplTaskDelvPo baseTmplTaskDelvPo = this.dozerMapper.map(baseTmplTaskDelvAssignForm, BaseTmplTaskDelvPo.class);
                        super.insert(baseTmplTaskDelvPo);
                        List<BaseTmplTaskDelvVo> baseTmplTaskDelvVoLista = this.mapper.selectTaskDelvByTaskDelv(idMap);
                        baseTmplTaskDelvList.add(baseTmplTaskDelvVoLista.get(0));
                    }
                }
            }else {
                throw new BaseException("请选择交付物!");
            }
        }else {
            throw new BaseException("请选择任务!");
        }
        return baseTmplTaskDelvList;
    }

    /**
     * 保存计划模版交付物
     * @param baseTmplTaskDelvPoList
     */
    @Override
    public void addPlanTmplTaskDelv(List<BaseTmplTaskDelvPo> baseTmplTaskDelvPoList){
        super.insert(baseTmplTaskDelvPoList);
    }
    @Override
    public BaseTmplTaskDelvPo updateTmplTaskDelv(BaseTmplTaskDelvUpdateForm baseTmplTaskDelvUpdateForm) {
        BaseTmplTaskDelvPo baseTmplTaskDelvPo = this.dozerMapper.map(baseTmplTaskDelvUpdateForm, BaseTmplTaskDelvPo.class);
        super.updateById(baseTmplTaskDelvPo);
        return baseTmplTaskDelvPo;
    }

    @Override
    public void deleteBaseTmplTaskDelv(List<Integer> ids) {
        super.deleteByIds(ids);
    }

    @Override
    public BaseTmplTaskDelvVo queryBaseTmplTaskDelvInfo(Integer id) {
        BaseTmplTaskDelvVo baseTmplTaskDelvVo = this.mapper.selectTaskDelvById(id);
        return baseTmplTaskDelvVo;
    }

    @Override
    public String queryBaseTmplTaskDelvByIds(List<Integer> ids) {
        List<BaseTmplTaskDelvVo> baseTmplTaskDelvVos = this.mapper.selectTaskDelvByIds(ids);
        String content = ListUtil.listToNames(baseTmplTaskDelvVos,"delvName");
        return content;
    }
}
