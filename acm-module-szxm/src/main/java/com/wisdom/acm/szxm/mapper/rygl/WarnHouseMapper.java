package com.wisdom.acm.szxm.mapper.rygl;

import com.wisdom.acm.szxm.po.rygl.WarnHousePo;
import com.wisdom.acm.szxm.vo.rygl.WarnHouseVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

public interface WarnHouseMapper extends CommMapper<WarnHousePo>
{
    List<WarnHouseVo> selectWarnHouseList(Map<String,Object> mapWhere) ;

    List<WarnHouseVo> selectWarnHouseListByProjId(Map<String,Object> mapWhere) ;

}
