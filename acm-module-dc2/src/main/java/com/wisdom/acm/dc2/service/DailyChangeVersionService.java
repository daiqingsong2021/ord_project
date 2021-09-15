package com.wisdom.acm.dc2.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.DailyChangeVersionAddForm;
import com.wisdom.acm.dc2.po.DailyChangeVersionPo;
import com.wisdom.acm.dc2.vo.DailyChangeVersionVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface DailyChangeVersionService extends CommService<DailyChangeVersionPo>
{

    DailyChangeVersionVo addDailyChangeVersion(DailyChangeVersionAddForm dailyChangeVersionAddForm);

    PageInfo<DailyChangeVersionVo> selectDailyChangeVersionList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    void deleteDailyChangeVersion(Integer id);

   // DailyChangeVersionVo updateDailyChangeVersion(DailyChangeVersionUpdateForm dailyRepresentationUpdateForm);

    DailyChangeVersionVo selectById(Integer id);

    List<DailyChangeVersionVo> selectByParams(Map<String, Object> mapWhere);

}
