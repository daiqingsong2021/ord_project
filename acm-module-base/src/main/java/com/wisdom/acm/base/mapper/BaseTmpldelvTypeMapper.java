package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseTmpldelvTypePo;
import com.wisdom.acm.base.vo.tmpldelv.BaseTmpldelvTypeVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BaseTmpldelvTypeMapper extends CommMapper<BaseTmpldelvTypePo> {

    /**
     * 交付物模板分页列表
     * @return
     */
    public List<BaseTmpldelvTypeVo> selectTmpldelvTypeList(@Param("key") String key);

    /**
     * 交付物模板基本信息
     * @return
     */
    public BaseTmpldelvTypeVo selectTmpldelvTypeById(@Param("id") Integer tmpldelvTypeId);


}