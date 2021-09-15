package com.wisdom.acm.szxm.service.rygl;

import com.wisdom.acm.szxm.form.rygl.ProjInfoAddForm;
import com.wisdom.acm.szxm.form.rygl.ProjInfoUpdateForm;
import com.wisdom.acm.szxm.po.rygl.ProjInfoPo;
import com.wisdom.acm.szxm.vo.rygl.*;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.ProjectTeamVo;

import java.util.List;
import java.util.Map;

public interface ProjInfoService  extends CommService<ProjInfoPo>
{
    /**
     * 根据查询 属性 以及 相关标段 查询所在的记录
     * @param mapWhere 查询Map
     * @param sectionList 标段ID List
     * @return
     */
    List<ProjInfoVo> selectProjInfoList(Map<String, Object> mapWhere, List<String> sectionList);

    /**
     * 创建项目基础信息
     * @param projInfoAddForm
     * @return
     */
    ProjInfoVo addPorjInfo(ProjInfoAddForm projInfoAddForm);

    /**
     * 同步分包审批后 同步分包单位
     * @param listProjInfoPo
     * @return
     */
    ApiResult addPorjInfos(List<ProjInfoPo> listProjInfoPo);

    ProjInfoVo updatePorjInfo(ProjInfoUpdateForm projInfoUpdateForm);

    void deletePorjInfo(Integer id);

    /**
     * 增加项目基本信息，供项目团队用
     * @param sgPojectTeamVo 外部单位项目团队VO
     * @return 项目基础信息主键ID
     */
   Integer porjInfoMsg(ProjectTeamVo sgPojectTeamVo);

    /**
     * 修改项目基本信息，供项目团队用
     * @param sgPojectTeamVo 外部单位项目团队VO
     * @return 项目基础信息主键ID
     */
    Integer updatePorjInfoMsg(ProjectTeamVo sgPojectTeamVo);

    /**
     * 删除项目基本信息，供项目团队用
     * @param sysOrgid 外部单位项目团队 ID
     * @return 项目基础信息主键ID
     */
    Integer deletePorjInfoMsg(Integer sysOrgid);

    /**
     * 根据主键ID 查询projInfo
     * @param id
     * @return
     */
    ProjInfoVo selectByProjInfoId(Integer id);

    /**
     * 选择人员用 查询机构以及机构下的人员
     * @param mapWhere
     * @param sectionList
     * @return
     */
    Object getOrgPeopleList(Map<String, Object> mapWhere, List<String> sectionList);

    /**
     * 查询通讯录Org
     * @return
     */
    List<AddressBookOrgVo> selectAddressBookOrg(Map<String, Object> mapWhere);

    List<PeopleVo> selectAddressBookPeopleFyz(Integer projectId, Integer userId,String searcher);

    List<PeopleVo> selectAddressBookPeople2(Integer projectId, Integer orgId, Integer userId,String searcher);

    List<PeopleVo> selectAddressBookPeopleYz(Integer projectId, String searcher);

    List<PeopleVo> queryUserListByProjectTeamId(Integer orgId, String searcher);

    /**
     * 检查施工单位对基本信息是否有更新
     */
    Integer queryProjInfoNotUpdate(String sectionId);
    /**
     * 根据项目查询组织信息
     */
    List<SysOrgTreeVo> getOrgInfoByProjectId(Integer projectId);

    /**
     * 获取参建单位基本信息
     * @return
     */
    List<ParticipateUnitVo> getParticipateUnitInfo(Map<String, Object> mapWhere);
    /**
     * 获取 施工作业队 基本信息
     * @return
     */
    List<ParticipateUnitVo> getProjectPeopleInfo(Map<String, Object> mapWhere);

}
