package com.wisdom.acm.dc2.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.DailyRepresentationAddForm;
import com.wisdom.acm.dc2.form.DailyRepresentationUpdateForm;
import com.wisdom.acm.dc2.po.DailyRepresentationPo;
import com.wisdom.acm.dc2.vo.DailyRepresentationVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface DailyRepresentationService extends CommService<DailyRepresentationPo>
{

    DailyRepresentationVo addDailyRepresentation(DailyRepresentationAddForm dailyRepresentationAddForm);

    PageInfo<DailyRepresentationVo> selectDailyRepresentationList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    void deleteDailyRepresentation(Integer id);

    DailyRepresentationVo updateDailyRepresentation(DailyRepresentationUpdateForm dailyRepresentationUpdateForm);

    DailyRepresentationVo selectById(Integer id);

    List<DailyRepresentationVo> selectByParams(Map<String, Object> mapWhere);

}
