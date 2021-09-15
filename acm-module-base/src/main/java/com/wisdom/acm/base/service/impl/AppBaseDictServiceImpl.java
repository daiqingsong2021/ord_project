package com.wisdom.acm.base.service.impl;

import com.wisdom.acm.base.mapper.AppBaseDictMapper;
import com.wisdom.acm.base.po.BaseDictPo;
import com.wisdom.acm.base.service.AppBaseDictService;
import com.wisdom.acm.base.vo.app.AppBaseDictVo;
import com.wisdom.base.common.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppBaseDictServiceImpl extends BaseService<AppBaseDictMapper, BaseDictPo> implements AppBaseDictService {

    /**
     * 获取字典类型的字典列表
     * @param typeCode
     * @return
     */
    @Override
    public List<AppBaseDictVo> queryAppBaseDictByTypeCode(String typeCode){
        List<AppBaseDictVo> list = mapper.selectAppDictVoByTypeCode(typeCode);
        return list;
    }
}
