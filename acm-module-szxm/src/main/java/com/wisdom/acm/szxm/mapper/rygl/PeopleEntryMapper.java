package com.wisdom.acm.szxm.mapper.rygl;

import com.wisdom.acm.szxm.po.rygl.PeopleEntryPo;
import com.wisdom.acm.szxm.vo.rygl.PeopleEntryVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PeopleEntryMapper extends CommMapper<PeopleEntryPo>
{
    void deletePeopEntryDetByeIds(@Param("peopleEntryIds") List<Integer> peopleEntryIds);

    List<PeopleEntryVo> selectPeopleEntry(Map<String,Object> mapWhere);
    Integer queryPeopleEntryCount(@Param("sectionId")String sectionId);

    List<PeopleEntryVo> selectSectionPeopleEntry(Map<String,Object> mapWhere);

}
