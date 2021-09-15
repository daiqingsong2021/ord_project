package com.wisdom.acm.szxm.service.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.rygl.PeopleEntryAddForm;
import com.wisdom.acm.szxm.form.rygl.PeopleEntryUpdateForm;
import com.wisdom.acm.szxm.po.rygl.PeopleEntryPo;
import com.wisdom.acm.szxm.vo.rygl.PeopleEntryVo;
import com.wisdom.acm.szxm.vo.rygl.WorkListsVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface PeopleEntryService extends CommService<PeopleEntryPo>
{

    PageInfo<PeopleEntryVo> selectPeopleEntryList(Map<String, Object> mapWhere, List<String> sectionList,
            Integer pageSize, Integer currentPageNum);


    /**
     *获取人员进退场列表 -- 领导首页
     * @param mapWhere
     * @return
     */
    WorkListsVo getWorkersList(Map<String, Object> mapWhere);

    /**
     * 流程查询 方法
     * @param mapWhere
     * @param sectionList
     * @return
     */
    List<PeopleEntryVo> selectFlowPeopleEntryList(Map<String, Object> mapWhere, List<String> sectionList);

    PeopleEntryVo addPeopleEntry(PeopleEntryAddForm peopleChangeAddForm);

    void deletePeopleEntry(List<Integer> ids);

    PeopleEntryVo updatePeopleEntry(PeopleEntryUpdateForm peopleChangeUpdateForm);

    PeopleEntryVo selectByPeopleEntryId(Integer id);

    /**
     * 驳回流程
     * @param ids
     */
    void rejectPeopleEntryFlow(List<Integer> ids);

    /**
     * 终止流程
     * @param ids
     */
    void terminatePeopleEntryFlow(List<Integer> ids);

    /**
     * 删除流程
     * @param ids
     */
    void deletePeopleEntryFlow(List<Integer> ids);

    /**
     * 检查进退场业务日期与数据创建日期超过7天 数量
     * @param sectionId
     * @return
     */
    int queryPeopleEntryCount(String sectionId);

    /**
     * 查询人员进退场信息
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    PageInfo<PeopleEntryVo> selectSectionPeopleEntryList(Map<String, Object> mapWhere,Integer pageSize, Integer currentPageNum);
}
