package com.wisdom.acm.processing.service.representation.impl;

import com.wisdom.acm.processing.mapper.representation.DailyRepresentationMapper;
import com.wisdom.acm.processing.po.representation.DailyRepresentationPo;
import com.wisdom.acm.processing.service.representation.DailyRepresentationService;
import com.wisdom.acm.processing.vo.representation.DailyRepresentationVo;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lc
 * @description
 * @date 2020/11/10 13:50
 */
@Service
@Slf4j
public class DailyRepresentationServiceImpl extends BaseService<DailyRepresentationMapper, DailyRepresentationPo> implements DailyRepresentationService {
    @Override
    public List<DailyRepresentationVo> queryDailyRepresentationList(Map<String, Object> map) {
        return mapper.queryDailyRepresentationList(map);
    }
}
