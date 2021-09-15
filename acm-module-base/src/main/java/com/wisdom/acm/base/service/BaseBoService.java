package com.wisdom.acm.base.service;

import com.wisdom.acm.base.po.BaseBoPo;
import com.wisdom.acm.base.vo.BaseBoVo;
import com.wisdom.base.common.service.CommService;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BaseBoService extends CommService<BaseBoPo> {

    /**
     * 查询业务对象列表
     * @return
     */
    public List<BaseBoVo> queryBoList(Integer boType);
}
