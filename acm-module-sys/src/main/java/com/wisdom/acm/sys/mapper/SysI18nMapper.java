package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.po.SysI18nPo;
import com.wisdom.acm.sys.vo.SysI18nRelationVo;
import com.wisdom.acm.sys.vo.SysI18nVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysI18nMapper extends CommMapper<SysI18nPo> {

    List<SysI18nVo> selectSysI18nVoByMenuId(@Param("menuId") Integer menuId);

    SysI18nVo selectSysI18nVoOneById(@Param("id") Integer id);

    //查询所有语种和值
    List<SysI18nRelationVo> selectSysI18nRelationVoByIds(@Param("ids") List<Integer> ids);


    //查询所有语种和值
    List<SysI18nRelationVo> selectSysI18nRelationVoById(@Param("id") Integer id);
}
