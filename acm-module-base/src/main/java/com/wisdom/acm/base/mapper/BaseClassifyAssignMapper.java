package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseClassifyAssignPo;
import com.wisdom.acm.base.vo.classify.BaseClassifyAssignVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseClassifyAssignMapper extends CommMapper<BaseClassifyAssignPo> {

    /**
     * 通过业务对象查找分类码
     * @param boCode
     * @return
     */
    List<BaseClassifyAssignVo> selectClassifyAssignDateListByBoCodeAndBoId(@Param("boCode") String boCode, @Param("bizId") Integer bizId);
}
