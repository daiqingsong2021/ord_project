package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseDictTypePo;
import com.wisdom.acm.base.vo.dict.BaseDictTypeInfoVo;
import com.wisdom.acm.base.vo.dict.BaseDictTypeVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseDictTypeMapper extends CommMapper<BaseDictTypePo> {

    /**
     * 通过数字字典代码查询数字字典
     * @param digitCode
     * @return
     */
    List<BaseDictTypeVo> selectDictTypeDateListByDictCode(@Param("boCode") String digitCode);

    /**
     * 查询数字字典
     * @return
     */
    List<BaseDictTypeVo> selectDictTypeDateList();

    /**
     * 通过数字字典代码查找数字字典名称
     * @param gbCode
     * @return
     */
    String selectGbNameByGbCode(@Param("gbCode") String gbCode);

    BaseDictTypeInfoVo selectDictTypeInfoById(@Param("id") Integer gbTypeId);

    List<String> selectDictTypeCodesByIds(@Param("ids") List<Integer> ids);

//    List<String> selectDictTypeCodesByIds(List<Integer> ids);
}
