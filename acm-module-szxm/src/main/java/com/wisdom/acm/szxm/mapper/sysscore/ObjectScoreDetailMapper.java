package com.wisdom.acm.szxm.mapper.sysscore;

import com.wisdom.acm.szxm.po.sysscore.ObjectScoreDetailPo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Author：wqd
 * Date：2019-12-30 16:52
 * Description：<描述>
 */
public interface ObjectScoreDetailMapper extends CommMapper<ObjectScoreDetailPo> {
    /**
     * 删除标段下 该年该月客观评分细项
     *
     * @param projectId
     * @param sectionId
     * @param year 评分所属年份
     * @param month 评分所属月份
     */
    void deleteObjectDetailScore(@Param("projectId") Integer projectId, @Param("sectionId")Integer sectionId,
                                 @Param("year")Integer year, @Param("month")Integer month);
}
