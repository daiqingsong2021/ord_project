package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseCoderuleCellPo;
import com.wisdom.acm.base.vo.coderule.BaseCoderuleCellVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseCoderuleCellMapper extends CommMapper<BaseCoderuleCellPo> {

    /**
     *
     * @param id
     * @return
     */
    BaseCoderuleCellVo selectCoderuleCellById(@Param("id") Integer id);

    /**
     * selectPosByRuleIds
     * @param coderuleIds
     * @return
     */
    List<BaseCoderuleCellPo> selectPosByRuleIds(@Param("coderuleIds") List<Integer> coderuleIds);

    /**
     * ByRuleIdAndPosition
     * @param ruleId
     * @param position
     * @return
     */
    List<BaseCoderuleCellVo> selectCoderuleCellByRuleIdAndPosition(@Param("ruleId") Integer ruleId,@Param("position") Integer position);
}
