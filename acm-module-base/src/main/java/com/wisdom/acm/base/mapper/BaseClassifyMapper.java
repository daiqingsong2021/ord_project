package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseClassifyPo;
import com.wisdom.acm.base.vo.classify.BaseClassifyTreeVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseClassifyMapper extends CommMapper<BaseClassifyPo> {

    List<BaseClassifyTreeVo> selectClassifyValueList(@Param("classifyId") Integer classifyId);
}
