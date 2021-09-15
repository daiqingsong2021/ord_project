package com.wisdom.acm.szxm.mapper.sysscore;

import com.wisdom.acm.szxm.po.sysscore.SysScorePo;
import com.wisdom.acm.szxm.vo.sysscore.SysScoreVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2019-12-30 16:51
 * Description：<描述>
 */
public interface SysScoreMapper extends CommMapper<SysScorePo> {
    List<SysScoreVo> selectSysScoreList(Map<String, Object> mapWhere);

    /**
     * 查询需要考评并且当前时间在考评期间内的标段
     * @param projectId
     * @return
     */
    List<String> selectEvaluationSections(Integer projectId);
}
