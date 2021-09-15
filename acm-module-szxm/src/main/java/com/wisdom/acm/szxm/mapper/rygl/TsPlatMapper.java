package com.wisdom.acm.szxm.mapper.rygl;

import com.wisdom.acm.szxm.po.rygl.TsPlatPo;
import com.wisdom.acm.szxm.vo.rygl.TsPlatVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

public interface TsPlatMapper extends CommMapper<TsPlatPo>
{


    List<TsPlatVo> selectTsPlatList(Map<String,Object> mapWhere) ;
}
