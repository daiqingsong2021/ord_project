package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseCoderulePo;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseCoderuleMapper extends CommMapper<BaseCoderulePo> {

    /**
     * byId
     * @param id
     * @return
     */
    BaseCoderuleVo selectCoderuleById(@Param("id") Integer id);

    /**
     *
     * @param boId
     * @return
     */
    List<BaseCoderuleVo> selectCoderuleListByboId(@Param("boId") Integer boId);

    BaseCoderuleVo selectDefaultByBoId(@Param("boId") Integer boId);
}
