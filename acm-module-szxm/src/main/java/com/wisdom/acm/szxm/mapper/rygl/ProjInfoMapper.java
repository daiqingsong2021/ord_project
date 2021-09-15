package com.wisdom.acm.szxm.mapper.rygl;

import com.wisdom.acm.szxm.po.rygl.ProjInfoPo;
import com.wisdom.acm.szxm.vo.rygl.AddressBookOrgVo;
import com.wisdom.acm.szxm.vo.rygl.OrgPeopleVo;
import com.wisdom.acm.szxm.vo.rygl.PeopleVo;
import com.wisdom.acm.szxm.vo.rygl.SysOrgTreeVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProjInfoMapper extends CommMapper<ProjInfoPo> {
    void deletePeopleByInfoIds(@Param("projInfoIds") List<Integer> projInfoIds);

    void deleteWarnHouseByInfoIds(@Param("projInfoIds") List<Integer> projInfoIds);

    void deleteTsPlatByInfoIds(@Param("projInfoIds") List<Integer> projInfoIds);

    List<OrgPeopleVo> selectOrgInfoList(Map<String, Object> mapWhere);

    List<AddressBookOrgVo> selectAddressBookOrg(Map<String, Object> mapWhere);

    List<PeopleVo> selectAddressBookPeopleFyz(@Param("projectId")Integer projectId, @Param("userId")Integer userId, @Param("searcher")String searcher);

    List<PeopleVo> selectAddressBookPeople2(@Param("projectId")Integer projectId, @Param("orgId")Integer orgId,@Param("userId")Integer userId, @Param("searcher")String searcher);

    List<PeopleVo> selectAddressBookPeopleYz(@Param("projectId")Integer projectId, @Param("searcher")String searcher);

    List<PeopleVo> queryUserListByProjectTeamId(@Param("orgId") Integer orgId,@Param("searcher") String searcher);

    /**
     * 检查施工单位对基本信息是否有更新   不查分包单位
     */
    Integer queryProjInfoNotUpdate(@Param("sectionId")String sectionId);
    /**
     * 通过项目查询组织的id
     */
    List<SysOrgTreeVo> getAllOrgInfoByProjectId(Integer projectId);

    Integer getOrgIdsByProjectId(Integer projectId);

    List<ProjInfoPo> selectParticipartUnitProInfo(Map<String, Object> mapWhere);

    /**
     * 施工作业队
     * @param mapWhere
     * @return
     */
    List<ProjInfoPo> selectOrgCategoryBySessionId(Map<String, Object> mapWhere);

}

