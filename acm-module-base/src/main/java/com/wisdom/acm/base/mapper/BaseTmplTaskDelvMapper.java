package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseTmplTaskDelvPo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplTaskDelvVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BaseTmplTaskDelvMapper extends CommMapper<BaseTmplTaskDelvPo> {

    /**
     * 查询交付清单列表
     * @param taskId
     * @return
     */
    public List<BaseTmplTaskDelvVo> selectTmplTaskDelvList(@Param("taskId") Integer taskId);

    /**
     * 查询交付清单列表
     * @param tmplId
     * @return
     */
    public List<BaseTmplTaskDelvVo> selectTmplTaskDelvListByTmplId(@Param("tmplId") Integer tmplId);

    public BaseTmplTaskDelvVo selectTaskDelvById(Integer id);

    public List<BaseTmplTaskDelvVo> selectTaskDelvByTaskDelv(Map<String,Integer> idMap);


    public List<BaseTmplTaskDelvVo> selectTaskDelvByIds(@Param("ids") List<Integer> ids);
}
