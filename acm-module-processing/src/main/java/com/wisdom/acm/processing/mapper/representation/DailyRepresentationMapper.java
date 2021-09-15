package com.wisdom.acm.processing.mapper.representation;

import com.wisdom.acm.processing.po.representation.DailyRepresentationPo;
import com.wisdom.acm.processing.vo.representation.DailyRepresentationVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

/**
 * @author lc
 * @description
 * @date 2020/11/10 13:40
 */
public interface DailyRepresentationMapper  extends CommMapper<DailyRepresentationPo> {

    /**
     * 获取情况说明记录
     * @param map
     * @return
     */
    List<DailyRepresentationVo> queryDailyRepresentationList(Map<String,Object> map);
}
