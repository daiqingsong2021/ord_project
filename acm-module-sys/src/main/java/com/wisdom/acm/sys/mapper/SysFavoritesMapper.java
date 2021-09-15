package com.wisdom.acm.sys.mapper;

import com.wisdom.acm.sys.po.SysFavoritesPo;
import com.wisdom.acm.sys.vo.SysFavoritesVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface SysFavoritesMapper extends CommMapper<SysFavoritesPo> {

    List<SysFavoritesVo> selectFavoritesByUserId(@Param("userId") Integer id);

    SysFavoritesVo selectFavoritesById(@Param("id") Integer id);

    void deleteFavorites(@Param("menuCodes") List<String> ids);

}
