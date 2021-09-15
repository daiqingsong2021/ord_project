package com.wisdom.acm.szxm.mapper.rygl;

import com.wisdom.acm.szxm.po.rygl.PeoplePo;
import com.wisdom.acm.szxm.vo.rygl.LwryHolidayVo;
import com.wisdom.acm.szxm.vo.rygl.PeopleVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PeopleMapper extends CommMapper<PeoplePo>
{
    void deleteSpeciWorkByPeoId(@Param("peopleIds") List<Integer> ids);

    List<PeopleVo> selectPeople(Map<String, Object> mapWhere);

    List<PeopleVo> selectSectionPeople(Map<String, Object> mapWhere);

    List<PeopleVo> selectOrgPeople(Map<String, Object> mapWhere);

    List<PeopleVo> selectAllKqPeople(Map<String,Object> mapWhere);

    List<PeopleVo> selectAddressBookPeople(Map<String, Object> mapWhere);

    List<PeopleVo> selectPeopleByIDCard(Map<String, Object> mapWhere);

    int getSysOrgIdBySectionId(int sectionId);
    List<LwryHolidayVo> getLwryInfoByOrgId(int sysOrgId);

    List<PeopleVo> selectPeopleListByOrgName(Map<String, Object> mapWhere);
}
