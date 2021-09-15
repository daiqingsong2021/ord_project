package com.wisdom.acm.base.mapper;

import com.wisdom.acm.base.po.BaseTmplPlanPo;
import com.wisdom.acm.base.vo.tmpltask.BaseTmplPlanVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseTmplPlanMapper extends CommMapper<BaseTmplPlanPo> {

    /**
     * 获取计划模板的数据
     * @return
     */
    public List<BaseTmplPlanVo> selectTmplPlanList();

    /**
     * 获取计划模板的数据
     * @return
     */
    public List<BaseTmplPlanVo> selectTmplPlanSelectList(@Param("userId") Integer userId);

    /**
     *
     * @param id
     * @return
     */
    public BaseTmplPlanVo selectTmplPlanById(Integer id);


}