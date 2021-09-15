package com.wisdom.acm.base.service;

import com.wisdom.acm.base.po.BaseDictPo;
import com.wisdom.acm.base.vo.app.AppBaseDictVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface AppBaseDictService extends CommService<BaseDictPo> {
    List<AppBaseDictVo> queryAppBaseDictByTypeCode(String typeCode);
}
