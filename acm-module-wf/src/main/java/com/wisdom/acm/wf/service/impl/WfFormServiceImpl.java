package com.wisdom.acm.wf.service.impl;

import com.wisdom.acm.wf.mapper.WfFormMapper;
import com.wisdom.acm.wf.po.WfFormPo;
import com.wisdom.acm.wf.service.WfFormService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.wf.MyUnFinishTaskVo;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class WfFormServiceImpl extends BaseService<WfFormMapper, WfFormPo> implements WfFormService
{


    @Override
    public WfFormPo getFormInfoByProcInstId(String procInstId) {

        Example example = new Example(WfFormPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("procInstId",procInstId);

        return this.selectOneByExample(example);
    }

    @Override
    public int getProcCreatorByProcInstId(String procInstId) {
        Example example = new Example(WfFormPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("procInstId",procInstId);

        WfFormPo wfFormPo =  this.selectOneByExample(example);
        return wfFormPo.getCreator();
    }

    @Override
    public List<MyUnFinishTaskVo> queryTaskVoByProcInstIds(List<String> procInstIds) {
        return this.mapper.selectTaskVoByProcInstIds(procInstIds);
    }
}
