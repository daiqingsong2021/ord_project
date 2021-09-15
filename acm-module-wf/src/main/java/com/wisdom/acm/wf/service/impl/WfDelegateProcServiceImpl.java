package com.wisdom.acm.wf.service.impl;

import com.wisdom.acm.wf.form.WfDelegateProcAddForm;
import com.wisdom.acm.wf.mapper.WfDelegateProcMapper;
import com.wisdom.acm.wf.po.WfDelegateProcPo;
import com.wisdom.acm.wf.service.WfBizTypeService;
import com.wisdom.acm.wf.service.WfDelegateProcService;
import com.wisdom.acm.wf.vo.WfBizTypeVo;
import com.wisdom.acm.wf.vo.WfDelegateProcVo;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WfDelegateProcServiceImpl extends BaseService<WfDelegateProcMapper, WfDelegateProcPo> implements WfDelegateProcService {

    @Autowired
    private WfBizTypeService wfBizTypeService;

    /**
     * 分配需要代理的流程业务
     * @param delegateId
     * @param bizTypeCodes
     */
    @Override
    public void wfDelegateProc(Integer delegateId, List<String> bizTypeCodes){
        List<WfDelegateProcPo> wfDelegateProcPos = new ArrayList<>();

        for (String bizTypeCode : bizTypeCodes){
            WfDelegateProcAddForm addForm = new WfDelegateProcAddForm();
            addForm.setDelegateId(delegateId);
            addForm.setBizTypeCode(bizTypeCode);

            WfDelegateProcPo wfDelegateProcPo = dozerMapper.map(addForm,WfDelegateProcPo.class);
            wfDelegateProcPos.add(wfDelegateProcPo);

        }

        this.insert(wfDelegateProcPos);
    }

    /**
     * 查询已分配的流程业务
     * @param delegateId
     * @return
     */
    @Override
    public List<WfDelegateProcVo> queryDelegateProcList(Integer delegateId){
        List<WfDelegateProcVo> list = mapper.selectDelegateProcList(delegateId);

        return list;
    }

    /**
     * 删除已分配的流程业务
     * @param ids
     */
    @Override
    public void deleteDelegateProc(List<Integer> ids){
        this.deleteByIds(ids);
    }

    /**
     * 查询可代理的流程业务
     * @param delegateId
     * @return
     */
    @Override
    public List<WfBizTypeVo> queryDelegateWfTypeList(Integer delegateId){
        //调用流程业务服务查询流程业务
        List<WfBizTypeVo> wfTypeVos = wfBizTypeService.queryWfTypeList();
        //查询已分配的流程业务
        List<WfDelegateProcVo> retList = mapper.selectDelegateProcList(delegateId);
        List<Integer> typeIds = ListUtil.toValueList(retList,"typeId",Integer.class,true);

        List<WfBizTypeVo> list = new ArrayList<>();
        for (WfBizTypeVo wfBizTypeVo : wfTypeVos){
            if (!typeIds.contains(wfBizTypeVo.getId())){
                list.add(wfBizTypeVo);
            }
        }

        return list;
    }

}
