package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseTmplTaskPredPo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskPredVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BaseTmplTaskPredMapper extends CommMapper<BaseTmplTaskPredPo> {

    /**
     * 询计划模板紧前
     * @param tmplId
     * @return
     */
    public List<BaseTmplTaskPredVo> selectTmplTaskPredListByTmplId(@Param("tmplId") Integer tmplId);

    /**
     * 查询计划模板紧前逻辑关系列表
     * @param taskId
     * @return
     */
    public List<BaseTmplTaskPredVo> selectTmplTaskPredList(@Param("taskId") Integer taskId);

    /**
     * 查询计划模板后续逻辑关系列表
     * @param taskId
     * @return
     */
    public List<BaseTmplTaskPredVo> selectTmplTaskFllowList(@Param("taskId") Integer taskId);

    /**
     * 根据map查询逻辑关系列表
     */
    public List<BaseTmplTaskPredVo> queryTmpltaskTreeListByTwoIds(Map<String,Integer> map);

    /**
     * 根据map查询逻辑关系列表后续
     */
    public List<BaseTmplTaskPredVo> queryFollowTmpltaskTreeListByTwoIds(Map<String,Integer> map);

    /**
     * 根据id查BaseTmplTaskPredVo
     * @param id
     * @return
     */
    public BaseTmplTaskPredVo queryTmplTaskPredById(Integer id);
}
