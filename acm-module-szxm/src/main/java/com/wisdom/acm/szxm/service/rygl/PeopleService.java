package com.wisdom.acm.szxm.service.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.rygl.PeopleAddForm;
import com.wisdom.acm.szxm.form.rygl.PeopleUpdateForm;
import com.wisdom.acm.szxm.po.rygl.PeopleEntryDetailPo;
import com.wisdom.acm.szxm.po.rygl.PeoplePo;
import com.wisdom.acm.szxm.vo.rygl.LwryHolidayVo;
import com.wisdom.acm.szxm.vo.rygl.PeopleVo;
import com.wisdom.base.common.service.CommService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PeopleService  extends CommService<PeoplePo>
{

    /**
     * 查询人员列表
     * @param mapWhere 查询条件
     * @param projInfoId 项目基础信息ID
     * @param pageSize 一页多少
     * @param currentPageNum 最近多少页
     * @return
     */
    PageInfo<PeopleVo> selectPeopleList(Map<String,Object> mapWhere,Integer projInfoId, Integer pageSize, Integer currentPageNum);

    /**
     * 增加人员
     * @param peopleAddForm
     * @return
     */
    PeopleVo addPeople(PeopleAddForm peopleAddForm);

    /**
     * 人员进退场用，增加人员
     * @param peopleEntryDetailPoList
     * @return key 为insert,unInsert,插入和未插入对应的PeoplePo
     */
    List<PeoplePo> addPeopleByEntry(List<PeopleEntryDetailPo> peopleEntryDetailPoList,Integer peojectId,Integer projInfoId);

    /**
     * 修改人员
     * @param peopleUpdateForm
     * @return
     */
    PeopleVo updatePeople(PeopleUpdateForm peopleUpdateForm);

    /**
     * 删除人员
     * @param ids
     */
    void deletePeople(List<Integer> ids);


    /**
     * 根据基础信息ID 删除所有人员
     * @param allIds
     */
    void deletePeopleByInfoIds(List<Integer> allIds);

    String uploadPeopleFile(MultipartFile file, Map<String, Object> paramMap);

     List<PeopleVo> selectPeople(Map<String, Object> mapWhere);

    List<PeopleVo> selectOrgPeople(Map<String, Object> mapWhere);

    /**
     * 查询所有需要考勤的人员数据
     * @return
     */
    List<PeopleVo> selectAllKqPeople(Map<String,Object> mapWhere);

    /**
     * 根据身份证查询人员信息
     * @return
     */
    List<PeopleVo> selectPeopleByIDCard(String idCard);

    /**
     * 查询所有需要考勤的人员数据 -- 所有标段下数据
     * @return
     */
    List<PeopleVo> selectAllKqPeopleAllSection(Map<String,Object> mapWhere);

     /* *
     *mapWhere
     * @param mapWhere
     * @return
     */
    List<PeopleVo> selectAddressBookPeople(Map<String,Object> mapWhere);

    /**
     * 查询section人员列表
     * @param mapWhere 查询条件
     * @param projInfoId 项目基础信息ID
     * @param pageSize 一页多少
     * @param currentPageNum 最近多少页
     * @return
     */
    PageInfo<PeopleVo> selectSectionPeopleList(Map<String,Object> mapWhere,Integer projInfoId, Integer pageSize, Integer currentPageNum);
    /**
     * 查询标段下面所有人员列表
     * @param sectionId 查询条件
     * @return
     */
    List<LwryHolidayVo> getConstructionCrowdInfo(int sectionId);
}
