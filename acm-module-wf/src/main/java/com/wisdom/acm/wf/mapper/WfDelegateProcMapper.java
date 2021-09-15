package com.wisdom.acm.wf.mapper;

import com.wisdom.acm.wf.po.WfDelegateProcPo;
import com.wisdom.acm.wf.vo.WfDelegateProcVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WfDelegateProcMapper extends CommMapper<WfDelegateProcPo> {

    List<WfDelegateProcVo> selectDelegateProcList(@Param("delegateId") Integer delegateId);

//    List<WfDelegateProcVo> selectDelegateWfBizTypeList();
}
