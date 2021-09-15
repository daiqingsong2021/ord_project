package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseSetPo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

public interface BaseSetMapper extends CommMapper<BaseSetPo> {

    void updateBaseSet(@Param("po") BaseSetPo baseSetPo);
}
