package com.wisdom.acm.wf.service.impl;

import com.wisdom.acm.wf.form.WfAssignAddForm;
import com.wisdom.acm.wf.mapper.WfAssignMapper;
import com.wisdom.acm.wf.po.WfAssignPo;
import com.wisdom.acm.wf.po.WfBizTypePo;
import com.wisdom.acm.wf.service.WfAssignService;
import com.wisdom.acm.wf.service.WfBizTypeService;
import com.wisdom.acm.wf.vo.WfAssignVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommActivitiService;
import com.wisdom.base.common.form.ActModelAddForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.TreeUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.ActModelVo;
import com.wisdom.base.common.vo.wf.WfProcessDefVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class WfAssignServiceImpl extends BaseService<WfAssignMapper, WfAssignPo> implements WfAssignService {

    @Autowired
    private WfBizTypeService wfBizTypeService;

    @Autowired
    private CommActivitiService commActivitiService;

    @Override
    public List<WfAssignVo> queryWfAssignList(){
        List<WfAssignVo> wfAssignVoList = new ArrayList<>();
        List<WfBizTypePo> wfBizTypePoList = wfBizTypeService.selectListAll();
        List<WfAssignPo> wfAssignPoList = mapper.selectAll();
            Map<String,Integer> bizModleMap = new HashMap<>();
        if(!ObjectUtils.isEmpty(wfAssignPoList)){
            for(WfAssignPo wfAssignPo : wfAssignPoList){
                bizModleMap.put(wfAssignPo.getModelId(),wfAssignPo.getTypeId());
            }
        }
        List<String> modelIds = ListUtil.toValueList(wfAssignPoList,"modelId",String.class);
        ApiResult<List<ActModelVo>> result = commActivitiService.queryWfAssignList(modelIds);

        List<ActModelVo> actModelVoList = result.getData();
        if(!ObjectUtils.isEmpty(actModelVoList)){
            WfAssignVo wfAssignVo = null;
            for(ActModelVo newModelVo : actModelVoList){
                wfAssignVo = new WfAssignVo();
                wfAssignVo.setId(newModelVo.getId());
                wfAssignVo.setWfTitle(newModelVo.getName());
                wfAssignVo.setParentId(bizModleMap.get(FormatUtil.toString(newModelVo.getId())));
                wfAssignVo.setStatus(newModelVo.getStatus());
                wfAssignVo.setWfDefName(newModelVo.getKey());
                wfAssignVo.setRemark(newModelVo.getMetaInfo());
                wfAssignVo.setType("activiti");
                wfAssignVoList.add(wfAssignVo);
            }
        }

        if(!ObjectUtils.isEmpty(wfBizTypePoList)){
            WfAssignVo wfAssignVo = null;
            for(WfBizTypePo wfBizTypePo : wfBizTypePoList){
                wfAssignVo = new WfAssignVo();
                wfAssignVo.setId(wfBizTypePo.getId());
                wfAssignVo.setWfTitle(wfBizTypePo.getTypeName());
                wfAssignVo.setParentId(0);
                wfAssignVo.setType("bussi");
                wfAssignVo.setStatus("");
                wfAssignVoList.add(wfAssignVo);
            }
            wfAssignVoList = TreeUtil.bulid(wfAssignVoList,0);
        }
        return wfAssignVoList;
    }

    /**
     * 获取模版id
     * @return
     */
    @Override
    public WfAssignVo addActivityNewModel(ActModelAddForm actModelAddForm){
        ApiResult result = commActivitiService.addActivityNewModel(actModelAddForm);
        String modelId = FormatUtil.toString(result.getData());
        WfAssignVo wfAssignVo = new WfAssignVo();
        ApiResult<List<ActModelVo>> modelresult = commActivitiService.queryWfAssignList(Arrays.asList(modelId));
        List<ActModelVo> actModelVoList = modelresult.getData();
        if(!ObjectUtils.isEmpty(actModelVoList)){
            wfAssignVo.setId(FormatUtil.toInteger(modelId));
            wfAssignVo.setWfTitle(actModelVoList.get(0).getName());
            wfAssignVo.setParentId(actModelAddForm.getParentId());
            wfAssignVo.setStatus(actModelVoList.get(0).getStatus());
            wfAssignVo.setWfDefName(actModelVoList.get(0).getKey());
            wfAssignVo.setType("activiti");
        }
        return wfAssignVo;
    }

    /**
     * 业务绑定流程
     * @return
     */
    @Override
    public WfAssignPo assignBussiNewModel(WfAssignAddForm wfAssignAddForm){
        WfAssignPo wfAssignPo = dozerMapper.map(wfAssignAddForm, WfAssignPo.class);
        super.insert(wfAssignPo);
        return wfAssignPo;
    }

    /**
     * 发布模版
     * @param modelId
     * @return
     */
    @Override
    public WfAssignVo releaseBussiNewModel(String modelId){
        WfAssignVo wfAssignVo = new WfAssignVo();
        if(!ObjectUtils.isEmpty(modelId)){
            Example example = new Example(WfAssignPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("modelId", modelId);
            WfAssignPo wfAssignPo = this.selectOneByExample(example);
            ApiResult result = commActivitiService.releaseActivityNewModel(modelId);
            String retMsg = FormatUtil.toString(result.getData());
            if("success".equals(retMsg)){
                ApiResult<List<ActModelVo>> modelresult = commActivitiService.queryWfAssignList(Arrays.asList(modelId));
                List<ActModelVo> actModelVoList = modelresult.getData();
                if(!ObjectUtils.isEmpty(actModelVoList)){
                    wfAssignVo.setId(FormatUtil.toInteger(modelId));
                    wfAssignVo.setWfTitle(actModelVoList.get(0).getName());
                    wfAssignVo.setParentId(!ObjectUtils.isEmpty(wfAssignPo) ? wfAssignPo.getTypeId() : null);
                    wfAssignVo.setStatus(actModelVoList.get(0).getStatus());
                    wfAssignVo.setWfDefName(actModelVoList.get(0).getKey());
                    wfAssignVo.setType("activiti");
                }
            }else{
                throw new BaseException(retMsg);
            }
        }
        return wfAssignVo;
    }

    /**
     * 删除模版
     * @param modelId
     * @return
     */
    @Override
    public String deleteBussiNewModel(String modelId){
        String retMsg = "";
        if(!ObjectUtils.isEmpty(modelId)){
            ApiResult result = commActivitiService.deleteActivityNewModel(modelId);
            retMsg = FormatUtil.toString(result.getData());
            Example example = new Example(WfAssignPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("modelId",modelId);
            this.deleteByExample(example);
        }
        return retMsg;
    }

    /**
     * 获取已发布模版
     * @param typeCode
     * @return
     */
    @Override
    public List<WfProcessDefVo> getAllModelByTypeId(String typeCode) {
        //通过typeCode 获取typeId;
        List<WfProcessDefVo> retList = new ArrayList<>();
        WfBizTypePo wfBizTypePo = wfBizTypeService.queryWfTypeByTypeCode(typeCode);
        if (!Tools.isEmpty(wfBizTypePo)) {
            Example example = new Example(WfAssignPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("typeId", wfBizTypePo.getId());
            List<WfAssignPo> wfAssignPos = this.selectByExample(example);
            if (!ObjectUtils.isEmpty(wfAssignPos)) {
                List<String> modelIds = ListUtil.toValueList(wfAssignPos, "modelId", String.class);
                ApiResult<List<WfProcessDefVo>> result = commActivitiService.queryProcDefByModulIdList(modelIds);
                if (!result.isSuccess()) {
                    throw new BaseException(result.getMessage());
                }
                retList = result.getData();
            }
        }
        return retList;
    }
}
