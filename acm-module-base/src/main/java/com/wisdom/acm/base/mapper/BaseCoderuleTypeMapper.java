package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseCoderuleTypePo;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleTypeVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;

public interface BaseCoderuleTypeMapper extends CommMapper<BaseCoderuleTypePo> {

    /**
     *
     * @param boId
     * @return
     */
    List<BaseCoderuleTypeVo> selectCoderuleTypeListByboId(Integer boId);

    /**
     *
     * @param id
     * @return
     */
    BaseCoderuleTypeVo selectCoderuleListById(Integer id);
}
