package com.wisdom.acm.wf.mapper;

import com.wisdom.acm.wf.form.WfTypeSearchForm;
import com.wisdom.acm.wf.po.WfBizTypePo;
import com.wisdom.acm.wf.vo.WfBizTypeVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WfBizTypeMapper extends CommMapper<WfBizTypePo> {

    List<WfBizTypeVo> selectWfTypeList();

    WfBizTypeVo selectWfTypeVo(@Param("id") Integer id);

    List<WfBizTypeVo> selectWfTypeBySearch(@Param("search") WfTypeSearchForm wfTypeSearchForm);
}
