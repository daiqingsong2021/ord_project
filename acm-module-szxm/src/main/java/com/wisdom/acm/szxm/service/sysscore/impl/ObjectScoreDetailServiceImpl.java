package com.wisdom.acm.szxm.service.sysscore.impl;

import com.wisdom.acm.szxm.mapper.sysscore.ObjectScoreDetailMapper;
import com.wisdom.acm.szxm.po.sysscore.ObjectScoreDetailPo;
import com.wisdom.acm.szxm.service.sysscore.ObjectScoreDetailService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Author：wqd
 * Date：2019-12-30 17:01
 * Description：<描述>
 */
@Service
@Slf4j
public class ObjectScoreDetailServiceImpl extends BaseService<ObjectScoreDetailMapper, ObjectScoreDetailPo> implements ObjectScoreDetailService {
    @Override
    public void deleteObjectDetailScore(Integer projectId, Integer sectionId, Integer year, Integer month) {
        mapper.deleteObjectDetailScore(projectId, sectionId, year, month);
    }
}
