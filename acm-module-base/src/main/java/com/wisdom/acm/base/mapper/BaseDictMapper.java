package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseDictPo;
import com.wisdom.acm.base.vo.dict.BaseDictTreeVo;
import com.wisdom.acm.base.vo.dict.BaseDictVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseDictMapper extends CommMapper<BaseDictPo> {

    /**
     * 通过业务对象查找字典码值
     * @param boCode
     * @return
     */
    List<BaseDictVo> selectDictDateListByBoCode(@Param("boCode") String boCode);

    /**
     * 通过业务对象和字典代码查找字典码值名称
     * @param digitDirCode
     * @param boCode
     * @return
     */
    String selectDictNameByDictCode(@Param("digitDirCode") String digitDirCode,@Param("boCode") String boCode);

    /**
     * 通过typeCode查找字典码值
     * @param typeCode
     * @return
     */
    List<BaseDictPo> selectDictDateListByTypeCode(@Param("typeCode") String typeCode);

    List<BaseDictTreeVo> selectDictTreeVoByTypeCode(@Param("typeCode") String typeCode);

    void deleteDictByTypeCodes(@Param("codes") List<String> typeCodes);
}
