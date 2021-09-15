package com.wisdom.acm.szxm.mapper.rygl;

import com.wisdom.acm.szxm.po.rygl.FbzydPo;
import com.wisdom.acm.szxm.vo.rygl.FbzydVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

public interface FbzydMapper extends CommMapper<FbzydPo>
{
    List<FbzydVo> selectFbzydList(Map<String, Object> mapWhere) ;

    List<FbzydVo> selectSectionWorkteamList(Map<String, Object> mapWhere) ;
}
