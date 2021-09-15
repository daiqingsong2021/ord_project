package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseCoderuleBoPo;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleBoVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseCoderuleBoMapper extends CommMapper<BaseCoderuleBoPo> {

    /**
     * 编码规则-业务对象列表
     * @return
     */
    List<BaseCoderuleBoVo> selectCoderuleboList();

    /**
     * 编码规则-业务对象的基本信息
     * @param id
     * @return
     */
    BaseCoderuleBoVo selectCoderuleboById(@Param("id") Integer id);

    /**
     * bocode不可重复
     * @param boCode
     * @return
     */
    List<BaseCoderuleBoVo> selectCoderuleboByBoCode(@Param("boCode") String boCode);
}
