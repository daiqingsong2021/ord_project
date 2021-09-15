package com.wisdom.acm.szxm.service.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.rygl.PeopleEntryDetailAddForm;
import com.wisdom.acm.szxm.form.rygl.PeopleEntryDetailUpdateForm;
import com.wisdom.acm.szxm.po.rygl.PeopleEntryDetailPo;
import com.wisdom.acm.szxm.vo.rygl.PeopleEntryDetailVo;
import com.wisdom.base.common.service.CommService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PeopleEntryDetailService extends CommService<PeopleEntryDetailPo>
{

    /**
     * 查询人员列表
     * @param mapWhere 查询条件
     * @param projInfoId 项目基础信息ID
     * @param pageSize 一页多少
     * @param currentPageNum 最近多少页
     * @return
     */
    PageInfo<PeopleEntryDetailVo> selectPeopleEntryDetailList(Map<String, Object> mapWhere, Integer projInfoId, Integer pageSize,
            Integer currentPageNum);

    /**
     * 增加人员
     * @param peopleAddForm
     * @return
     */
    PeopleEntryDetailVo addPeopleEntryDetail(PeopleEntryDetailAddForm peopleAddForm);

    /**
     * 修改人员
     * @param peopleUpdateForm
     * @return
     */
    PeopleEntryDetailVo updatePeopleEntryDetail(PeopleEntryDetailUpdateForm peopleUpdateForm);

    /**
     * 删除人员
     * @param ids
     */
    void deletePeopleEntryDetail(List<Integer> ids);

    String uploadPeoEntryDetailFile(MultipartFile file,Map<String,Object> paramMap);


    /**
     * 批量增加退场明细
     * @param peopleEntryDetailAddForms
     * @return
     */
    List<PeopleEntryDetailVo> addPeopleEntryDetail(List<PeopleEntryDetailAddForm> peopleEntryDetailAddForms);
}
