package com.wisdom.acm.szxm.service.sysscore;

import com.wisdom.acm.szxm.po.sysscore.ObjectScoreDetailPo;
import com.wisdom.base.common.service.CommService;

/**
 * Author：wqd
 * Date：2019-12-30 16:54
 * Description：<描述>
 */
public interface ObjectScoreDetailService extends CommService<ObjectScoreDetailPo> {

    /**
     * 删除标段下 该年该月客观评分细项
     * @param projectId
     * @param sectionId
     * @param year
     * @param month
     */
    void deleteObjectDetailScore(Integer projectId,Integer sectionId,Integer year,Integer month);
}
