package com.wisdom.acm.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.wisdom.acm.base.mapper.BaseBoMapper;
import com.wisdom.acm.base.po.BaseBoPo;
import com.wisdom.acm.base.service.BaseBoService;
import com.wisdom.acm.base.vo.BaseBoVo;
import com.wisdom.base.common.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseBoServiceImpl extends BaseService<BaseBoMapper, BaseBoPo> implements BaseBoService {


    /**
     * 查询业务对象列表
     * @return
     */
    @Override
    public List<BaseBoVo> queryBoList(Integer boType) {
        List<BaseBoVo> digitDirBoList = this.mapper.queryBoList(boType);
        return digitDirBoList;
    }
}
