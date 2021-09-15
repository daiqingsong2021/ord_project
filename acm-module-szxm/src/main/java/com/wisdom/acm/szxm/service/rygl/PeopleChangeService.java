package com.wisdom.acm.szxm.service.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.rygl.PeopleChangeAddForm;
import com.wisdom.acm.szxm.form.rygl.PeopleChangeUpdateForm;
import com.wisdom.acm.szxm.po.rygl.PeopleChangePo;
import com.wisdom.acm.szxm.vo.rygl.PeopleChangeVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface PeopleChangeService extends CommService<PeopleChangePo>
{

    PageInfo<PeopleChangeVo> selectPeopleChangeList(Map<String, Object> mapWhere, List<String> sectionList, Integer pageSize, Integer currentPageNum);

    PeopleChangeVo addPeopleChange(PeopleChangeAddForm peopleChangeAddForm);

    void deletePeopleChange(List<Integer> ids);

    PeopleChangeVo updatePeopleChange(PeopleChangeUpdateForm peopleChangeUpdateForm);

    PeopleChangeVo selectByPeopleChangeId(Integer id);

    List<PeopleChangeVo> selectFlowPeopleChangeList(Map<String, Object> mapWhere, List<String> sectionList);

    void approvePeopleChangeFlow(String bizType, List<Integer> ids);

    /**
     * 人员变更预览
     * @param id 人员变更id
     * @return
     */
    String ylPeopleChangeCheck(Integer id);

    /**
     * 检查变更业务日期与数据创建日期超过7天数量
     * @param sectionId
     * @return
     */
    int queryPeopleChangeCount(String sectionId);


    PageInfo<PeopleChangeVo> selectSectionPeopleChangeList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);
}
