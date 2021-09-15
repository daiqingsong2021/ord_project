package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseDictPo;
import com.wisdom.acm.base.vo.app.AppBaseDictVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppBaseDictMapper extends CommMapper<BaseDictPo> {

    List<AppBaseDictVo> selectAppDictVoByTypeCode(@Param("typeCode") String typeCode);
}
