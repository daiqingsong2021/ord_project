package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseBoPo;
import com.wisdom.acm.base.vo.BaseBoVo;
import com.wisdom.base.common.mapper.CommMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BaseBoMapper extends CommMapper<BaseBoPo> {
    /**
     * 查询业务对象列表
     * @return
     */
    public List<BaseBoVo> queryBoList(Integer boType);

}
