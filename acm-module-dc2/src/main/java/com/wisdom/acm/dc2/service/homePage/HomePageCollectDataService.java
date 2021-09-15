package com.wisdom.acm.dc2.service.homePage;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.TrainDailyAddForm;
import com.wisdom.acm.dc2.form.TrainDailyUpdateForm;
import com.wisdom.acm.dc2.po.TrainDailyPo;
import com.wisdom.acm.dc2.vo.TrainDailyVo;
import com.wisdom.acm.dc2.vo.homePage.HomePageCollectDataVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface HomePageCollectDataService
{

    HomePageCollectDataVo selectCollectDataList(Map<String, Object> mapWhere);
}
