package com.wisdom.acm.szxm.service.sysscore;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.po.sysscore.SysScorePo;
import com.wisdom.acm.szxm.vo.sysscore.ObjectScoreVo;
import com.wisdom.acm.szxm.vo.sysscore.SysScoreVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2019-12-17 16:15
 * Description：<描述>
 */
public interface SysScoreService extends CommService<SysScorePo> {
    /**
     * 获取系统客观评分
     * @return
     */
    ObjectScoreVo getSysObjectScores(Map<String, Object> mapWhere);

    /**
     * 获取系统评分
     * @return
     */
    PageInfo<SysScoreVo> getSysScores(Map<String, Object> mapWhere, List<String> sectionList, Integer pageSize, Integer currentPageNum);

    /**
     * 获取系统评分
     * @return
     */
    List<SysScoreVo> getAllSysScores(Map<String, Object> mapWhere, List<String> sectionList);

    /**
     * 流程查询 方法 获取系统评分
     * @return
     */
    List<SysScoreVo> getFlowSysScores(Map<String, Object> mapWhere);

    /**
     * 查询需要考评并且当前时间在考评期间内的标段
     * @param projectId
     * @return
     */
    List<String> selectEvaluationSections(Integer projectId);

    /**
     * 生成系统评分
     * @param projectId 项目id
     * @param sectionId 标段id
     * @return
     */
    void createSysScores(Integer projectId, String sectionId);

    void approveQuaConceFlow(String bizType, List<Integer> ids);

    /**
     * 通过系统评分主键id 删除考评记录
     * @param ids
     */
    void deleteSysObjectScore(List<Integer> ids);
}
